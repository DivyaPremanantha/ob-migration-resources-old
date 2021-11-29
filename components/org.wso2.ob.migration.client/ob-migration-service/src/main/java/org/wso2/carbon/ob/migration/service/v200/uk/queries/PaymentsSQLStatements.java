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
 * SQL statements for payments migration
 */
public class PaymentsSQLStatements extends ConsentSQLStatements {

    public String getConsentInitiations() {

        return "SELECT * FROM UK_TRANSACTION_INITIATION";
    }

    public String getConsentBindingsByConsentId() {

        return "SELECT * FROM UK_TRANSACTION_CONSENT_BINDING WHERE CONSENT_ID = ?";
    }

    // Consent revocation will be skipped for payments
    public String getConsentRevsByConsentId() {

        return "";
    }

    // Consent revocation will be skipped for payments
    public String getConsentRevHistoryByConsentId() {

        return "";
    }

    public String getStoreConsentFileByConsentId() {

        return "INSERT INTO OB_CONSENT_FILE (`CONSENT_ID`, `CONSENT_FILE`) " +
                "SELECT CONSENT_ID, PAYMENT_FILE AS PAYMENT_FILE FROM UK_TRANSACTION_FILE WHERE CONSENT_ID = ?";
    }

    public String getFileUploadIdempotencyKeyByConsentId() {

        return "SELECT CONSENT_ID, IDEMPOTENT_KEY FROM UK_TRANSACTION_FILE WHERE CONSENT_ID = ?";
    }
}
