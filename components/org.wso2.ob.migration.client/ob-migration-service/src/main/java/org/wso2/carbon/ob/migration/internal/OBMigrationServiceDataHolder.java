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
package org.wso2.carbon.ob.migration.internal;

import org.wso2.carbon.base.api.ServerConfigurationService;
import org.wso2.carbon.identity.claim.metadata.mgt.ClaimMetadataManagementService;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.service.TenantRegistryLoader;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Migration Service Data Holder.
 */
public class OBMigrationServiceDataHolder {

    //Registry Service which is used to get registry data.
    private static RegistryService registryService;
    private static ClaimMetadataManagementService claimMetadataManagementService;

    //Realm Service which is used to get tenant data.
    private static RealmService realmService;

    private static ServerConfigurationService serverConfigurationService;

    //Tenant registry loader which is used to load tenant registry
    private static TenantRegistryLoader tenantRegLoader;

    private static String identityOracleUser;
    private static String umOracleUser;

    /**
     * Method to get RegistryService.
     *
     * @return registryService.
     */
    public static RegistryService getRegistryService() {

        return registryService;
    }

    /**
     * Method to set registry RegistryService.
     *
     * @param service registryService.
     */
    public static void setRegistryService(RegistryService service) {

        registryService = service;
    }

    /**
     * This method used to get RealmService.
     *
     * @return RealmService.
     */
    public static RealmService getRealmService() {

        return realmService;
    }

    /**
     * Method to set registry RealmService.
     *
     * @param service RealmService.
     */
    public static void setRealmService(RealmService service) {

        realmService = service;
    }

    /**
     * This method used to get TenantRegistryLoader.
     *
     * @return tenantRegLoader  Tenant registry loader for load tenant registry
     */
    public static TenantRegistryLoader getTenantRegLoader() {

        return tenantRegLoader;
    }

    /**
     * This method used to set TenantRegistryLoader.
     *
     * @param service Tenant registry loader for load tenant registry
     */
    public static void setTenantRegLoader(TenantRegistryLoader service) {

        tenantRegLoader = service;
    }

    /**
     * This method is used to get the user when the database is oracle.
     *
     * @return oracleUser user of the oracle database
     */
    public static String getIdentityOracleUser() {

        return identityOracleUser;
    }

    /**
     * This method is used to set the user when the user when the database is oracle.
     *
     * @param identityOracleUser
     */
    public static void setIdentityOracleUser(String identityOracleUser) {

        OBMigrationServiceDataHolder.identityOracleUser = identityOracleUser;
    }

    public static String getUmOracleUser() {

        return umOracleUser;
    }

    public static void setUmOracleUser(String umOracleUser) {

        OBMigrationServiceDataHolder.umOracleUser = umOracleUser;
    }

    public static ClaimMetadataManagementService getClaimMetadataManagementService() {

        return claimMetadataManagementService;
    }

    public static void setClaimMetadataManagementService(
            ClaimMetadataManagementService claimMetadataManagementService) {

        OBMigrationServiceDataHolder.claimMetadataManagementService = claimMetadataManagementService;
    }

    public static ServerConfigurationService getServerConfigurationService() {

        return serverConfigurationService;
    }

    public static void setServerConfigurationService(ServerConfigurationService serverConfigurationService) {

        OBMigrationServiceDataHolder.serverConfigurationService = serverConfigurationService;
    }
}
