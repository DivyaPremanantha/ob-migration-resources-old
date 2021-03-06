
-- ACCOUNTS_RAW_DATA migration

ALTER TABLE ACCOUNTS_RAW_DATA DROP COLUMN TRA_DECISION;
ALTER TABLE ACCOUNTS_RAW_DATA DROP COLUMN EXEMPTION;
ALTER TABLE ACCOUNTS_RAW_DATA DROP COLUMN IS_FRAUD;
ALTER TABLE ACCOUNTS_RAW_DATA ADD API_SPEC_VERSION varchar(254);
ALTER TABLE ACCOUNTS_RAW_DATA ADD CONSENT_TYPE varchar(254);
ALTER TABLE ACCOUNTS_RAW_DATA ADD EXPIRATION_DATE_TIME varchar(254);
ALTER TABLE ACCOUNTS_RAW_DATA ADD MESSAGE_ID varchar(254);

UPDATE ACCOUNTS_RAW_DATA
SET API_SPEC_VERSION = ( SELECT UARD.API_SPEC_VERSION
                         FROM ACCOUNTS_RAW_DATA ARD
                                  INNER JOIN UK_ADDITIONAL_RAW_DATA UARD
                                             ON UARD.ID = ARD.ID
                         WHERE ARD.ID=ACCOUNTS_RAW_DATA.ID );

UPDATE ACCOUNTS_RAW_DATA
SET CONSENT_TYPE = ( SELECT UARD.CONSENT_TYPE
                     FROM ACCOUNTS_RAW_DATA ARD
                              INNER JOIN UK_ADDITIONAL_RAW_DATA UARD
                                         ON UARD.ID = ARD.ID
                     WHERE ARD.ID=ACCOUNTS_RAW_DATA.ID );

UPDATE ACCOUNTS_RAW_DATA
SET EXPIRATION_DATE_TIME = ( SELECT UARD.EXPIRATION_DATE_TIME
                             FROM ACCOUNTS_RAW_DATA ARD
                                      INNER JOIN UK_ADDITIONAL_RAW_DATA UARD
                                                 ON UARD.ID = ARD.ID
                             WHERE ARD.ID=ACCOUNTS_RAW_DATA.ID );

UPDATE ACCOUNTS_RAW_DATA
SET MESSAGE_ID = ( SELECT AIRD.ID
                   FROM ACCOUNTS_RAW_DATA ARD
                            INNER JOIN API_INVOCATION_RAW_DATA AIRD
                                       ON AIRD.ID = ARD.ID
                   WHERE ARD.ID=ACCOUNTS_RAW_DATA.ID );

UPDATE ACCOUNTS_RAW_DATA SET ID = lower(NEWID());

-- FUNDS_CONFIRMATION_RAW_DATA migration

ALTER TABLE FUNDS_CONFIRMATION_RAW_DATA DROP COLUMN IS_FRAUD;
ALTER TABLE FUNDS_CONFIRMATION_RAW_DATA ADD API_SPEC_VERSION varchar(254);
ALTER TABLE FUNDS_CONFIRMATION_RAW_DATA ADD CONSENT_TYPE varchar(254);
ALTER TABLE FUNDS_CONFIRMATION_RAW_DATA ADD EXPIRATION_DATE_TIME varchar(254);
ALTER TABLE FUNDS_CONFIRMATION_RAW_DATA ADD MESSAGE_ID varchar(254);

UPDATE FUNDS_CONFIRMATION_RAW_DATA
SET API_SPEC_VERSION = ( SELECT UARD.API_SPEC_VERSION
                         FROM FUNDS_CONFIRMATION_RAW_DATA FCRD
                                  INNER JOIN UK_ADDITIONAL_RAW_DATA UARD
                                             ON UARD.ID = FCRD.ID
                         WHERE FCRD.ID=FUNDS_CONFIRMATION_RAW_DATA.ID );

UPDATE FUNDS_CONFIRMATION_RAW_DATA
SET CONSENT_TYPE = ( SELECT UARD.CONSENT_TYPE
                     FROM FUNDS_CONFIRMATION_RAW_DATA FCRD
                              INNER JOIN UK_ADDITIONAL_RAW_DATA UARD
                                         ON UARD.ID = FCRD.ID
                     WHERE FCRD.ID=FUNDS_CONFIRMATION_RAW_DATA.ID );

UPDATE FUNDS_CONFIRMATION_RAW_DATA
SET EXPIRATION_DATE_TIME = ( SELECT UARD.EXPIRATION_DATE_TIME
                             FROM FUNDS_CONFIRMATION_RAW_DATA FCRD
                                      INNER JOIN UK_ADDITIONAL_RAW_DATA UARD
                                                 ON UARD.ID = FCRD.ID
                             WHERE FCRD.ID=FUNDS_CONFIRMATION_RAW_DATA.ID );

UPDATE FUNDS_CONFIRMATION_RAW_DATA
SET MESSAGE_ID = ( SELECT AIRD.ID
                   FROM FUNDS_CONFIRMATION_RAW_DATA FCRD
                            INNER JOIN API_INVOCATION_RAW_DATA AIRD
                                       ON AIRD.ID = FCRD.ID
                   WHERE FCRD.ID=FUNDS_CONFIRMATION_RAW_DATA.ID );

UPDATE FUNDS_CONFIRMATION_RAW_DATA SET ID = lower(NEWID());


-- PAYMENTS_RAW_DATA migration

CREATE TABLE PAYMENTS_RAW_DATA_TEMP (
                                        ID varchar(254) NOT NULL,
                                        CONSENT_ID varchar(254) ,
                                        USER_ID varchar(254) ,
                                        TPP_ID varchar(254) ,
                                        CLIENT_ID varchar(254) ,
                                        PAYMENT_TYPE varchar(254) ,
                                        TIMESTAMP bigint NOT NULL,
                                        API_SPEC_VERSION varchar(254) ,
                                        CREDITOR_ACCOUNT_ID varchar(254) ,
                                        DEBTOR_ACCOUNT_ID varchar(254) ,
                                        MULTI_AUTHORISATION BIT NULL ,
                                        CURRENCY varchar(254) ,
                                        AMOUNT varchar(254) ,
                                        NO_OF_TRANSACTIONS varchar(254) ,
                                        LOCAL_INSTRUMENT varchar(254) ,
                                        PAYMENT_SUBMISSION_ID varchar(254) ,
                                        MULTI_AUTH_USERS_COUNT int NULL ,
                                        PAYMENT_REFUND varchar(254) ,
                                        CONSENT_TYPE varchar(254) ,
                                        DELIVERY_ADDRESS varchar(254) ,
                                        MESSAGE_ID varchar(254) ,
                                        PRIMARY KEY (ID,TIMESTAMP)
);

INSERT INTO PAYMENTS_RAW_DATA_TEMP
(ID, CONSENT_ID, USER_ID, TPP_ID, CLIENT_ID, PAYMENT_TYPE, TIMESTAMP, API_SPEC_VERSION, CREDITOR_ACCOUNT_ID,
 DEBTOR_ACCOUNT_ID, MULTI_AUTHORISATION, CURRENCY, AMOUNT, NO_OF_TRANSACTIONS, LOCAL_INSTRUMENT,
 PAYMENT_SUBMISSION_ID, MULTI_AUTH_USERS_COUNT, PAYMENT_REFUND, CONSENT_TYPE, DELIVERY_ADDRESS, MESSAGE_ID)
SELECT
    lower(NEWID()),
    CONSENT_ID, USER_ID, TPP_ID, CLIENT_ID, PAYMENT_TYPE, TIMESTAMP,
    (SELECT API_SPEC_VERSION from UK_ADDITIONAL_RAW_DATA UARD where UARD.ID=PRD.ID),
    CREDITOR_ACCOUNT_ID, DEBTOR_ACCOUNT_ID, MULTI_AUTHORISATION, CURRENCY, AMOUNT, NO_OF_TRANSACTIONS,
    (SELECT LOCAL_INSTRUMENT from UK_ADDITIONAL_RAW_DATA UARD where UARD.ID=PRD.ID),
    PAYMENT_SUBMISSION_ID, MULTI_AUTH_USERS_COUNT,
    (SELECT PAYMENT_REFUND from UK_ADDITIONAL_RAW_DATA UARD where UARD.ID=PRD.ID),
    (SELECT CONSENT_TYPE from UK_ADDITIONAL_RAW_DATA UARD where UARD.ID=PRD.ID),
    (SELECT deliveryAddress from TransactionDeliveryAddress TDA where TDA.consentId=PRD.CONSENT_ID),
    (SELECT ID from API_INVOCATION_RAW_DATA AIRD where AIRD.ID=PRD.ID)
FROM PAYMENTS_RAW_DATA PRD;

DROP TABLE IF EXISTS PAYMENTS_RAW_DATA;

sp_rename 'PAYMENTS_RAW_DATA_TEMP' , 'PAYMENTS_RAW_DATA';


-- API_LATENCY_RAW_DATA migration

CREATE TABLE API_LATENCY_RAW_DATA (
              ID varchar(254) ,
              MESSAGE_ID varchar(254) ,
              REQUEST_TIMESTAMP varchar(254) ,
              BACKEND_LATENCY bigint NULL ,
              REQUEST_MEDIATION_LATENCY bigint NULL ,
              RESPONSE_LATENCY bigint NULL ,
              RESPONSE_MEDIATION_LATENCY bigint NULL ,
              PRIMARY KEY (ID,MESSAGE_ID)
);

INSERT INTO API_LATENCY_RAW_DATA
(ID, MESSAGE_ID, REQUEST_TIMESTAMP, BACKEND_LATENCY, REQUEST_MEDIATION_LATENCY, RESPONSE_LATENCY,
 RESPONSE_MEDIATION_LATENCY)
SELECT
    lower(NEWID()),
    ID,
    CASE WHEN REQUEST_EXQ_START_TIME > 0
        THEN FORMAT(dateadd(S, REQUEST_EXQ_START_TIME/1000, '1970-01-01 00:00:00'),'yyyy-MM-ddTHH:mm:ss.fffZ')
        ELSE FORMAT(dateadd(S, TIMESTAMP, '1970-01-01 00:00:00'),'yyyy-MM-ddTHH:mm:ss.fffZ')
    END,
    BACKEND_LATENCY,
    ( BACKEND_REQ_START_TIME - REQUEST_EXQ_START_TIME ),
    CASE
        WHEN REQUEST_EXQ_START_TIME > 0 AND ( TIMESTAMP * 1000 - REQUEST_EXQ_START_TIME ) > 0
            THEN  ( TIMESTAMP * 1000 - REQUEST_EXQ_START_TIME )
        ELSE 0
        END,
    CASE
        WHEN BACKEND_REQ_END_TIME > 0 AND ( TIMESTAMP * 1000 - BACKEND_REQ_END_TIME ) > 0
            THEN  ( TIMESTAMP * 1000 - BACKEND_REQ_END_TIME )
        ELSE 0
        END
FROM   API_INVOCATION_RAW_DATA;


-- API_INVOCATION_RAW_DATA migration

ALTER TABLE API_INVOCATION_RAW_DATA DROP COLUMN REQUEST_EXQ_START_TIME;
ALTER TABLE API_INVOCATION_RAW_DATA DROP COLUMN BACKEND_REQ_START_TIME;
ALTER TABLE API_INVOCATION_RAW_DATA DROP COLUMN BACKEND_REQ_END_TIME;
ALTER TABLE API_INVOCATION_RAW_DATA DROP COLUMN BACKEND_LATENCY;
ALTER TABLE API_INVOCATION_RAW_DATA DROP COLUMN AUTHORISATION_STATUS;
ALTER TABLE API_INVOCATION_RAW_DATA DROP COLUMN STATUS_MESSAGE;

ALTER TABLE API_INVOCATION_RAW_DATA ADD API_SPEC_VERSION varchar(254);

ALTER TABLE API_INVOCATION_RAW_DATA ADD TIMESTAMP_TEMP bigint NULL;
UPDATE API_INVOCATION_RAW_DATA SET TIMESTAMP_TEMP = TIMESTAMP;
ALTER TABLE API_INVOCATION_RAW_DATA DROP COLUMN TIMESTAMP;
sp_rename 'API_INVOCATION_RAW_DATA.TIMESTAMP_TEMP', 'TIMESTAMP', 'COLUMN';

ALTER TABLE API_INVOCATION_RAW_DATA ADD MESSAGE_ID varchar(254);
sp_rename 'API_INVOCATION_RAW_DATA.TPP_ID', 'CONSUMER_ID', 'COLUMN';

UPDATE API_INVOCATION_RAW_DATA SET MESSAGE_ID = ID;

UPDATE API_INVOCATION_RAW_DATA
SET API_SPEC_VERSION = ( SELECT UARD.API_SPEC_VERSION
                         FROM API_INVOCATION_RAW_DATA AIRD
                                  INNER JOIN UK_ADDITIONAL_RAW_DATA UARD
                                             ON UARD.ID = AIRD.ID
                         WHERE AIRD.ID=API_INVOCATION_RAW_DATA.ID );

UPDATE API_INVOCATION_RAW_DATA SET ID = lower(NEWID());

--
--
-- -- AUTHENTICATION_RAW_DATA migration
--
-- ALTER TABLE AUTHENTICATION_RAW_DATA DROP COLUMN USER_AGENT;
-- ALTER TABLE AUTHENTICATION_RAW_DATA DROP COLUMN IS_SCA_APPLIED;
--
-- ALTER TABLE AUTHENTICATION_RAW_DATA ADD COLUMN `PSU_CHANNEL` varchar(254) DEFAULT NULL;
--
-- UPDATE AUTHENTICATION_RAW_DATA ARD
--     INNER JOIN UK_ADDITIONAL_RAW_DATA UARD ON UARD.ID=ARD.ID
--     SET ARD.PSU_CHANNEL = UARD.PSU_CHANNEL;
--
--
-- -- AUTHORISATION_RAW_DATA migration
--
-- ALTER TABLE AUTHORISATION_RAW_DATA ADD COLUMN `PSU_INTERACTION_TIME` bigint(20) DEFAULT NULL;
--
-- UPDATE AUTHORISATION_RAW_DATA ARD
--     INNER JOIN UK_ADDITIONAL_RAW_DATA UARD ON UARD.ID=ARD.ID
--     SET ARD.PSU_INTERACTION_TIME = UARD.PSU_INTERACTION_TIME;
--
-- -- Removing other additional tables.
--
DROP TABLE IF EXISTS CONSENT_RAW_DATA;
DROP TABLE IF EXISTS TransactionDeliveryAddress;
DROP TABLE IF EXISTS UK_ADDITIONAL_RAW_DATA;
