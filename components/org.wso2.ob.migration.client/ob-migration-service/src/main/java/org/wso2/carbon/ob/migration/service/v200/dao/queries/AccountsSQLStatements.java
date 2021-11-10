package org.wso2.carbon.ob.migration.service.v200.dao.queries;

/**
 * SQL statements common to all database types
 */
public class AccountsSQLStatements {

    public String getConsentInitiations() {

        return "SELECT * FROM UK_ACCOUNT_INITIATION";
    }

    public String getAccountConsentBindingsByConsentId() {

        return "SELECT * FROM UK_ACCOUNT_CONSENT_BINDING WHERE CONSENT_ID = ?";
    }


    public String getAccountConsentRevsByConsentId() {

        return "SELECT * FROM UK_CONSENT_REV WHERE CONSENT_ID = ?";
    }

    public String getAccountConsentRevHistoryByConsentId() {

        return "SELECT * FROM UK_ACCOUNT_CONSENT_REV_HISTORY WHERE CONSENT_ID = ?";
    }
}
