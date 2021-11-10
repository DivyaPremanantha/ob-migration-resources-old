package org.wso2.carbon.ob.migration.service.v200.dao;

import org.wso2.carbon.identity.core.migrate.MigrationClientException;
import org.wso2.carbon.ob.migration.service.v200.dao.impl.AccountsConsentDaoImpl;
import org.wso2.carbon.ob.migration.service.v200.dao.queries.AccountsSQLStatements;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class handles consent DAO layer initiation with the relevant SQL statements per database types
 */
public class V200ConsentDaoStoreInitializer {

    private static final String MYSQL = "MySQL";
    private static final String H2 = "H2";
    private static final String MICROSOFT = "Microsoft";
    private static final String MS_SQL = "MSSQL";
    private static final String POSTGRE = "PostgreSQL";
    private static final String ORACLE = "Oracle";
    private static AccountsConsentDao accountsConsentDao = null;

    /**
     * Return the DAO implementation initialized for the relevant database type.
     *
     * @return the dao implementation
     * @throws MigrationClientException thrown if an error occurs when getting the database connection
     */
    public static synchronized AccountsConsentDao initializeAccountsConsentDAO(Connection connection)
            throws MigrationClientException {

        if (accountsConsentDao == null) {
            try {
                String driverName = connection.getMetaData().getDriverName();
                if (driverName.contains(MYSQL)) {
                    accountsConsentDao = new AccountsConsentDaoImpl(new AccountsSQLStatements());
                } else if (driverName.contains(MS_SQL) || driverName.contains(MICROSOFT)) {
                    accountsConsentDao = new AccountsConsentDaoImpl(new AccountsSQLStatements());
                } else if (driverName.contains(POSTGRE)) {
                    accountsConsentDao = new AccountsConsentDaoImpl(new AccountsSQLStatements());
                } else if (driverName.contains(ORACLE)) {
                    accountsConsentDao = new AccountsConsentDaoImpl(new AccountsSQLStatements());
                } else {
                    throw new MigrationClientException("Unhandled DB driver: " + driverName + " detected : ");
                }
            } catch (SQLException e) {
                throw new MigrationClientException("Error while getting the database connection : ", e);
            }
        }
        return accountsConsentDao;
    }
}
