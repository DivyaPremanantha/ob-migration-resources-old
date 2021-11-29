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
 * Accounts initiation model class for UK v200 DB schema
 */
public class UKConsentInitiationModel {

    private String id = null;
    private String request = null;
    private LocalDateTime createdTimestamp = null;
    private String status = null;
    private String clientId = null;
    private LocalDateTime statusUpdateTimestamp = null;
    private String specVersion = null;
    private String idempotentKey = null;

    public UKConsentInitiationModel() {

    }

    public UKConsentInitiationModel(String id, String request, LocalDateTime createdTimestamp, String status,
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

    public String getIdempotentKey() {
        return idempotentKey;
    }

    public void setIdempotentKey(String idempotentKey) {
        this.idempotentKey = idempotentKey;
    }
}
