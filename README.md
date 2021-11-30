# How to use the client

* Create the required tables by following this [link](https://ob.docs.wso2.com/en/latest/install-and-setup/setting-up-databases/#creating-database-tables).
* Build the project and copy the jar file in the target to the <OB-ACCELERATOR-HOME>/repository/components/dropins 
  directory.
* Copy the ob-migration-resources folder to <OB-ACCELERATOR-HOME>
* Make sure the migration is enabled in the migration-config.yaml
* Start the server with -DobMigrationSpec=<OB-SPEC> (Mention the openbanking specification UK, BG or AU)