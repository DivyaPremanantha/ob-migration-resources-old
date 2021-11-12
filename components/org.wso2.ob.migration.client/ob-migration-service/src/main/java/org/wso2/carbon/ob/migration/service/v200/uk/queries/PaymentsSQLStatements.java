package org.wso2.carbon.ob.migration.service.v200.uk.queries;

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
