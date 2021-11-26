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

import java.time.LocalDateTime;

/**
 * Accounts consent binding model class for UK v200 DB schema
 */
public class UKConsentBindingModel {

    private String userId = null;
    private String consentId = null;
    private String accountId = null;
    private String debtorAccount = null;
    private LocalDateTime timestamp = null;
    private String collectionMethod = null;

    public UKConsentBindingModel(String userId, String consentId, String accountId, String debtorAccount,
                                 LocalDateTime timestamp, String collectionMethod) {

        this.userId = userId;
        this.consentId = consentId;
        this.accountId = accountId;
        this.debtorAccount = debtorAccount;
        this.timestamp = timestamp;
        this.collectionMethod = collectionMethod;
    }

    public UKConsentBindingModel() {

    }

    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {

        this.userId = userId;
    }

    public String getConsentId() {

        return consentId;
    }

    public void setConsentId(String consentId) {

        this.consentId = consentId;
    }

    public String getAccountId() {

        return accountId;
    }

    public void setAccountId(String accountId) {

        this.accountId = accountId;
    }

    public String getDebtorAccount() {

        return debtorAccount;
    }

    public void setDebtorAccount(String debtorAccount) {

        this.debtorAccount = debtorAccount;
    }

    public LocalDateTime getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {

        this.timestamp = timestamp;
    }

    public String getCollectionMethod() {

        return collectionMethod;
    }

    public void setCollectionMethod(String collectionMethod) {

        this.collectionMethod = collectionMethod;
    }
}
