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
    //todo verify 'multi-authorization' and 're-authorization' wordings
    public static final String AUTH_TYPE_MULTI_AUTHORIZATION = "multi-authorization";
    public static final String AUTH_TYPE_REAUTHORIZATION = "re-authorization";

    public static final String AUTH_STATUS_CREATED = "created";
    public static final String AUTH_STATUS_AUTHORIZED = "authorized";

    // Audit reasons
    public static final String REASON_CREATE_CONSENT = "Create consent";
    public static final String REASON_REVOKE_CONSENT = "Revoke the consent";
    public static final String REASON_BIND_CONSENT = "Bind user accounts to consent";
    public static final String REASON_RECEIVE_SUBMISSION_REQUEST_FOR_CONSENT =
            "Receive submission request for the consent";

    // Attribute names
    //todo verify 'SPEC_VERSION' wordings
    public static final String SPEC_VERSION = "SPEC_VERSION";
    public static final String FILE_UPLOAD_IDEMPOTENCY_KEY = "FileUploadIdempotencyKey";
    public static final String IDEMPOTENT_KEY = "IdempotencyKey";
    public static final String CONSENT_EXPIRY_TIME_ATTRIBUTE = "ExpirationDateTime";

    public static final String NOT_APPLICABLE = "n/a";
    public static final String CONSENT_MAPPING_STATUS_ACTIVE = "active";
    public static final String CONSENT_MAPPING_STATUS_INACTIVE = "inactive";

}
