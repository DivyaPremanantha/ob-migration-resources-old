package org.wso2.carbon.ob.migration.service.v200.uk.dao;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.core.migrate.MigrationClientException;
import org.wso2.carbon.ob.migration.service.v200.uk.queries.AccountsSQLStatements;
import org.wso2.carbon.ob.migration.service.v200.uk.queries.ConsentSQLStatements;
import org.wso2.carbon.ob.migration.service.v200.uk.queries.DCRSQLStatements;

import java.sql.Connection;
import java.sql.SQLException;

public class V200DCRDaoInitializer {

    private static V200DCRDao v200DCRDao = null;


    public static synchronized V200DCRDao initializeDCRDao(Connection connection)
            throws MigrationClientException {
        if (v200DCRDao == null) {
            try {
                String driverName = connection.getMetaData().getDriverName();
                if (StringUtils.isNotEmpty(driverName)) {
                    v200DCRDao = new V200DCRDAoImpl(new DCRSQLStatements());
                } else {
                    throw new MigrationClientException("Unhandled DB driver: " + driverName + " detected : ");
                }
            } catch (SQLException e) {
                throw new MigrationClientException("Error while getting the database connection : ", e);
            }
        }
        return v200DCRDao;
    }
}
