package org.wso2.carbon.ob.migration.service.v200.dao;

import com.wso2.openbanking.accelerator.common.exception.OpenBankingException;
import org.wso2.carbon.ob.migration.service.v200.dao.model.UKAccountConsentBindingModel;
import org.wso2.carbon.ob.migration.service.v200.dao.model.UKAccountConsentRevHistoryModel;
import org.wso2.carbon.ob.migration.service.v200.dao.model.UKAccountInitiationModel;
import org.wso2.carbon.ob.migration.service.v200.dao.model.UKConsentRevModel;

import java.sql.Connection;
import java.util.List;

/**
 * Data access interface for accounts related consents.
 */
public interface AccountsConsentDao {

    /**
     * Method to fetch account consent initiations
     *
     * @return
     */
    List<UKAccountInitiationModel> getAccountConsentInitiations(Connection connection) throws OpenBankingException;

    /**
     * Method to fetch account consent bindings by consentId
     *
     * @param consentId
     * @return
     */
    List<UKAccountConsentBindingModel> getAccountConsentBindingByConsentId(Connection connection, String consentId)
            throws OpenBankingException;

    /**
     * Method to fetch account consent revocations by consentId
     *
     * @param consentId
     * @return
     */
    List<UKConsentRevModel> getConsentRevByConsentId(Connection connection, String consentId)
            throws OpenBankingException;

    /**
     * Method to fetch account consent revocations by consentId
     *
     * @param consentId
     * @return
     */
    List<UKAccountConsentRevHistoryModel> getAccountConsentRevHistoryByConsentId(Connection connection,
                                                                                 String consentId)
            throws OpenBankingException;

}
