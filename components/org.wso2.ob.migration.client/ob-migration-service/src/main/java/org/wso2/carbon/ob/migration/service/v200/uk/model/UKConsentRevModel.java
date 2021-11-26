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
 * Consent rev model class for UK v200 DB schema
 */
public class UKConsentRevModel {

    private String id = null;
    private String userId = null;
    private String consentId = null;
    private String accountId = null;
    private LocalDateTime timestamp = null;
    private String collectionMethod = null;

    public UKConsentRevModel(String id, String userId, String consentId, String accountId, LocalDateTime timestamp,
                             String collectionMethod) {

        this.id = id;
        this.userId = userId;
        this.consentId = consentId;
        this.accountId = accountId;
        this.timestamp = timestamp;
        this.collectionMethod = collectionMethod;
    }

    public UKConsentRevModel() {

    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
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
