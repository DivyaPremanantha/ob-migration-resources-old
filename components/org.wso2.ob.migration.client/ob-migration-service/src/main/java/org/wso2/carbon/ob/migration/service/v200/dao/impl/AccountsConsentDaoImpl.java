package org.wso2.carbon.ob.migration.service.v200.dao.impl;

import com.wso2.openbanking.accelerator.common.exception.OpenBankingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.ob.migration.service.v200.dao.AccountsConsentDao;
import org.wso2.carbon.ob.migration.service.v200.dao.model.UKAccountConsentBindingModel;
import org.wso2.carbon.ob.migration.service.v200.dao.model.UKAccountConsentRevHistoryModel;
import org.wso2.carbon.ob.migration.service.v200.dao.model.UKAccountInitiationModel;
import org.wso2.carbon.ob.migration.service.v200.dao.model.UKConsentRevModel;
import org.wso2.carbon.ob.migration.service.v200.dao.queries.AccountsSQLStatements;
import org.wso2.carbon.ob.migration.service.v200.dao.util.ConsentDaoUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountsConsentDaoImpl implements AccountsConsentDao {

    private static Log log = LogFactory.getLog(AccountsConsentDaoImpl.class);

    protected AccountsSQLStatements sqlStatements;

    public AccountsConsentDaoImpl(AccountsSQLStatements sqlStatements) {

        this.sqlStatements = sqlStatements;
    }

    @Override
    public List<UKAccountInitiationModel> getAccountConsentInitiations(Connection connection)
            throws OpenBankingException {

        String sqlStatements = this.sqlStatements.getConsentInitiations();
        List<UKAccountInitiationModel> accountInitiations = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatements)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    accountInitiations.add(ConsentDaoUtil.mapToAccountInitiationModel(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving account consent initiations");
            throw new OpenBankingException("Error occurred while retrieving account consent initiations");
        }

        return accountInitiations;
    }

    @Override
    public List<UKAccountConsentBindingModel> getAccountConsentBindingByConsentId(Connection connection,
                                                                                  String consentId)
            throws OpenBankingException {

        String sqlStatements = this.sqlStatements.getAccountConsentBindingsByConsentId();
        List<UKAccountConsentBindingModel> consentBindingModels = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatements)) {

            log.debug(String.format("Populating prepared statement with consentId : %s", consentId));
            preparedStatement.setString(1, consentId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    consentBindingModels.add(ConsentDaoUtil.mapToAccountConsentBindingModel(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving account consent bindings for consentId: " + consentId);
            throw new OpenBankingException("Error occurred while retrieving account consent bindings");
        }
        return consentBindingModels;
    }

    @Override
    public List<UKConsentRevModel> getConsentRevByConsentId(Connection connection, String consentId)
            throws OpenBankingException {

        String sqlStatements = this.sqlStatements.getAccountConsentRevsByConsentId();
        List<UKConsentRevModel> consentRevModels = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatements)) {

            log.debug(String.format("Populating prepared statement with consentId : %s", consentId));
            preparedStatement.setString(1, consentId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    consentRevModels.add(ConsentDaoUtil.mapToConsentRevModel(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving account consent rev for consentId: " + consentId);
            throw new OpenBankingException("Error occurred while retrieving account consent revocations");
        }
        return consentRevModels;
    }

    @Override
    public List<UKAccountConsentRevHistoryModel> getAccountConsentRevHistoryByConsentId(Connection connection,
                                                                                        String consentId)
            throws OpenBankingException {

        String sqlStatements = this.sqlStatements.getAccountConsentRevHistoryByConsentId();
        List<UKAccountConsentRevHistoryModel> consentRevHistoryModels = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatements)) {

            log.debug(String.format("Populating prepared statement with consentId : %s", consentId));
            preparedStatement.setString(1, consentId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    consentRevHistoryModels.add(ConsentDaoUtil.mapToConsentRevHistoryModel(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving account consent rev history for consentId: " + consentId);
            throw new OpenBankingException("Error occurred while retrieving account consent rev history");
        }
        return consentRevHistoryModels;
    }
}
