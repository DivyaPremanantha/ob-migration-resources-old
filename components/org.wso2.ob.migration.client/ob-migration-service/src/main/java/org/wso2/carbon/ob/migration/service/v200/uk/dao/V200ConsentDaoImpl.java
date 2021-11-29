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

import com.wso2.openbanking.accelerator.common.exception.OpenBankingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.wso2.carbon.ob.migration.service.v200.uk.constants.DaoConstants;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKAccountConsentRevHistoryModel;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKConsentBindingModel;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKConsentInitiationModel;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKConsentRevModel;
import org.wso2.carbon.ob.migration.service.v200.uk.queries.ConsentSQLStatements;
import org.wso2.carbon.ob.migration.service.v200.uk.util.ConsentDaoUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class V200ConsentDaoImpl implements V200ConsentDao {

    private static Log log = LogFactory.getLog(V200ConsentDaoImpl.class);

    protected ConsentSQLStatements sqlStatements;

    public V200ConsentDaoImpl(ConsentSQLStatements sqlStatements) {

        this.sqlStatements = sqlStatements;
    }

    @Override
    public List<UKConsentInitiationModel> getConsentInitiations(Connection connection)
            throws OpenBankingException {

        String sqlStatements = this.sqlStatements.getConsentInitiations();
        List<UKConsentInitiationModel> consentInitiations = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatements)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    consentInitiations.add(ConsentDaoUtil.mapToConsentInitiationModel(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving consent initiations" + e);
            throw new OpenBankingException("Error occurred while retrieving consent initiations", e);
        }

        return consentInitiations;
    }

    @Override
    public List<UKConsentBindingModel> getConsentBindingByConsentId(Connection connection,
                                                                    String consentId)
            throws OpenBankingException {

        String sqlStatements = this.sqlStatements.getConsentBindingsByConsentId();
        List<UKConsentBindingModel> consentBindingModels = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatements)) {

            log.debug(String.format("Populating prepared statement with consentId : %s", consentId));
            preparedStatement.setString(1, consentId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    consentBindingModels.add(ConsentDaoUtil.mapToConsentBindingModel(resultSet));
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

        String sqlStatements = this.sqlStatements.getConsentRevsByConsentId();
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
    public List<UKAccountConsentRevHistoryModel> getConsentRevHistoryByConsentId(Connection connection,
                                                                                 String consentId)
            throws OpenBankingException {

        String sqlStatements = this.sqlStatements.getConsentRevHistoryByConsentId();
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

    @Override
    public boolean storeConsentFile(Connection connection, String consentId) throws OpenBankingException {

        String sqlStatements = this.sqlStatements.getStoreConsentFileByConsentId();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatements)) {

            log.debug(String.format("Populating prepared statement with consentId : %s", consentId));
            preparedStatement.setString(1, consentId);
            if (preparedStatement.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException e) {
            log.error("Error occurred while storing consent file for consentId: " + consentId);
            throw new OpenBankingException("Error occurred while storing consent file");
        }
        log.error("Error occurred while storing consent file for consentId: " + consentId);
        return false;
    }

    @Override
    public String getFileUploadIdempotencyKeyByConsentId(Connection connection, String consentId) throws OpenBankingException {

        String sqlStatements = this.sqlStatements.getFileUploadIdempotencyKeyByConsentId();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatements)) {

            log.debug(String.format("Populating prepared statement with consentId : %s", consentId));
            preparedStatement.setString(1, consentId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    return resultSet.getString(DaoConstants.IDEMPOTENT_KEY);
                }
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving IDEMPOTENT_KEY for consentId: " + consentId);
            throw new OpenBankingException("Error occurred while retrieving IDEMPOTENT_KEY");
        }
        log.debug("No IDEMPOTENT_KEY found for consentId: " + consentId);
        return "";
    }
}
