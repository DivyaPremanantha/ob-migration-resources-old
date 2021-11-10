package org.wso2.carbon.ob.migration.service.v200.dao.model;

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
