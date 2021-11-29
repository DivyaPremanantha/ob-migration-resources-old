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

public class DCRModel {
    private String id = null;
    private String tppName = null;
    private String requestHeader = null;
    private String request = null;
    private String clientId = null;
    private String softwareEnv = null;
    private String applicationId = null;
    private String softwareId = null;
    private String specVersion = null;
    private String createdTimestamp = null;
    private String status = null;
    private String clidIssuedAt = null;
    private String statusUpdateTimestamp = null;

    public DCRModel(){

    }

    public DCRModel(String id, String tppName, String requestHeader, String request, String clientId,
                    String softwareEnv, String applicationId, String softwareId, String specVersion,
                    String createdTimestamp, String status, String clidIssuedAt, String statusUpdateTimestamp) {
        this.id = id;
        this.tppName = tppName;
        this.requestHeader = requestHeader;
        this.request = request;
        this.clientId = clientId;
        this.softwareEnv = softwareEnv;
        this.applicationId = applicationId;
        this.softwareId = softwareId;
        this.specVersion = specVersion;
        this.createdTimestamp = createdTimestamp;
        this.status = status;
        this.clidIssuedAt = clidIssuedAt;
        this.statusUpdateTimestamp = statusUpdateTimestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTppName() {
        return tppName;
    }

    public void setTppName(String tppName) {
        this.tppName = tppName;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSoftwareEnv() {
        return softwareEnv;
    }

    public void setSoftwareEnv(String softwareEnv) {
        this.softwareEnv = softwareEnv;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(String softwareId) {
        this.softwareId = softwareId;
    }

    public String getSpecVersion() {
        return specVersion;
    }

    public void setSpecVersion(String specVersion) {
        this.specVersion = specVersion;
    }

    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClidIssuedAt() {
        return clidIssuedAt;
    }

    public void setClidIssuedAt(String clidIssuedAt) {
        this.clidIssuedAt = clidIssuedAt;
    }

    public String getStatusUpdateTimestamp() {
        return statusUpdateTimestamp;
    }

    public void setStatusUpdateTimestamp(String statusUpdateTimestamp) {
        this.statusUpdateTimestamp = statusUpdateTimestamp;
    }
}
