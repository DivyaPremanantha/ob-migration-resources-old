package org.wso2.carbon.ob.migration.service.v200.uk.util;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.ob.migration.service.v200.uk.constants.DaoConstants;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKConsentBindingModel;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKAccountConsentRevHistoryModel;
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
        model.setCreatedTimestamp(resultSet.getTimestamp(DaoConstants.CREATED_TIMESTAMP).toLocalDateTime());
        model.setStatus(resultSet.getString(DaoConstants.STATUS));
        model.setClientId(resultSet.getString(DaoConstants.CLIENT_ID));
        model.setStatusUpdateTimestamp(resultSet.getTimestamp(DaoConstants.STATUS_UPDATED_TIMESTAMP).toLocalDateTime());
        model.setSpecVersion(resultSet.getString(DaoConstants.SPEC_VERSION));

        if (resultSet.getMetaData().getColumnCount() > 7) {
            model.setIdempotentKey(resultSet.getString(DaoConstants.IDEMPOTENT_KEY));
        }
        return model;
    }

    public static UKConsentBindingModel mapToConsentBindingModel(ResultSet resultSet)
            throws SQLException {

        UKConsentBindingModel model = new UKConsentBindingModel();
        model.setUserId(resultSet.getString(DaoConstants.USER_ID));
        model.setConsentId(resultSet.getString(DaoConstants.CONSENT_ID));
        model.setAccountId(resultSet.getString(DaoConstants.ACCOUNT_ID));
        model.setDebtorAccount(resultSet.getString(DaoConstants.DEBTOR_ACCOUNT));
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
