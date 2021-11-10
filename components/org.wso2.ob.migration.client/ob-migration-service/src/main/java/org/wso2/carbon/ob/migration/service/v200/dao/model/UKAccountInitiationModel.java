package org.wso2.carbon.ob.migration.service.v200.dao.model;

import java.time.LocalDateTime;

/**
 * Accounts initiation model class for UK v200 DB schema
 */
public class UKAccountInitiationModel {

    private String id = null;
    private String request = null;
    private LocalDateTime createdTimestamp = null;
    private String status = null;
    private String clientId = null;
    private LocalDateTime statusUpdateTimestamp = null;
    private String specVersion = null;

    public UKAccountInitiationModel() {

    }

    public UKAccountInitiationModel(String id, String request, LocalDateTime createdTimestamp, String status,
                                    String clientId, LocalDateTime statusUpdateTimestamp, String specVersion) {

        this.id = id;
        this.request = request;
        this.createdTimestamp = createdTimestamp;
        this.status = status;
        this.clientId = clientId;
        this.statusUpdateTimestamp = statusUpdateTimestamp;
        this.specVersion = specVersion;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getRequest() {

        return request;
    }

    public void setRequest(String request) {

        this.request = request;
    }

    public LocalDateTime getCreatedTimestamp() {

        return createdTimestamp;
    }

    public void setCreatedTimestamp(LocalDateTime createdTimestamp) {

        this.createdTimestamp = createdTimestamp;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getClientId() {

        return clientId;
    }

    public void setClientId(String clientId) {

        this.clientId = clientId;
    }

    public LocalDateTime getStatusUpdateTimestamp() {

        return statusUpdateTimestamp;
    }

    public void setStatusUpdateTimestamp(LocalDateTime statusUpdateTimestamp) {

        this.statusUpdateTimestamp = statusUpdateTimestamp;
    }

    public String getSpecVersion() {

        return specVersion;
    }

    public void setSpecVersion(String specVersion) {

        this.specVersion = specVersion;
    }
}
