package org.wso2.carbon.ob.migration.service.v200.dao.util;

import org.wso2.carbon.ob.migration.service.v200.dao.constants.DaoConstants;
import org.wso2.carbon.ob.migration.service.v200.dao.model.UKAccountConsentBindingModel;
import org.wso2.carbon.ob.migration.service.v200.dao.model.UKAccountConsentRevHistoryModel;
import org.wso2.carbon.ob.migration.service.v200.dao.model.UKAccountInitiationModel;
import org.wso2.carbon.ob.migration.service.v200.dao.model.UKConsentRevModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Utils class for consent module.
 */
public class ConsentDaoUtil {

    public static UKAccountInitiationModel mapToAccountInitiationModel(ResultSet resultSet) throws SQLException {

        UKAccountInitiationModel model = new UKAccountInitiationModel();
        model.setId(resultSet.getString(DaoConstants.ID));
        model.setRequest(resultSet.getString(DaoConstants.REQUEST));
        model.setCreatedTimestamp(resultSet.getTimestamp(DaoConstants.CREATED_TIMESTAMP).toLocalDateTime());
        model.setStatus(resultSet.getString(DaoConstants.STATUS));
        model.setClientId(resultSet.getString(DaoConstants.CLIENT_ID));
        model.setStatusUpdateTimestamp(resultSet.getTimestamp(DaoConstants.STATUS_UPDATED_TIMESTAMP).toLocalDateTime());
        model.setSpecVersion(resultSet.getString(DaoConstants.SPEC_VERSION));
        return model;
    }

    public static UKAccountConsentBindingModel mapToAccountConsentBindingModel(ResultSet resultSet)
            throws SQLException {

        UKAccountConsentBindingModel model = new UKAccountConsentBindingModel();
        model.setUserId(resultSet.getString(DaoConstants.USER_ID));
        model.setConsentId(resultSet.getString(DaoConstants.CONSENT_ID));
        model.setAccountId(resultSet.getString(DaoConstants.ACCOUNT_ID));
        model.setTimestamp(resultSet.getTimestamp(DaoConstants.TIMESTAMP).toLocalDateTime());
        model.setCollectionMethod(resultSet.getString(DaoConstants.COLLECTION_METHOD));
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
