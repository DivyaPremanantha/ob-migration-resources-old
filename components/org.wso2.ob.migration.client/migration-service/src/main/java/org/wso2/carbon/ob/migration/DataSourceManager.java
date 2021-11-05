/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.ob.migration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.consent.mgt.core.util.ConsentConfigParser;
import org.wso2.carbon.identity.core.migrate.MigrationClientException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Class for datasource management.
 */
public class DataSourceManager {

    private static final Logger log = LoggerFactory.getLogger(DataSourceManager.class);
    private static DataSourceManager dataSourceManager = null;
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
            DataSourceManager.dataSourceManager = new DataSourceManager();
        }
        return DataSourceManager.dataSourceManager;
    }

    /**
     * Initialize Consent Data Source.
     *
     * @throws MigrationClientException
     */
    private void initObDataSource() throws MigrationClientException {

        ConsentConfigParser configParser = new ConsentConfigParser();
        // check for a config reader
//        String dataSourceName = configParser.getConsentDataSource();
        String dataSourceName = "WSO2OB_DB";
        if (dataSourceName == null) {
            String errorMsg = "DataSource Element is not available for Consent management";
            log.error(errorMsg);
            throw new MigrationClientException(errorMsg);
        }

        // Should work but not sure
        Context ctx;
        try {
            ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup(dataSourceName);

        } catch (NamingException e) {
            String errorMsg = "Error when looking up the Consent Data Source.";
            throw new MigrationClientException(errorMsg, e);
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
