/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com). All Rights Reserved.
 *
 * This software is the property of WSO2 Inc. and its suppliers, if any.
 * Dissemination of any information or reproduction of any material contained
 * herein is strictly forbidden, unless permitted by WSO2 in accordance with
 * the WSO2 Commercial License available at http://wso2.com/licenses. For specific
 * language governing the permissions and limitations under this license,
 * please see the license as well as any agreement you’ve entered into with
 * WSO2 governing the purchase of this software and any associated services.
 */
package org.wso2.carbon.ob.migration.service.v200.uk.dao;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.core.migrate.MigrationClientException;
import org.wso2.carbon.ob.migration.service.v200.uk.queries.AccountsSQLStatements;
import org.wso2.carbon.ob.migration.service.v200.uk.queries.FundsConfirmationSQLStatements;
import org.wso2.carbon.ob.migration.service.v200.uk.queries.PaymentsSQLStatements;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class handles consent DAO layer initiation with the relevant SQL statements per database types
 */
public class V200ConsentDaoInitializer {

    private static final String MYSQL = "MySQL";
    private static final String H2 = "H2";
    private static final String MICROSOFT = "Microsoft";
    private static final String MS_SQL = "MSSQL";
    private static final String POSTGRE = "PostgreSQL";
    private static final String ORACLE = "Oracle";
    private static V200ConsentDao accountsConsentDao = null;
    private static V200ConsentDao fundsConfirmationConsentDao = null;
    private static V200ConsentDao paymentsConsentDao = null;

    /**
     * Return the DAO implementation initialized for the relevant database type.
     *
     * @return the dao implementation
     * @throws MigrationClientException thrown if an error occurs when getting the database connection
     */
    public static synchronized V200ConsentDao initializeAccountsConsentDAO(Connection connection)
            throws MigrationClientException {

        if (accountsConsentDao == null) {
            try {
                String driverName = connection.getMetaData().getDriverName();
                if (StringUtils.isNotEmpty(driverName)) {
                    accountsConsentDao = new V200ConsentDaoImpl(new AccountsSQLStatements());
                } else {
                    throw new MigrationClientException("Unhandled DB driver: " + driverName + " detected : ");
                }
            } catch (SQLException e) {
                throw new MigrationClientException("Error while getting the database connection : ", e);
            }
        }
        return accountsConsentDao;
    }

    /**
     * Return the DAO implementation initialized for the relevant database type.
     *
     * @return the dao implementation
     * @throws MigrationClientException thrown if an error occurs when getting the database connection
     */
    public static synchronized V200ConsentDao initializeFundsConfirmationConsentDAO(Connection connection)
            throws MigrationClientException {

        if (fundsConfirmationConsentDao == null) {
            try {
                String driverName = connection.getMetaData().getDriverName();
                if (StringUtils.isNotEmpty(driverName)) {
                    fundsConfirmationConsentDao = new V200ConsentDaoImpl(new FundsConfirmationSQLStatements());
                } else {
                    throw new MigrationClientException("Unhandled DB driver: " + driverName + " detected : ");
                }
            } catch (SQLException e) {
                throw new MigrationClientException("Error while getting the database connection : ", e);
            }
        }
        return fundsConfirmationConsentDao;
    }

    /**
     * Return the DAO implementation initialized for the relevant database type.
     *
     * @return the dao implementation
     * @throws MigrationClientException thrown if an error occurs when getting the database connection
     */
    public static synchronized V200ConsentDao initializePaymentsConsentDAO(Connection connection)
            throws MigrationClientException {

        if (paymentsConsentDao == null) {
            try {
                String driverName = connection.getMetaData().getDriverName();
                if (StringUtils.isNotEmpty(driverName)) {
                    paymentsConsentDao = new V200ConsentDaoImpl(new PaymentsSQLStatements());
                } else {
                    throw new MigrationClientException("Unhandled DB driver: " + driverName + " detected : ");
                }
            } catch (SQLException e) {
                throw new MigrationClientException("Error while getting the database connection : ", e);
            }
        }
        return paymentsConsentDao;
    }
}
