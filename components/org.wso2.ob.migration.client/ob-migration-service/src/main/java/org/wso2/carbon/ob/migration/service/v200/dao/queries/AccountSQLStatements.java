package org.wso2.carbon.ob.migration.service.v200.dao.queries;

/**
 * SQL statements common to all database types
 */
public class AccountSQLStatements {

    public String getConsentInitiations() {

        return "SELECT * FROM UK_ACCOUNT_INITIATION";
    }
}
