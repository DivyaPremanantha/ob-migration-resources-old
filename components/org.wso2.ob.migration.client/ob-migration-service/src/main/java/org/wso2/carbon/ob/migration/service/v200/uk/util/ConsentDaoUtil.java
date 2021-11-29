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
package org.wso2.carbon.ob.migration.service.v200.uk.util;

import org.wso2.carbon.ob.migration.service.v200.uk.constants.DaoConstants;
import org.wso2.carbon.ob.migration.service.v200.uk.model.DCRModel;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKAccountConsentRevHistoryModel;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKConsentBindingModel;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKConsentInitiationModel;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKConsentRevModel;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utils class for consent module.
 */
public class ConsentDaoUtil {

    public static UKConsentInitiationModel mapToConsentInitiationModel(ResultSet resultSet) throws SQLException {

        UKConsentInitiationModel model = new UKConsentInitiationModel();
        model.setId(resultSet.getString(DaoConstants.ID));
        model.setRequest(resultSet.getString(DaoConstants.REQUEST));
        model.setStatus(resultSet.getString(DaoConstants.STATUS));
        model.setClientId(resultSet.getString(DaoConstants.CLIENT_ID));
        model.setStatusUpdateTimestamp(resultSet.getTimestamp(DaoConstants.STATUS_UPDATED_TIMESTAMP).toLocalDateTime());
        model.setSpecVersion(resultSet.getString(DaoConstants.SPEC_VERSION));

        if (resultSet.getMetaData().getColumnCount() > 7) {
            model.setIdempotentKey(resultSet.getString(DaoConstants.IDEMPOTENT_KEY));
            model.setCreatedTimestamp(resultSet.getTimestamp(DaoConstants.TIMESTAMP).toLocalDateTime());
        } else {
            model.setCreatedTimestamp(resultSet.getTimestamp(DaoConstants.CREATED_TIMESTAMP).toLocalDateTime());
        }
        return model;
    }

    public static DCRModel mapToDCRModel(ResultSet resultSet) throws SQLException {

        DCRModel dcrModel = new DCRModel();
        dcrModel.setId(resultSet.getString(DaoConstants.ID));
        dcrModel.setClientId(resultSet.getString(DaoConstants.CLIENT_ID));
        dcrModel.setRequest(resultSet.getString(DaoConstants.REQUEST));

        return dcrModel;
    }

    public static UKConsentBindingModel mapToConsentBindingModel(ResultSet resultSet)
            throws SQLException {

        UKConsentBindingModel model = new UKConsentBindingModel();
        model.setUserId(resultSet.getString(DaoConstants.USER_ID));
        model.setConsentId(resultSet.getString(DaoConstants.CONSENT_ID));
        model.setTimestamp(resultSet.getTimestamp(DaoConstants.TIMESTAMP).toLocalDateTime());
        model.setCollectionMethod(resultSet.getString(DaoConstants.COLLECTION_METHOD));

        if (resultSet.getMetaData().getColumnName(3).equalsIgnoreCase(DaoConstants.DEBTOR_ACCOUNT)) {
            model.setDebtorAccount(resultSet.getString(DaoConstants.DEBTOR_ACCOUNT));
        } else {
            model.setAccountId(resultSet.getString(DaoConstants.ACCOUNT_ID));
        }

        return model;
    }

    public static UKConsentRevModel mapToConsentRevModel(ResultSet resultSet) throws SQLException {

        UKConsentRevModel model = new UKConsentRevModel();
        model.setUserId(resultSet.getString(DaoConstants.USER_ID));
        model.setId(resultSet.getString(DaoConstants.ID));
        model.setConsentId(resultSet.getString(DaoConstants.CONSENT_ID));
        model.setAccountId(resultSet.getString(DaoConstants.ACCOUNT_ID));
        model.setTimestamp(resultSet.getTimestamp(DaoConstants.TIMESTAMP).toLocalDateTime());
        model.setCollectionMethod(resultSet.getString(DaoConstants.COLLECTION_METHOD));
        return model;
    }

    public static UKAccountConsentRevHistoryModel mapToConsentRevHistoryModel(ResultSet resultSet) throws SQLException {

        UKAccountConsentRevHistoryModel model = new UKAccountConsentRevHistoryModel();
        model.setConsentId(resultSet.getString(DaoConstants.CONSENT_ID));
        model.setRevocationUser(resultSet.getString(DaoConstants.REVOCATION_USER));
        model.setRevocationReason(resultSet.getString(DaoConstants.REVOCATION_REASON));
        return model;
    }
}
