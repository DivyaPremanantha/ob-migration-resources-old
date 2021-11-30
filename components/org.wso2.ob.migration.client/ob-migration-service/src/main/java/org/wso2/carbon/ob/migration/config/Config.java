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
package org.wso2.carbon.ob.migration.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.identity.core.migrate.MigrationClientException;
import org.wso2.carbon.ob.migration.util.Constant;
import org.wso2.carbon.ob.migration.util.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Config holder for the migration service.
 */
public class Config {

    private static final Logger log = LoggerFactory.getLogger(Config.class);
    private static volatile Config config = null;
    private boolean migrationEnable;
    private String currentVersion;
    private String migrateVersion;
    private boolean continueOnError;
    private boolean batchUpdate;
    private boolean ignoreForInactiveTenants;
    private boolean migrateTenantRange;
    private int migrationStartingTenantID;
    private int migrationEndingTenantID;
    private List<Version> versions = new ArrayList<>();
    private String currentEncryptionAlgorithm;
    private String migratedEncryptionAlgorithm;
    private String dataSource;

    private Config() {

    }

    /**
     * Loading configs.
     *
     * @return
     */
    public static Config getInstance() {

        if (config == null) {
            synchronized (Config.class) {
                if (config == null) {
                    String migrationConfigFileName = Utility.getMigrationResourceDirectoryPath() + File.separator +
                            Constant.MIGRATION_CONFIG_FILE_NAME;
                    log.info(Constant.MIGRATION_LOG + "Loading Migration Configs, PATH:" + migrationConfigFileName);
                    try {
                        config = Utility.loadMigrationConfig(migrationConfigFileName);
                    } catch (MigrationClientException e) {
                        log.error("Error while loading migration configs.", e);
                    }
                    log.info(Constant.MIGRATION_LOG + "Successfully loaded the config file.");
                }
            }
        }

        return Config.config;
    }

    public static Config getConfig() {

        return config;
    }

    public static void setConfig(Config config) {

        Config.config = config;
    }

    public boolean isMigrationEnable() {

        return migrationEnable;
    }

    public void setMigrationEnable(boolean migrationEnable) {

        this.migrationEnable = migrationEnable;
    }

    public List<Version> getVersions() {

        return versions;
    }

    public void setVersions(List<Version> versions) {

        this.versions = versions;
    }

    public String getCurrentVersion() {

        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {

        this.currentVersion = currentVersion;
    }

    public String getMigrateVersion() {

        return migrateVersion;
    }

    public void setMigrateVersion(String migrateVersion) {

        this.migrateVersion = migrateVersion;
    }

    public Version getMigrateVersion(String version) {

        for (Version migrateVersion : versions) {
            if (migrateVersion.getVersion().equals(version)) {
                return migrateVersion;
            }
        }
        return null;
    }

    public boolean isContinueOnError() {

        return continueOnError;
    }

    public void setContinueOnError(boolean continueOnError) {

        this.continueOnError = continueOnError;
    }

    public boolean isBatchUpdate() {

        return batchUpdate;
    }

    public void setBatchUpdate(boolean batchUpdate) {

        this.batchUpdate = batchUpdate;
    }

    public boolean isIgnoreForInactiveTenants() {

        return ignoreForInactiveTenants;
    }

    public void setIgnoreForInactiveTenants(boolean ignoreForInactiveTenants) {

        this.ignoreForInactiveTenants = ignoreForInactiveTenants;
    }

    public boolean isMigrateTenantRange() {

        return migrateTenantRange;
    }

    public void setMigrateTenantRange(boolean migrateTenantRange) {

        this.migrateTenantRange = migrateTenantRange;
    }

    public int getMigrationStartingTenantID() {

        return migrationStartingTenantID;
    }

    public void setMigrationStartingTenantID(int migrationStartingTenantID) {

        this.migrationStartingTenantID = migrationStartingTenantID;
    }

    public int getMigrationEndingTenantID() {

        return migrationEndingTenantID;
    }

    public void setMigrationEndingTenantID(int migrationEndingTenantID) {

        this.migrationEndingTenantID = migrationEndingTenantID;
    }

    public String getCurrentEncryptionAlgorithm() {

        return currentEncryptionAlgorithm;
    }

    public void setCurrentEncryptionAlgorithm(String currentEncryptionAlgorithm) {

        this.currentEncryptionAlgorithm = currentEncryptionAlgorithm;
    }

    public String getMigratedEncryptionAlgorithm() {

        return migratedEncryptionAlgorithm;
    }

    public void setMigratedEncryptionAlgorithm(String migratedEncryptionAlgorithm) {

        this.migratedEncryptionAlgorithm = migratedEncryptionAlgorithm;
    }

    public String getDataSource() {

        return dataSource;
    }

    public void setDataSource(String dataSource) {

        this.dataSource = dataSource;
    }
}
