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
import org.wso2.carbon.core.ServerStartupObserver;
import org.wso2.carbon.ob.migration.config.Config;
import org.wso2.carbon.ob.migration.util.Constant;
import org.wso2.carbon.ob.migration.util.Utility;

import java.util.List;

/**
 * MigrationClientImpl is the one that trigger by the relevant component trigger to start the migration service.
 */
public class MigrationClientImpl implements ServerStartupObserver {

    private static final Logger log = LoggerFactory.getLogger(MigrationClientImpl.class);


    @Override
    public void completingServerStartup() {

    }

    @Override
    public void completedServerStartup() {

        try {
            Config config = Config.getInstance();
            String spec = System.getProperty("migrationSpec");
            log.info("............................................................................................");
            log.info("....." + spec + ".............................................................................." +
                    "....." +
                    ".");

            if (!config.isMigrationEnable()) {
                return;
            }

            log.info("............................................................................................");
            log.info("............................................................................................");
            log.info("............................................................................................");
            log.info("............................................................................................");
            log.info("............................................................................................");
            log.info("............................................................................................");
            log.info("............................................................................................");
            log.info("............................................................................................");
            log.info("............................................................................................");
            log.info("............................................................................................");
            log.info("............................................................................................");

            VersionMigrationHolder versionMigrationHolder = VersionMigrationHolder.getInstance();
            List<VersionMigration> versionMigrationList = versionMigrationHolder.getVersionMigrationList();

            log.info("Migration Versions List.........................");

            boolean isMigrationStarted = false;
            if (Utility.isMigrateTenantRange()) {
                log.info("Migration started for the tenant range " + Utility.getMigrationStartingTenantID() + " - "
                        + Utility.getMigrationEndingTenantID());
            }

            for (VersionMigration versionMigration : versionMigrationList) {
                log.info(versionMigration.getPreviousVersion() + " to " + versionMigration.getCurrentVersion());
                log.info(Constant.MIGRATION_LOG + "Start Version : " + versionMigration.getPreviousVersion() + " to "
                        + versionMigration.getCurrentVersion());
                if (!isMigrationStarted && versionMigration.getPreviousVersion().equals(config.getCurrentVersion())) {
                    versionMigration.migrate(spec);
                    isMigrationStarted = true;
                    if (versionMigration.getCurrentVersion().equals(config.getMigrateVersion())) {
                        break;
                    }
                    continue;
                }

                if (isMigrationStarted) {
                    versionMigration.migrate(spec);
                    if (versionMigration.getCurrentVersion().equals(config.getMigrateVersion())) {
                        break;
                    }
                }
            }
            log.info(Constant.MIGRATION_LOG + "Execution was done through all the requested version list without "
                    + "having unexpected issues. There may be some steps that is not executed correctly but bypass "
                    + "that because of enabling 'continueOnError' property. Please see the above logs to more "
                    + "details.");
        } catch (Throwable e) {
            log.error("Migration process was stopped.", e);
        }

        log.info("............................................................................................");
        log.info("............................................................................................");
        log.info("............................................................................................");
        log.info("............................................................................................");
        log.info("............................................................................................");
        log.info("............................................................................................");
        log.info("............................................................................................");
        log.info("............................................................................................");
        log.info("............................................................................................");
    }
}
