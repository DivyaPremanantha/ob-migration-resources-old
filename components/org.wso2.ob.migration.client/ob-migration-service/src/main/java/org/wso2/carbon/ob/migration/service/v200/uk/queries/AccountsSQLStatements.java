package org.wso2.carbon.ob.migration.service.v200.uk.queries;

/**
 * SQL statements common to all database types
 */
public class AccountsSQLStatements extends ConsentSQLStatements {

    public String getConsentInitiations() {

        return "SELECT * FROM UK_ACCOUNT_INITIATION";
    }

    public String getConsentBindingsByConsentId() {

        return "SELECT * FROM UK_ACCOUNT_CONSENT_BINDING WHERE CONSENT_ID = ?";
    }


    public String getConsentRevsByConsentId() {

        return "SELECT * FROM UK_CONSENT_REV WHERE CONSENT_ID = ?";
    }

    public String getConsentRevHistoryByConsentId() {

        return "SELECT * FROM UK_ACCOUNT_CONSENT_REV_HISTORY WHERE CONSENT_ID = ?";
    }
}
