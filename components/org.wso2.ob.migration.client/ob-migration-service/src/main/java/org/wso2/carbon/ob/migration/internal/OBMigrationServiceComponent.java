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

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.base.api.ServerConfigurationService;
import org.wso2.carbon.core.ServerStartupObserver;
import org.wso2.carbon.identity.core.migrate.MigrationClient;
import org.wso2.carbon.ob.migration.MigrationClientImpl;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * @scr.component name="org.wso2.carbon.ob.migration.client" immediate="true"
 * @scr.reference name="realm.service"
 * interface="org.wso2.carbon.user.core.service.RealmService" cardinality="1..1"
 * policy="dynamic" bind="setRealmService" unbind="unsetRealmService"
 * @scr.reference name="server.configuration.service" interface="org.wso2.carbon.base.api.ServerConfigurationService"
 * cardinality="1..1" policy="dynamic"  bind="setServerConfigurationService" unbind="unsetServerConfigurationService"
 * @scr.reference name="registry.service" interface="org.wso2.carbon.registry.core.service.RegistryService"
 * cardinality="1..1" policy="dynamic"  bind="setRegistryService" unbind="unsetRegistryService"
 */
public class OBMigrationServiceComponent {

    private static final Logger log = LoggerFactory.getLogger(OBMigrationServiceComponent.class);

    /**
     * Method to activate bundle.
     *
     * @param context OSGi component context.
     */
    protected void activate(ComponentContext context) {

        try {
            OBMigrationServiceDataHolder.setIdentityOracleUser(System.getProperty("identityOracleUser"));
            OBMigrationServiceDataHolder.setUmOracleUser(System.getProperty("umOracleUser"));
            context.getBundleContext().registerService(ServerStartupObserver.class.getName(), new
                    MigrationClientImpl(), null);
            if (log.isDebugEnabled()) {
                log.debug("WSO2 IS migration bundle is activated");
            }
        } catch (Throwable e) {
            log.error("Error while initiating Config component", e);
        }
    }

    /**
     * Method to deactivate bundle.
     *
     * @param context OSGi component context.
     */
    protected void deactivate(ComponentContext context) {

        if (log.isDebugEnabled()) {
            log.debug("WSO2 IS migration bundle is deactivated");
        }
    }

    /**
     * Method to set realm service.
     *
     * @param realmService service to get tenant data.
     */
    protected void setRealmService(RealmService realmService) {

        if (log.isDebugEnabled()) {
            log.debug("Setting RealmService to WSO2 IS Config component");
        }
        OBMigrationServiceDataHolder.setRealmService(realmService);
    }

    /**
     * Method to unset realm service.
     *
     * @param realmService service to get tenant data.
     */
    protected void unsetRealmService(RealmService realmService) {

        if (log.isDebugEnabled()) {
            log.debug("Unsetting RealmService from WSO2 IS Config component");
        }
        OBMigrationServiceDataHolder.setRealmService(null);
    }

    protected void setServerConfigurationService(ServerConfigurationService serverConfigurationService) {

        OBMigrationServiceDataHolder.setServerConfigurationService(serverConfigurationService);
    }

    protected void unsetServerConfigurationService(ServerConfigurationService serverConfigurationService) {

        OBMigrationServiceDataHolder.setServerConfigurationService(null);
    }

    protected void setRegistryService(RegistryService registryService) {

        OBMigrationServiceDataHolder.setRegistryService(registryService);
    }

    protected void unsetRegistryService(RegistryService registryService) {

        OBMigrationServiceDataHolder.setRegistryService(null);
    }
}
