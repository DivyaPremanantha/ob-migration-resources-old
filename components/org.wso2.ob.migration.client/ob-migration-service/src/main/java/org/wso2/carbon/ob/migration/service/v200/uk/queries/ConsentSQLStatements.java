package org.wso2.carbon.ob.migration.service.v200.uk.queries;

public abstract class ConsentSQLStatements {

    public abstract String getConsentInitiations();

    public abstract String getConsentBindingsByConsentId();

    public abstract String getConsentRevsByConsentId();

    public abstract String getConsentRevHistoryByConsentId();
}
