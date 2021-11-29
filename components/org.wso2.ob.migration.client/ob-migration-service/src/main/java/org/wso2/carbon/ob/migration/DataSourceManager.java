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
package org.wso2.carbon.ob.migration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.identity.core.migrate.MigrationClientException;
import org.wso2.carbon.ob.migration.config.Config;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Class for datasource management.
 */
public class DataSourceManager {

    private static final Logger log = LoggerFactory.getLogger(DataSourceManager.class);
    private static volatile DataSourceManager dataSourceManager = null;
    private DataSource dataSource;

    private DataSourceManager() {

        try {
            initObDataSource();
        } catch (MigrationClientException e) {
            log.error("Error while initializing datasource manager.", e);
        }
    }

    public static DataSourceManager getInstance() {

        if (DataSourceManager.dataSourceManager == null) {
            synchronized (DataSourceManager.class) {
                if (DataSourceManager.dataSourceManager == null) {
                    DataSourceManager.dataSourceManager = new DataSourceManager();
                }
            }
        }
        return DataSourceManager.dataSourceManager;
    }

    /**
     * Initialize Consent Data Source.
     *
     * @throws MigrationClientException
     */
    private void initObDataSource() throws MigrationClientException {
        Config config = Config.getInstance();
        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup(config.getDataSource());
            if (dataSource == null) {
                String errorMsg = "OB Datasource initialization error.";
                throw new MigrationClientException(errorMsg);
            }
        } catch (NamingException e) {
            String msg = "Error while looking up the data source: " + config.getDataSource();
            log.error(msg);
            throw new MigrationClientException(msg);
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
