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
import org.wso2.carbon.ob.migration.config.MigratorConfig;
import org.wso2.carbon.ob.migration.config.Version;
import org.wso2.carbon.ob.migration.service.Migrator;
import org.wso2.carbon.ob.migration.util.Constant;
import org.wso2.carbon.ob.migration.util.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Abstract class for version migration.
 */
public abstract class VersionMigration {

    private static final Logger log = LoggerFactory.getLogger(VersionMigration.class);

    public void migrate(String spec) throws MigrationClientException {

        String dryRun = System.getProperty(Constant.DRY_RUN);

        List<Migrator> migrators = getMigrators(spec);
        for (Migrator migrator : migrators) {
            log.info(Constant.MIGRATION_LOG + "Version : " + getCurrentVersion() + ", Migration Step : " +
                    migrator.getClass().getSimpleName() + " is starting........................... ");
            if (dryRun != null) {
                migrator.dryRun();
            } else {
                migrator.migrate();
            }
        }
    }

    public List<Migrator> getMigrators(String spec) throws MigrationClientException {

        List<Migrator> migrators = new ArrayList<>();
        Version version = getMigrationConfig();
        List<MigratorConfig> migratorConfigs = version.getMigratorConfigs();
        for (MigratorConfig migratorConfig : migratorConfigs) {
            if (migratorConfig.getSpec().equals(Specification.valueOf(spec))) {
                String migratorName = migratorConfig.getName();
                Migrator migrator = getMigrator(spec, migratorName);
                migrator.setMigratorConfig(migratorConfig);
                migrator.setVersionConfig(version);
                migrators.add(migrator);
            }
        }

        return migrators;
    }

    public abstract String getPreviousVersion();

    public abstract String getCurrentVersion();

    public Migrator getMigrator(String spec, String migratorName) {

        Package aPackage = this.getClass().getPackage();
        String basePackage = aPackage.getName() + "." + spec.toLowerCase(Locale.ROOT) + ".migrator";
        try {
            Class<?> migratorClass = Class.forName(basePackage  + "." + migratorName);
            return (Migrator) migratorClass.newInstance();
        } catch (ClassNotFoundException e) {
            log.error("Migrator class not exists for: " + migratorName);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("Error while creating migration instance", e);
        }
        return null;
    }

    public Version getMigrationConfig() throws MigrationClientException {

        Config config = Config.getInstance();
        return config.getMigrateVersion(getPreviousVersion());
    }

}
