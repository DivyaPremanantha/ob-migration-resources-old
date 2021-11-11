package org.wso2.carbon.ob.migration.service.v200.uk.dao;

import com.wso2.openbanking.accelerator.common.exception.OpenBankingException;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKConsentBindingModel;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKAccountConsentRevHistoryModel;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKConsentInitiationModel;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKConsentRevModel;

import java.sql.Connection;
import java.util.List;

/**
 * Data access interface for accounts related consents.
 */
public interface V200ConsentDao {

    /**
     * Method to fetch consent initiations
     *
     * @return
     */
    List<UKConsentInitiationModel> getConsentInitiations(Connection connection) throws OpenBankingException;

    /**
     * Method to fetch consent bindings by consentId
     *
     * @param consentId
     * @return
     */
    List<UKConsentBindingModel> getConsentBindingByConsentId(Connection connection, String consentId)
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
     * Method to fetch consent revocations by consentId
     *
     * @param consentId
     * @return
     */
    List<UKAccountConsentRevHistoryModel> getConsentRevHistoryByConsentId(Connection connection,
                                                                                 String consentId)
            throws OpenBankingException;

}
