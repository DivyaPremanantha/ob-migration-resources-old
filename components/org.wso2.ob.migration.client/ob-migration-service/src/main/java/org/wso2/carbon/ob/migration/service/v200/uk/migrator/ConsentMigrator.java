package org.wso2.carbon.ob.migration.service.v200.uk.migrator;

import com.wso2.openbanking.accelerator.common.exception.OpenBankingException;
import com.wso2.openbanking.accelerator.consent.mgt.dao.ConsentCoreDAO;
import com.wso2.openbanking.accelerator.consent.mgt.dao.models.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.identity.core.migrate.MigrationClientException;
import org.wso2.carbon.ob.migration.service.Migrator;
import org.wso2.carbon.ob.migration.service.v200.uk.constants.UKCommonConstants;
import org.wso2.carbon.ob.migration.service.v200.uk.dao.V200ConsentDao;
import org.wso2.carbon.ob.migration.service.v200.uk.dao.V200ConsentDaoInitializer;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKAccountConsentRevHistoryModel;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKConsentBindingModel;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKConsentInitiationModel;
import org.wso2.carbon.ob.migration.service.v200.uk.util.UKUtils;
import org.wso2.carbon.ob.migration.service.v300.dao.V300ConsentDaoStoreInitializer;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class ConsentMigrator extends Migrator {

    private static final Logger log = LoggerFactory.getLogger(ConsentMigrator.class);

    @Override
    public void dryRun() throws MigrationClientException {

        log.info("Dry run");
    }

    public void migrate() throws MigrationClientException {

        try (Connection connection = getDataSource().getConnection()) {
            connection.setAutoCommit(false);

            V200ConsentDao v200AccountsConsentDao = V200ConsentDaoInitializer.initializeAccountsConsentDAO(connection);
            migrateConsent(connection, v200AccountsConsentDao, UKCommonConstants.ACCOUNTS);

            V200ConsentDao v200FundsConfirmationConsentDao =
                    V200ConsentDaoInitializer.initializeFundsConfirmationConsentDAO(connection);
            migrateConsent(connection, v200FundsConfirmationConsentDao, UKCommonConstants.FUNDS_CONFIRMATIONS);

            V200ConsentDao v200PaymentsConsentDao =
                    V200ConsentDaoInitializer.initializePaymentsConsentDAO(connection);
            migrateConsent(connection, v200PaymentsConsentDao, UKCommonConstants.PAYMENTS);

        } catch (SQLException | OpenBankingException e) {
            throw new MigrationClientException("Failed to migrate permissions.", e);
        }
    }

    private void migrateConsent(Connection connection, V200ConsentDao v200ConsentDao, String consentType) throws
            OpenBankingException, MigrationClientException {

        List<UKConsentInitiationModel> consentInitiations = v200ConsentDao.getConsentInitiations(connection);
        for (UKConsentInitiationModel consentInitiation : consentInitiations) {
            handleConsent(consentInitiation, connection, consentType, consentInitiation.getStatus());
        }
    }

    private void handleConsent(UKConsentInitiationModel consentInitiation, Connection connection,
                               String consentType, String status)
            throws MigrationClientException, OpenBankingException {
        long createdTime = Instant.from(consentInitiation.getCreatedTimestamp().atZone(ZoneId.systemDefault()))
                .getEpochSecond();
        long updatedTime = Instant.from(consentInitiation.getStatusUpdateTimestamp().atZone(ZoneId.systemDefault()))
                .getEpochSecond();
        ConsentCoreDAO v300ConsentCoreDAO = V300ConsentDaoStoreInitializer.getInitializedConsentCoreDAOImpl(connection);
        String fileUploadIdempotencyKey = null;
        String currentStatus = getV3Status(status);

        // Store OB_CONSENT Row
        ConsentResource consentResource = new ConsentResource(consentInitiation.getId(),
                consentInitiation.getClientId(), consentInitiation.getRequest(), consentType, 0,
                getValidityPeriod(consentInitiation, consentType), getRecurringIndicator(consentType),
                currentStatus, createdTime, updatedTime);
        v300ConsentCoreDAO.storeConsentResource(connection, consentResource);

        // Store OB_CONSENT_AUTH_RESOURCE
        if (UKCommonConstants.V3_AWAITING_AUTHORISATION.equalsIgnoreCase(currentStatus)) {
            AuthorizationResource authorizationResource = new AuthorizationResource(consentInitiation.getId(), null,
                    UKCommonConstants.AUTH_STATUS_CREATED, UKCommonConstants.AUTH_TYPE_AUTHORIZATION,
                    consentResource.getUpdatedTime());
            v300ConsentCoreDAO.storeAuthorizationResource(connection, authorizationResource);
        } else {
            V200ConsentDao v200ConsentDao = getV200ConsentDao(consentType, connection);
            List<UKConsentBindingModel> consentBindingByConsentId =
                    v200ConsentDao.getConsentBindingByConsentId(connection, consentInitiation.getId());
            List<String> distinctUserIds = consentBindingByConsentId.stream()
                    .map(UKConsentBindingModel::getUserId).distinct().collect(Collectors.toList());

            for (String userId : distinctUserIds) {
                // Store OB_CONSENT_AUTH_RESOURCE for each user
                String authType = getAuthType(distinctUserIds);
                Map<String, String> statusMap = getStatus(currentStatus);
                AuthorizationResource authorizationResource = new AuthorizationResource(consentInitiation.getId(), userId,
                        statusMap.get(UKCommonConstants.AUTH_STATUS), authType, consentResource.getUpdatedTime());
                v300ConsentCoreDAO.storeAuthorizationResource(connection, authorizationResource);

                List<UKConsentBindingModel> userConsentBindings = consentBindingByConsentId.stream()
                        .filter(ukConsentBindingModel -> ukConsentBindingModel.getUserId().equalsIgnoreCase(userId))
                        .collect(Collectors.toList());
                // Store OB_CONSENT_MAPPING for each accountIds
                for (UKConsentBindingModel userConsentBinding : userConsentBindings) {
                    ConsentMappingResource consentMappingResource =
                            new ConsentMappingResource(authorizationResource.getAuthorizationID(),
                                    getAccountID(userConsentBinding, consentType),
                                    UKCommonConstants.NOT_APPLICABLE, statusMap.get(UKCommonConstants.CONSENT_STATUS));
                    v300ConsentCoreDAO.storeConsentMappingResource(connection, consentMappingResource);
                }
            }
            long actionTime = createdTime;
            String actionBy = null;
            if (!consentBindingByConsentId.isEmpty()) {
                // set actionTime from consent binding.
                ZonedDateTime actionTimestamp = consentBindingByConsentId.get(0)
                        .getTimestamp().atZone(ZoneId.systemDefault());
                actionTime = Instant.from(actionTimestamp).getEpochSecond();
                // todo get verified for multiple user scenarios audit user
                actionBy = distinctUserIds.get(0);
            }
            if (UKCommonConstants.V3_REJECTED.equalsIgnoreCase(currentStatus)) {
                ConsentStatusAuditRecord rejectedConsentAudit = new ConsentStatusAuditRecord(consentInitiation.getId(),
                        UKCommonConstants.V3_REJECTED, updatedTime,
                        UKCommonConstants.REASON_BIND_CONSENT,
                        actionBy, UKCommonConstants.V3_AWAITING_AUTHORISATION);
                v300ConsentCoreDAO.storeConsentStatusAuditRecord(connection, rejectedConsentAudit);
            } else {
                ConsentStatusAuditRecord authorizedConsentAudit = new ConsentStatusAuditRecord(consentInitiation.getId(),
                        UKCommonConstants.V3_AUTHORISED, actionTime, UKCommonConstants.REASON_BIND_CONSENT,
                        actionBy, UKCommonConstants.V3_AWAITING_AUTHORISATION);
                v300ConsentCoreDAO.storeConsentStatusAuditRecord(connection, authorizedConsentAudit);

                if (UKCommonConstants.V3_REVOKED.equalsIgnoreCase(currentStatus)) {
                    List<UKAccountConsentRevHistoryModel> revHistoryModels =
                            v200ConsentDao.getConsentRevHistoryByConsentId(connection,
                                    consentInitiation.getId());
                    ConsentStatusAuditRecord revokedConsentAudit = new ConsentStatusAuditRecord(consentInitiation.getId(),
                            UKCommonConstants.V3_REVOKED, updatedTime, revHistoryModels.get(0).getRevocationReason(),
                            revHistoryModels.get(0).getRevocationUser(), UKCommonConstants.V3_AUTHORISED);
                    v300ConsentCoreDAO.storeConsentStatusAuditRecord(connection, revokedConsentAudit);
                } else if (UKCommonConstants.V3_CONSUMED.equalsIgnoreCase(currentStatus)) {
                    ConsentStatusAuditRecord consumedConsentAudit =
                            new ConsentStatusAuditRecord(consentInitiation.getId(),
                                    UKCommonConstants.V3_CONSUMED, updatedTime,
                                    UKCommonConstants.REASON_RECEIVE_SUBMISSION_REQUEST_FOR_CONSENT,
                                    v200ConsentDao.getConsentBindingByConsentId(connection,
                                            consentInitiation.getId()).get(0).getUserId()
                                    , UKCommonConstants.V3_AUTHORISED);
                    v300ConsentCoreDAO.storeConsentStatusAuditRecord(connection, consumedConsentAudit);
                }

            }
            if (UKCommonConstants.PAYMENTS.equalsIgnoreCase(consentType)) {
                // Store payment consent file
                v200ConsentDao.storeConsentFile(connection, consentInitiation.getId());
                // Check for file upload idempotency key
                fileUploadIdempotencyKey = v200ConsentDao
                        .getFileUploadIdempotencyKeyByConsentId(connection, consentInitiation.getId());
            }
        }

        // Store OB_CONSENT_STATUS_AUDIT
        ConsentStatusAuditRecord consentStatusAuditRecord = new ConsentStatusAuditRecord(consentInitiation.getId(),
                UKCommonConstants.V3_AWAITING_AUTHORISATION, consentResource.getCreatedTime(),
                UKCommonConstants.REASON_CREATE_CONSENT, null, null);
        v300ConsentCoreDAO.storeConsentStatusAuditRecord(connection, consentStatusAuditRecord);

        // Store OB_CONSENT_ATTRIBUTE
        Map<String, String> attributesMap = getAttributesMap(consentInitiation, fileUploadIdempotencyKey, consentType);
        ConsentAttributes consentAttributes = new ConsentAttributes(consentInitiation.getId(), attributesMap);
        v300ConsentCoreDAO.storeConsentAttributes(connection, consentAttributes);
    }

    private V200ConsentDao getV200ConsentDao(String consentType, Connection connection) throws MigrationClientException {
        V200ConsentDao v200ConsentDao;
        if (UKCommonConstants.ACCOUNTS.equalsIgnoreCase(consentType)) {
            v200ConsentDao = V200ConsentDaoInitializer.initializeAccountsConsentDAO(connection);
        } else if (UKCommonConstants.FUNDS_CONFIRMATIONS.equalsIgnoreCase(consentType)) {
            v200ConsentDao =
                    V200ConsentDaoInitializer.initializeFundsConfirmationConsentDAO(connection);
        } else {
            v200ConsentDao =
                    V200ConsentDaoInitializer.initializePaymentsConsentDAO(connection);
        }
        return v200ConsentDao;
    }

    private String getV3Status(String status) {
        String currentStatus;
        if (UKCommonConstants.V2_AWAITING_AUTHORISATION.equalsIgnoreCase(status)) {
            currentStatus = UKCommonConstants.V3_AWAITING_AUTHORISATION;
        } else {
            currentStatus = status.toLowerCase(Locale.ROOT);
        }
        return currentStatus;
    }

    private String getAccountID(UKConsentBindingModel userConsentBinding, String consentType) {
        String accountID;
        if (UKCommonConstants.PAYMENTS.equalsIgnoreCase(consentType)) {
            accountID = userConsentBinding.getDebtorAccount();
        } else {
            accountID = userConsentBinding.getAccountId();
        }
        return accountID;
    }

    private Map<String, String> getStatus(String status) {
        Map<String, String> statusMap = new HashMap<>();
        if (UKCommonConstants.V3_AUTHORISED.equalsIgnoreCase(status)) {
            statusMap.put(UKCommonConstants.AUTH_STATUS, UKCommonConstants.V3_AUTHORISED);
            statusMap.put(UKCommonConstants.CONSENT_STATUS, UKCommonConstants.CONSENT_MAPPING_STATUS_ACTIVE);
        } else if (UKCommonConstants.V3_REJECTED.equalsIgnoreCase(status)) {
            statusMap.put(UKCommonConstants.AUTH_STATUS, UKCommonConstants.V3_REJECTED);
            statusMap.put(UKCommonConstants.CONSENT_STATUS, UKCommonConstants.CONSENT_MAPPING_STATUS_INACTIVE);
        } else if (UKCommonConstants.V2_CONSUMED.equalsIgnoreCase(status)) {
            statusMap.put(UKCommonConstants.AUTH_STATUS, UKCommonConstants.V3_CONSUMED);
            statusMap.put(UKCommonConstants.CONSENT_STATUS, UKCommonConstants.CONSENT_MAPPING_STATUS_ACTIVE);
        } else {
            statusMap.put(UKCommonConstants.AUTH_STATUS, UKCommonConstants.V3_REVOKED);
            statusMap.put(UKCommonConstants.CONSENT_STATUS, UKCommonConstants.CONSENT_MAPPING_STATUS_INACTIVE);
        }
        return statusMap;
    }

    private String getAuthType(List<String> distinctUserIds) {
        // todo check multi auth table in payment flow
        String authType;
        if (distinctUserIds.size() > 1) {
            authType = UKCommonConstants.AUTH_TYPE_MULTI_AUTHORIZATION;
        } else {
            authType = UKCommonConstants.AUTH_TYPE_AUTHORIZATION;
        }
        return authType;
    }

    private boolean getRecurringIndicator(String consentType) {
        return !(UKCommonConstants.PAYMENTS.equalsIgnoreCase(consentType));
    }

    private long getValidityPeriod(UKConsentInitiationModel consentInitiation, String consentType) {
        if (!(UKCommonConstants.PAYMENTS.equalsIgnoreCase(consentType))) {
            return UKUtils.getExpirationTimeFromReceipt(consentInitiation.getRequest());
        }
        return 0;
    }

    private Map<String, String> getAttributesMap(UKConsentInitiationModel consentInitiation,
                                                 String fileUploadIdempotencyKey, String consentType) {
        Map<String, String> attributesMap = new HashMap<>();
        attributesMap.put(UKCommonConstants.SPEC_VERSION, consentInitiation.getSpecVersion());
        if (UKCommonConstants.PAYMENTS.equalsIgnoreCase(consentType)) {
            attributesMap.put(UKCommonConstants.IDEMPOTENT_KEY, consentInitiation.getIdempotentKey());
        }
        if (StringUtils.isNotEmpty(fileUploadIdempotencyKey) && !fileUploadIdempotencyKey.isEmpty()) {
            attributesMap.put(UKCommonConstants.FILE_UPLOAD_IDEMPOTENCY_KEY, fileUploadIdempotencyKey);
        }
        return attributesMap;
    }
}
