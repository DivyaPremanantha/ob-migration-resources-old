package org.wso2.carbon.ob.migration.service.v200.dao.model;

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
