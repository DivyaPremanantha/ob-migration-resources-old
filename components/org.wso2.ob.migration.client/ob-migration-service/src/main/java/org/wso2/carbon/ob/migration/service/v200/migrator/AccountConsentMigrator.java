package org.wso2.carbon.ob.migration.service.v200.migrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.identity.core.migrate.MigrationClientException;
import org.wso2.carbon.ob.migration.service.Migrator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountConsentMigrator extends Migrator {

    private Connection conn = null;
    private static final Logger log = LoggerFactory.getLogger(AccountConsentMigrator.class);

    @Override
    public void dryRun() throws MigrationClientException {
        log.info("Dry run");
    }

    public void migrate() throws MigrationClientException {
        try (Connection connection = getDataSource().getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from OB_CONSENT");
            ResultSet resultSet = preparedStatement.executeQuery();
            String t = "";
            while (resultSet.next()) {
                t = resultSet.getString("CONSENT_ID");
            }
            log.error("CONSENT_ID: " + t);

            PreparedStatement preparedStatement3 = connection.prepareStatement("select * from UK_ACCOUNT_INITIATION");
            ResultSet resultSet3 = preparedStatement3.executeQuery();
            String t3 = "";
            while (resultSet3.next()) {
                t3 = resultSet3.getString("REQUEST");
            }
            log.info("REQUEST: " + t3);
        } catch (
                SQLException e) {
            throw new MigrationClientException("Failed to migrate permissions.", e);
        }
    }
}
