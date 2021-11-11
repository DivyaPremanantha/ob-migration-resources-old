package org.wso2.carbon.ob.migration.service.v200.uk.migrator;

import com.wso2.openbanking.accelerator.common.exception.OpenBankingException;
import com.wso2.openbanking.accelerator.consent.mgt.dao.ConsentCoreDAO;
import com.wso2.openbanking.accelerator.consent.mgt.dao.exceptions.OBConsentDataInsertionException;
import com.wso2.openbanking.accelerator.consent.mgt.dao.models.AuthorizationResource;
import com.wso2.openbanking.accelerator.consent.mgt.dao.models.ConsentResource;
import com.wso2.openbanking.accelerator.consent.mgt.dao.models.ConsentStatusAuditRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.identity.core.migrate.MigrationClientException;
import org.wso2.carbon.ob.migration.service.Migrator;
import org.wso2.carbon.ob.migration.service.v200.dao.AccountsConsentDao;
import org.wso2.carbon.ob.migration.service.v200.dao.V200ConsentDaoStoreInitializer;
import org.wso2.carbon.ob.migration.service.v200.dao.model.UKAccountInitiationModel;
import org.wso2.carbon.ob.migration.service.v200.uk.constants.UKCommonConstants;
import org.wso2.carbon.ob.migration.service.v200.uk.utils.UKUtils;
import org.wso2.carbon.ob.migration.service.v300.dao.V300ConsentDaoStoreInitializer;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class AccountConsentMigrator extends Migrator {

    private Connection conn = null;
    private static final Logger log = LoggerFactory.getLogger(AccountConsentMigrator.class);

    @Override
    public void dryRun() throws MigrationClientException {

        log.info("Dry run");
    }

    public void migrate() throws MigrationClientException {

        try (Connection connection = getDataSource().getConnection()) {
            connection.setAutoCommit(false);

            AccountsConsentDao v200AccountsConsentDao =
                    V200ConsentDaoStoreInitializer.initializeAccountsConsentDAO(connection);

            List<UKAccountInitiationModel> accountConsentInitiations =
                    v200AccountsConsentDao.getAccountConsentInitiations(connection);

            for (UKAccountInitiationModel accountInitiation : accountConsentInitiations) {
                String status = accountInitiation.getStatus();
                if (status.equalsIgnoreCase(UKCommonConstants.V2_AWAITING_AUTHORISATION)) {
                    handleAwaitingAuthAccountConsent(accountInitiation, connection);
                } else if (status.equalsIgnoreCase(UKCommonConstants.V2_AUTHORISED)) {
                    handleAuthorisedAccountConsent(accountInitiation);
                } else if (status.equalsIgnoreCase(UKCommonConstants.V2_REJECTED)) {
                    handleRejectedAccountConsent(accountInitiation);
                } else if (status.equalsIgnoreCase(UKCommonConstants.V2_REVOKED)) {
                    handleRevokedAccountConsent(accountInitiation);
                } else {
                    log.error(String.format("Unknown account consent status found %s for consentId : %s",
                            status, accountInitiation.getId()));
                }
            }
        } catch (SQLException | OpenBankingException e) {
            throw new MigrationClientException("Failed to migrate permissions.", e);
        }
    }

    private void handleAwaitingAuthAccountConsent(UKAccountInitiationModel accountInitiation, Connection connection)
            throws MigrationClientException, OBConsentDataInsertionException {

        // Store OB_CONSENT Row
        ConsentCoreDAO v300ConsentCoreDAO =
                V300ConsentDaoStoreInitializer.getInitializedConsentCoreDAOImpl(connection);
        ConsentResource consentResource = new ConsentResource();
        consentResource.setConsentID(accountInitiation.getId());
        consentResource.setReceipt(accountInitiation.getRequest());

        ZonedDateTime createdTime = accountInitiation.getCreatedTimestamp().atZone(ZoneId.systemDefault());
        consentResource.setCreatedTime(Instant.from(createdTime).getEpochSecond());

        ZonedDateTime updatedTime = accountInitiation.getStatusUpdateTimestamp().atZone(ZoneId.systemDefault());
        consentResource.setUpdatedTime(Instant.from(updatedTime).getEpochSecond());

        consentResource.setClientID(accountInitiation.getClientId());
        consentResource.setConsentType(UKCommonConstants.ACCOUNTS);
        consentResource.setCurrentStatus(UKCommonConstants.V3_AUTHORISED);
        consentResource.setConsentFrequency(0);
        consentResource.setRecurringIndicator(true);
        consentResource.setValidityPeriod(UKUtils.getExpirationTimeFromReceipt(accountInitiation.getRequest()));
        v300ConsentCoreDAO.storeConsentResource(connection, consentResource);

        // Store OB_CONSENT_AUTH_RESOURCE
        AuthorizationResource authResource = new AuthorizationResource();

        authResource.setUserID(null);
        authResource.setConsentID(accountInitiation.getId());
        authResource.setAuthorizationType(UKCommonConstants.AUTH_TYPE_AUTHORIZATION);
        authResource.setAuthorizationStatus(UKCommonConstants.AUTH_STATUS_CREATED);
        authResource.setUpdatedTime(Instant.from(updatedTime).getEpochSecond());
        v300ConsentCoreDAO.storeAuthorizationResource(connection, authResource);

        // Store OB_CONSENT_STATUS_AUDIT
        ConsentStatusAuditRecord consentStatusAuditRecord = new ConsentStatusAuditRecord();
        consentStatusAuditRecord.setConsentID(accountInitiation.getId());
        consentStatusAuditRecord.setCurrentStatus(UKCommonConstants.V3_AWAITING_AUTHORISATION);
        consentStatusAuditRecord.setActionTime(Instant.from(createdTime).getEpochSecond());
        consentStatusAuditRecord.setReason(UKCommonConstants.REASON_CREATE_CONSENT);
        consentStatusAuditRecord.setActionBy(null);
        consentStatusAuditRecord.setPreviousStatus(null);
        v300ConsentCoreDAO.storeConsentStatusAuditRecord(connection, consentStatusAuditRecord);

        //todo : add 'spec_version' consent attribute
    }

    private void handleAuthorisedAccountConsent(UKAccountInitiationModel accountInitiation) {
        //todo
    }

    private void handleRejectedAccountConsent(UKAccountInitiationModel accountInitiation) {
        //todo
    }

    private void handleRevokedAccountConsent(UKAccountInitiationModel accountInitiation) {
        //todo
    }

}
