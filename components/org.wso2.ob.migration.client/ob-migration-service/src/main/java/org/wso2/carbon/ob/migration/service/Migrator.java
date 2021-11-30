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
package org.wso2.carbon.ob.migration.service;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.core.migrate.MigrationClientException;
import org.wso2.carbon.ob.migration.DataSourceManager;
import org.wso2.carbon.ob.migration.config.Config;
import org.wso2.carbon.ob.migration.config.MigratorConfig;
import org.wso2.carbon.ob.migration.config.Version;

import javax.sql.DataSource;

/**
 * Abstract class for Migrator contract. All migration implementation should be implemented from this class.
 */
public abstract class Migrator {

    public static final String SCHEMA = "schema";
    public static final String CONTINUE_ON_ERROR = "continueOnError";
    public static final String BATCH_UPDATE = "batchUpdate";

    private MigratorConfig migratorConfig;
    private Version versionConfig;

    public MigratorConfig getMigratorConfig() {

        return this.migratorConfig;
    }

    public void setMigratorConfig(MigratorConfig migratorConfig) {

        this.migratorConfig = migratorConfig;
    }

    public DataSource getDataSource() throws MigrationClientException {

        DataSource dataSource = DataSourceManager.getInstance().getDataSource();
        return dataSource;
    }

    public boolean isContinueOnError() {

        String continueOnError = getMigratorConfig().getParameterValue(CONTINUE_ON_ERROR);
        if (StringUtils.isBlank(continueOnError)) {
            return Config.getInstance().isContinueOnError();
        }
        return Boolean.parseBoolean(continueOnError);
    }

    public boolean isBatchUpdate() {

        String batchUpdate = getMigratorConfig().getParameterValue(BATCH_UPDATE);
        if (StringUtils.isBlank(batchUpdate)) {
            return Config.getInstance().isBatchUpdate();
        }
        return Boolean.parseBoolean(batchUpdate);
    }

    public String getSchema() {

        return getMigratorConfig().getParameterValue(SCHEMA);
    }

    public Version getVersionConfig() {

        return versionConfig;
    }

    public void setVersionConfig(Version versionConfig) {

        this.versionConfig = versionConfig;
    }

    /**
     * Do a dry run instead of doing the migration.
     *
     * @throws MigrationClientException
     */
    public abstract void dryRun() throws MigrationClientException;

    /**
     * Migrator specific implementation.
     *
     * @throws MigrationClientException
     */
    public abstract void migrate() throws MigrationClientException;

}
