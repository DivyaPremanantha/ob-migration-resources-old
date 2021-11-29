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
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKAccountConsentRevHistoryModel;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKConsentBindingModel;
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

    /**
     * Method to store consent file by consentId
     * @param connection
     * @param consentId
     * @return
     */
    boolean storeConsentFile(Connection connection, String consentId) throws OpenBankingException;

    /**
     * Method to get file upload idempotency key by consentId
     * @param connection
     * @param consentId
     * @return
     */
    String getFileUploadIdempotencyKeyByConsentId(Connection connection, String consentId)
            throws OpenBankingException;

}
