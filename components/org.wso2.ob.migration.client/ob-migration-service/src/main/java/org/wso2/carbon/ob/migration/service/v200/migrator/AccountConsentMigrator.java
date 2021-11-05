package org.wso2.carbon.ob.migration.service.v200.migrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.identity.core.migrate.MigrationClientException;
import org.wso2.carbon.ob.migration.service.Migrator;

public class AccountConsentMigrator extends Migrator {

    private static final Logger log = LoggerFactory.getLogger(AccountConsentMigrator.class);

    @Override
    public void dryRun() throws MigrationClientException {
        log.info("Dry run");
    }

    public void migrate() throws MigrationClientException {
        log.info("Migrate");
    }
}
