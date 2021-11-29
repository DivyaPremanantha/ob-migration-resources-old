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
