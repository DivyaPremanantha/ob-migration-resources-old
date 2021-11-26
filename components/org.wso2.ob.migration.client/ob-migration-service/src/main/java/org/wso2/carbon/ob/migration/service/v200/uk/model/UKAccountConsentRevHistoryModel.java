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
package org.wso2.carbon.ob.migration.service.v200.uk.model;

/**
 * Consent rev model class for UK v200 DB schema
 */
public class UKAccountConsentRevHistoryModel {

    private String consentId = null;
    private String revocationUser = null;
    private String revocationReason = null;

    public UKAccountConsentRevHistoryModel(String consentId, String revocationUser, String revocationReason) {

        this.consentId = consentId;
        this.revocationUser = revocationUser;
        this.revocationReason = revocationReason;
    }

    public UKAccountConsentRevHistoryModel() {

    }

    public String getConsentId() {

        return consentId;
    }

    public void setConsentId(String consentId) {

        this.consentId = consentId;
    }

    public String getRevocationUser() {

        return revocationUser;
    }

    public void setRevocationUser(String revocationUser) {

        this.revocationUser = revocationUser;
    }

    public String getRevocationReason() {

        return revocationReason;
    }

    public void setRevocationReason(String revocationReason) {

        this.revocationReason = revocationReason;
    }
}
