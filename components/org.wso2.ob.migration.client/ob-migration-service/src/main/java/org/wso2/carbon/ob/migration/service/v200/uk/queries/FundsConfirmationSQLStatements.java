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
package org.wso2.carbon.ob.migration.service.v200.uk.queries;

/**
 * SQL statements for CoF migration
 */
public class FundsConfirmationSQLStatements extends ConsentSQLStatements {

    public String getConsentInitiations() {

        return "SELECT * FROM UK_COF_INITIATION";
    }

    public String getConsentBindingsByConsentId() {

        return "SELECT * FROM UK_COF_CONSENT_BINDING WHERE CONSENT_ID = ?";
    }

    public String getConsentRevsByConsentId() {

        return "SELECT * FROM UK_CONSENT_REV WHERE CONSENT_ID = ?";
    }

    public String getConsentRevHistoryByConsentId() {

        return "SELECT * FROM UK_COF_CONSENT_REV_HISTORY WHERE CONSENT_ID = ?";
    }

    // Consent file will be skipped for cof
    @Override
    public String getStoreConsentFileByConsentId() {

        return "";
    }

    // Consent file will be skipped for cof
    @Override
    public String getFileUploadIdempotencyKeyByConsentId() {

        return "";
    }
}
