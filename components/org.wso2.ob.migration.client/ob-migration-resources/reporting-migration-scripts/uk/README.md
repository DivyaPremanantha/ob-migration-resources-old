Data Migration UK - Reporting
=============================

1. Shutdown WSO2-OBBI-2.0.0 if it is running.

2. Backup your `openbank_ob_reporting_statsdb` 
   `openbank_ob_reporting_summarizeddb` Databases of your OBBI 2.0.0 instance.

3. Execute relevant sql script in `components/org.wso2.ob.migration.client/ob-migration-resources/reporting-migration-scripts/uk`
   ( Ex: `reporting-<DB_TYPE>-<FROM_VERSION>_to_<TO_VERSION>.sql`) directory against your `openbank_ob_reporting_statsdb` 
   Database. This will migrate tables and data for UK reporting.