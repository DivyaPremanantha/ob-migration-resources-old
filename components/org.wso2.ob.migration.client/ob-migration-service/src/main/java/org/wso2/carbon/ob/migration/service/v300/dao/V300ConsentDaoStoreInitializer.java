package org.wso2.carbon.ob.migration.service.v300.dao;

import com.wso2.openbanking.accelerator.consent.mgt.dao.ConsentCoreDAO;
import com.wso2.openbanking.accelerator.consent.mgt.dao.impl.ConsentCoreDAOImpl;
import com.wso2.openbanking.accelerator.consent.mgt.dao.impl.MssqlConsentCoreDAOImpl;
import com.wso2.openbanking.accelerator.consent.mgt.dao.impl.OracleConsentCoreDAOImpl;
import com.wso2.openbanking.accelerator.consent.mgt.dao.queries.ConsentMgtCommonDBQueries;
import com.wso2.openbanking.accelerator.consent.mgt.dao.queries.ConsentMgtMssqlDBQueries;
import com.wso2.openbanking.accelerator.consent.mgt.dao.queries.ConsentMgtOracleDBQueries;
import com.wso2.openbanking.accelerator.consent.mgt.dao.queries.ConsentMgtPostgresDBQueries;
import org.wso2.carbon.identity.core.migrate.MigrationClientException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class handles consent DAO layer initiation with the relevant SQL statements per database types
 */
public class V300ConsentDaoStoreInitializer {

    private static final String MYSQL = "MySQL";
    private static final String H2 = "H2";
    private static final String MICROSOFT = "Microsoft";
    private static final String MS_SQL = "MSSQL";
    private static final String POSTGRE = "PostgreSQL";
    private static final String ORACLE = "Oracle";
    private static ConsentCoreDAO consentCoreDAO = null;

    /**
     * Return the DAO implementation initialized for the relevant database type.
     *
     * @return the dao implementation
     * @throws MigrationClientException thrown if an error occurs when getting the database connection
     */
    public static synchronized ConsentCoreDAO getInitializedConsentCoreDAOImpl(Connection connection)
            throws MigrationClientException {

        if (consentCoreDAO == null) {
            try {
                String driverName = connection.getMetaData().getDriverName();

                if (driverName.contains(MYSQL)) {
                    consentCoreDAO = new ConsentCoreDAOImpl(new ConsentMgtCommonDBQueries());
                } else if (driverName.contains(H2)) {
                    consentCoreDAO = new ConsentCoreDAOImpl(new ConsentMgtCommonDBQueries());
                } else if (driverName.contains(MS_SQL) || driverName.contains(MICROSOFT)) {
                    consentCoreDAO = new MssqlConsentCoreDAOImpl(new ConsentMgtMssqlDBQueries());
                } else if (driverName.contains(POSTGRE)) {
                    consentCoreDAO = new ConsentCoreDAOImpl(new ConsentMgtPostgresDBQueries());
                } else if (driverName.contains(ORACLE)) {
                    consentCoreDAO = new OracleConsentCoreDAOImpl(new ConsentMgtOracleDBQueries());
                } else {
                    throw new MigrationClientException("Unhandled DB driver: " + driverName + " detected : ");
                }
            } catch (SQLException e) {
                throw new MigrationClientException("Error while getting the database connection : ", e);
            }
        }
        return consentCoreDAO;
    }
}
