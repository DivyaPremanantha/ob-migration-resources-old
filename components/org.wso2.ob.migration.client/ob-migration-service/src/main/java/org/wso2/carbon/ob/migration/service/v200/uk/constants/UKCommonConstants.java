package org.wso2.carbon.ob.migration.service.v200.uk.constants;

/**
 * This class includes the common constants which is specific for UK
 */
public class UKCommonConstants {

    // OB2 statuses
    public static final String V2_AUTHORISED = "Authorised";
    public static final String V2_REVOKED = "Revoked";
    public static final String V2_AWAITING_AUTHORISATION = "AwaitingAuthorisation";
    public static final String V2_REJECTED = "Rejected";
    public static final String V2_CONSUMED = "Consumed";

    // OB3 statuses
    public static final String V3_AUTHORISED = "authorized";
    public static final String V3_REVOKED = "revoked";
    public static final String V3_AWAITING_AUTHORISATION = "awaitingAuthorization";
    public static final String V3_REJECTED = "rejected";
    public static final String V3_CONSUMED = "consumed";

    public static final String ACCOUNTS = "accounts";
    public static final String PAYMENTS = "payments";
    public static final String FUNDS_CONFIRMATIONS = "fundsconfirmations";

    public static final String AUTH_TYPE_AUTHORIZATION = "authorization";
    public static final String AUTH_STATUS_CREATED = "created";

    public static final String REASON_CREATE_CONSENT = "Create consent";
    public static final String REASON_REVOKE_CONSENT = "Revoke the consent";
    public static final String REASON_BIND_CONSENT = "Bind user accounts to consent";
    public static final String REASON_RECEIVE_SUBMISSION_REQUEST_FOR_CONSENT =
            "Receive submission request for the consent";

    public static final String CONSENT_EXPIRY_TIME_ATTRIBUTE = "ExpirationDateTime";

}
