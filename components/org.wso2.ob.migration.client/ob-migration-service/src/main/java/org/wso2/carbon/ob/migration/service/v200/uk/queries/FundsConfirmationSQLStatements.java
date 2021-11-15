package org.wso2.carbon.ob.migration.service.v200.uk.queries;

/**
 * SQL statements common to all database types
 */
public class FundsConfirmationSQLStatements extends ConsentSQLStatements{

    public String getConsentInitiations() {

        return "SELECT * FROM UK_COF_INITIATION";
    }

    public String getConsentBindingsByConsentId() {

        return "SELECT * FROM UK_COF_CONSENT_BINDING WHERE CONSENT_ID = ?";
    }


    public String getConsentRevsByConsentId() {

        return "SELECT * FROM UK_CONSENT_REV WHERE CONSENT_ID = ?";
    }

    public String getConsentRevHistoryByConsentId() {

        return "SELECT * FROM UK_COF_CONSENT_REV_HISTORY WHERE CONSENT_ID = ?";
    }

    // Consent file will be skipped for cof
    @Override
    public String getStoreConsentFileByConsentId() {

        return "";
    }

    // Consent file will be skipped for cof
    @Override
    public String getFileUploadIdempotencyKeyByConsentId() {

        return "";
    }
}
