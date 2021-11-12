package org.wso2.carbon.ob.migration.service.v200.uk.migrator;

import com.wso2.openbanking.accelerator.common.exception.OpenBankingException;
import com.wso2.openbanking.accelerator.consent.mgt.dao.ConsentCoreDAO;
import com.wso2.openbanking.accelerator.consent.mgt.dao.exceptions.OBConsentDataInsertionException;
import com.wso2.openbanking.accelerator.consent.mgt.dao.models.AuthorizationResource;
import com.wso2.openbanking.accelerator.consent.mgt.dao.models.ConsentAttributes;
import com.wso2.openbanking.accelerator.consent.mgt.dao.models.ConsentMappingResource;
import com.wso2.openbanking.accelerator.consent.mgt.dao.models.ConsentResource;
import com.wso2.openbanking.accelerator.consent.mgt.dao.models.ConsentStatusAuditRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.identity.core.migrate.MigrationClientException;
import org.wso2.carbon.ob.migration.service.Migrator;
import org.wso2.carbon.ob.migration.service.v200.uk.dao.V200ConsentDao;
import org.wso2.carbon.ob.migration.service.v200.uk.dao.V200ConsentDaoInitializer;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKConsentBindingModel;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKConsentInitiationModel;
import org.wso2.carbon.ob.migration.service.v200.uk.constants.UKCommonConstants;
import org.wso2.carbon.ob.migration.service.v200.uk.util.UKUtils;
import org.wso2.carbon.ob.migration.service.v300.dao.V300ConsentDaoStoreInitializer;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConsentMigrator extends Migrator {

    private Connection conn = null;
    private static final Logger log = LoggerFactory.getLogger(ConsentMigrator.class);

    @Override
    public void dryRun() throws MigrationClientException {

        log.info("Dry run");
    }

    public void migrate() throws MigrationClientException {

        try (Connection connection = getDataSource().getConnection()) {
            connection.setAutoCommit(false);

            V200ConsentDao v200AccountsConsentDao = V200ConsentDaoInitializer.initializeAccountsConsentDAO(connection);
            migrateConsentInitiation(connection, v200AccountsConsentDao, UKCommonConstants.ACCOUNTS);

            V200ConsentDao v200FundsConfirmationConsentDao =
                    V200ConsentDaoInitializer.initializeFundsConfirmationConsentDAO(connection);
            migrateConsentInitiation(connection, v200FundsConfirmationConsentDao, UKCommonConstants.FUNDS_CONFIRMATIONS);

            V200ConsentDao v200PaymentsConsentDao =
                    V200ConsentDaoInitializer.initializePaymentsConsentDAO(connection);
            migrateConsentInitiation(connection, v200PaymentsConsentDao, UKCommonConstants.PAYMENTS);

        } catch (SQLException | OpenBankingException e) {
            throw new MigrationClientException("Failed to migrate permissions.", e);
        }
    }

    private void migrateConsentInitiation(Connection connection, V200ConsentDao v200ConsentDao, String consentType) throws
            OpenBankingException, MigrationClientException {

        List<UKConsentInitiationModel> consentInitiations = v200ConsentDao.getConsentInitiations(connection);
        for (UKConsentInitiationModel consentInitiation : consentInitiations) {
            String status = consentInitiation.getStatus();
            if (status.equalsIgnoreCase(UKCommonConstants.V2_AWAITING_AUTHORISATION)) {
                handleAwaitingAuthAccountConsent(consentInitiation, connection, consentType);
            } else if (status.equalsIgnoreCase(UKCommonConstants.V2_AUTHORISED)) {
                handleAuthorisedAccountConsent(consentInitiation, connection, consentType);
            } else if (status.equalsIgnoreCase(UKCommonConstants.V2_REJECTED)) {
                handleRejectedAccountConsent(consentInitiation);
            } else if (status.equalsIgnoreCase(UKCommonConstants.V2_REVOKED)) {
                handleRevokedAccountConsent(consentInitiation);
            } else {
                log.error(String.format("Unknown account consent status found %s for consentId : %s",
                        status, consentInitiation.getId()));
            }
        }
    }

    private void handleAwaitingAuthAccountConsent(UKConsentInitiationModel consentInitiation, Connection connection,
                                                  String consentType)
            throws MigrationClientException, OBConsentDataInsertionException {

        ConsentCoreDAO v300ConsentCoreDAO =
                V300ConsentDaoStoreInitializer.getInitializedConsentCoreDAOImpl(connection);

        // Store OB_CONSENT Row
        ConsentResource consentResource = new ConsentResource();
        consentResource.setConsentID(consentInitiation.getId());
        consentResource.setReceipt(consentInitiation.getRequest());
        ZonedDateTime createdTime = consentInitiation.getCreatedTimestamp().atZone(ZoneId.systemDefault());
        consentResource.setCreatedTime(Instant.from(createdTime).getEpochSecond());
        ZonedDateTime updatedTime = consentInitiation.getStatusUpdateTimestamp().atZone(ZoneId.systemDefault());
        consentResource.setUpdatedTime(Instant.from(updatedTime).getEpochSecond());
        consentResource.setClientID(consentInitiation.getClientId());
        consentResource.setConsentType(consentType);
        consentResource.setCurrentStatus(UKCommonConstants.V3_AWAITING_AUTHORISATION);
        if (UKCommonConstants.PAYMENTS.equalsIgnoreCase(consentType)) {
            consentResource.setConsentFrequency(0);
            consentResource.setRecurringIndicator(false);
            consentResource.setValidityPeriod(0);
        } else {
            consentResource.setConsentFrequency(0);
            consentResource.setRecurringIndicator(true);
            consentResource.setValidityPeriod(UKUtils.getExpirationTimeFromReceipt(consentInitiation.getRequest()));
        }
        v300ConsentCoreDAO.storeConsentResource(connection, consentResource);

        // Store OB_CONSENT_AUTH_RESOURCE
        AuthorizationResource authResource = new AuthorizationResource();
        authResource.setUserID(null);
        authResource.setConsentID(consentInitiation.getId());
        authResource.setAuthorizationType(UKCommonConstants.AUTH_TYPE_AUTHORIZATION);
        authResource.setAuthorizationStatus(UKCommonConstants.AUTH_STATUS_CREATED);
        authResource.setUpdatedTime(Instant.from(updatedTime).getEpochSecond());
        v300ConsentCoreDAO.storeAuthorizationResource(connection, authResource);

        // Store OB_CONSENT_STATUS_AUDIT
        ConsentStatusAuditRecord consentStatusAuditRecord = new ConsentStatusAuditRecord();
        consentStatusAuditRecord.setConsentID(consentInitiation.getId());
        consentStatusAuditRecord.setCurrentStatus(UKCommonConstants.V3_AWAITING_AUTHORISATION);
        consentStatusAuditRecord.setActionTime(Instant.from(createdTime).getEpochSecond());
        consentStatusAuditRecord.setReason(UKCommonConstants.REASON_CREATE_CONSENT);
        consentStatusAuditRecord.setActionBy(null);
        consentStatusAuditRecord.setPreviousStatus(null);
        v300ConsentCoreDAO.storeConsentStatusAuditRecord(connection, consentStatusAuditRecord);

        // Store spec_version as consent attribute
        ConsentAttributes consentAttributes = new ConsentAttributes();
        Map<String, String> attributesMap = new HashMap<>();
        attributesMap.put(UKCommonConstants.SPEC_VERSION, consentInitiation.getSpecVersion());
        consentAttributes.setConsentID(consentInitiation.getId());
        consentAttributes.setConsentAttributes(attributesMap);
        v300ConsentCoreDAO.storeConsentAttributes(connection, consentAttributes);
    }

    private void handleAuthorisedAccountConsent(UKConsentInitiationModel consentInitiation, Connection connection,
                                                String consentType)
            throws MigrationClientException, OpenBankingException {

        ConsentCoreDAO v300ConsentCoreDAO =
                V300ConsentDaoStoreInitializer.getInitializedConsentCoreDAOImpl(connection);
        V200ConsentDao v200AccountsConsentDao = V200ConsentDaoInitializer.initializeAccountsConsentDAO(connection);

        // Store OB_CONSENT Row
        ConsentResource consentResource = new ConsentResource();
        consentResource.setConsentID(consentInitiation.getId());
        consentResource.setReceipt(consentInitiation.getRequest());
        ZonedDateTime createdTime = consentInitiation.getCreatedTimestamp().atZone(ZoneId.systemDefault());
        consentResource.setCreatedTime(Instant.from(createdTime).getEpochSecond());
        ZonedDateTime updatedTime = consentInitiation.getStatusUpdateTimestamp().atZone(ZoneId.systemDefault());
        consentResource.setUpdatedTime(Instant.from(updatedTime).getEpochSecond());
        consentResource.setClientID(consentInitiation.getClientId());
        consentResource.setConsentType(consentType);
        consentResource.setCurrentStatus(UKCommonConstants.V3_AUTHORISED);
        if (UKCommonConstants.PAYMENTS.equalsIgnoreCase(consentType)) {
            consentResource.setConsentFrequency(0);
            consentResource.setRecurringIndicator(false);
            consentResource.setValidityPeriod(0);
        } else {
            consentResource.setConsentFrequency(0);
            consentResource.setRecurringIndicator(true);
            consentResource.setValidityPeriod(UKUtils.getExpirationTimeFromReceipt(consentInitiation.getRequest()));
        }
        v300ConsentCoreDAO.storeConsentResource(connection, consentResource);

        List<UKConsentBindingModel> consentBindingByConsentId =
                v200AccountsConsentDao.getConsentBindingByConsentId(connection, consentInitiation.getId());
        List<String> distinctUserIds = consentBindingByConsentId.stream()
                .map(UKConsentBindingModel::getUserId).distinct().collect(Collectors.toList());

        for (String userId : distinctUserIds) {
            // Store OB_CONSENT_AUTH_RESOURCE for each user
            AuthorizationResource authResource = new AuthorizationResource();
            authResource.setUserID(userId);
            authResource.setConsentID(consentInitiation.getId());
            // todo check multi auth table in payment flow
            if (distinctUserIds.size() > 1) {
                authResource.setAuthorizationType(UKCommonConstants.AUTH_TYPE_MULTI_AUTHORIZATION);
            } else {
                authResource.setAuthorizationType(UKCommonConstants.AUTH_TYPE_AUTHORIZATION);
            }
            authResource.setAuthorizationStatus(UKCommonConstants.AUTH_STATUS_AUTHORIZED);
            authResource.setUpdatedTime(Instant.from(updatedTime).getEpochSecond());
            AuthorizationResource authorizationResource =
                    v300ConsentCoreDAO.storeAuthorizationResource(connection, authResource);

            List<UKConsentBindingModel> userConsentBindings = consentBindingByConsentId.stream()
                    .filter(ukConsentBindingModel -> ukConsentBindingModel.getUserId().equalsIgnoreCase(userId))
                    .collect(Collectors.toList());

            // Store OB_CONSENT_MAPPING for each accountIds
            for (UKConsentBindingModel userConsentBinding : userConsentBindings) {
                ConsentMappingResource consentMappingResource = new ConsentMappingResource();
                consentMappingResource.setAuthorizationID(authorizationResource.getAuthorizationID());
                if (UKCommonConstants.PAYMENTS.equalsIgnoreCase(consentType)) {
                    consentMappingResource.setAccountID(userConsentBinding.getDebtorAccount());
                } else {
                    consentMappingResource.setAccountID(userConsentBinding.getAccountId());
                }
                consentMappingResource.setPermission(UKCommonConstants.NOT_APPLICABLE);
                consentMappingResource.setMappingStatus(UKCommonConstants.CONSENT_MAPPING_STATUS_ACTIVE);
                v300ConsentCoreDAO.storeConsentMappingResource(connection, consentMappingResource);
            }
        }

        String fileUploadIdempotencyKey = "";
        if (UKCommonConstants.PAYMENTS.equalsIgnoreCase(consentType)) {
            // Store payment consent file
            v200AccountsConsentDao.storeConsentFile(connection, consentInitiation.getId());
            // Check for file upload idempotency key
            fileUploadIdempotencyKey = v200AccountsConsentDao
                    .getFileUploadIdempotencyKeyByConsentId(connection, consentInitiation.getId());
        }

        // Store awaitingAuth OB_CONSENT_STATUS_AUDIT
        ConsentStatusAuditRecord consentStatusAuditRecord = new ConsentStatusAuditRecord();
        consentStatusAuditRecord.setConsentID(consentInitiation.getId());
        consentStatusAuditRecord.setCurrentStatus(UKCommonConstants.V3_AWAITING_AUTHORISATION);
        consentStatusAuditRecord.setActionTime(Instant.from(createdTime).getEpochSecond());
        consentStatusAuditRecord.setReason(UKCommonConstants.REASON_CREATE_CONSENT);
        consentStatusAuditRecord.setActionBy(null);
        consentStatusAuditRecord.setPreviousStatus(null);
        v300ConsentCoreDAO.storeConsentStatusAuditRecord(connection, consentStatusAuditRecord);

        // Store authorized OB_CONSENT_STATUS_AUDIT
        ConsentStatusAuditRecord authorizedConsentAudit = new ConsentStatusAuditRecord();
        authorizedConsentAudit.setConsentID(consentInitiation.getId());
        authorizedConsentAudit.setCurrentStatus(UKCommonConstants.V3_AUTHORISED);
        long actionTime = Instant.from(createdTime).getEpochSecond();
        if (!consentBindingByConsentId.isEmpty()) {
            // set actionTime from consent binding.
            ZonedDateTime actionTimestamp = consentBindingByConsentId.get(0)
                    .getTimestamp().atZone(ZoneId.systemDefault());
            actionTime = Instant.from(actionTimestamp).getEpochSecond();
            // todo get verified for multiple user scenarios audit user
            authorizedConsentAudit.setActionBy(distinctUserIds.get(0));
        }
        authorizedConsentAudit.setActionTime(actionTime);
        authorizedConsentAudit.setReason(UKCommonConstants.REASON_BIND_CONSENT);
        authorizedConsentAudit.setPreviousStatus(UKCommonConstants.V3_AWAITING_AUTHORISATION);
        v300ConsentCoreDAO.storeConsentStatusAuditRecord(connection, authorizedConsentAudit);

        // Store spec_version as consent attribute
        ConsentAttributes consentAttributes = new ConsentAttributes();
        Map<String, String> attributesMap = new HashMap<>();
        attributesMap.put(UKCommonConstants.SPEC_VERSION, consentInitiation.getSpecVersion());
        attributesMap.put(UKCommonConstants.IDEMPOTENT_KEY, consentInitiation.getIdempotentKey());
        if (!fileUploadIdempotencyKey.isEmpty()) {
            attributesMap.put(UKCommonConstants.FILE_UPLOAD_IDEMPOTENCY_KEY, fileUploadIdempotencyKey);
        }
        consentAttributes.setConsentID(consentInitiation.getId());
        consentAttributes.setConsentAttributes(attributesMap);
        v300ConsentCoreDAO.storeConsentAttributes(connection, consentAttributes);

    }

    private void handleRejectedAccountConsent(UKConsentInitiationModel accountInitiation) {
        //todo
    }

    private void handleRevokedAccountConsent(UKConsentInitiationModel accountInitiation) {
        //todo
    }

}
