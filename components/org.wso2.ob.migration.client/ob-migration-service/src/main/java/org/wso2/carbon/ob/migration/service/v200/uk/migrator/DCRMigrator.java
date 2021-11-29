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
package org.wso2.carbon.ob.migration.service.v200.uk.migrator;

import com.google.gson.Gson;
import com.wso2.openbanking.accelerator.common.exception.OpenBankingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.model.ServiceProviderProperty;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.application.mgt.ApplicationMgtSystemConfig;
import org.wso2.carbon.identity.application.mgt.dao.ApplicationDAO;
import org.wso2.carbon.identity.core.migrate.MigrationClientException;
import org.wso2.carbon.ob.migration.service.Migrator;
import org.wso2.carbon.ob.migration.service.v200.uk.dao.V200DCRDao;
import org.wso2.carbon.ob.migration.service.v200.uk.dao.V200DCRDaoInitializer;
import org.wso2.carbon.ob.migration.service.v200.uk.model.DCRModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * DCRMigrator class to migrate DCR related data.
 */
public class DCRMigrator extends Migrator {

    private static final Logger log = LoggerFactory.getLogger(DCRMigrator.class);
    private static Gson gson = new Gson();

    @Override
    public void dryRun() throws MigrationClientException {

        log.info("Dry run");
    }

    @Override
    public void migrate() throws MigrationClientException {

        try (Connection connection = getDataSource().getConnection()) {
            connection.setAutoCommit(false);
            V200DCRDao v200DCRDao = V200DCRDaoInitializer.initializeDCRDao(connection);
            migrateDCR(connection, v200DCRDao);

        } catch (SQLException | OpenBankingException e) {
            throw new MigrationClientException("Failed to migrate DCR.", e);
        }
    }

    private void migrateDCR(Connection connection, V200DCRDao v200DCRDao)
            throws MigrationClientException, OpenBankingException {
        List<DCRModel> dcrModels = v200DCRDao.getDCRDetails(connection);
        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        ApplicationManagementService applicationManagementService = ApplicationManagementService.getInstance();

        for (DCRModel dcrModel : dcrModels) {
            try {
                ServiceProvider serviceProvider = applicationManagementService
                        .getServiceProviderByClientId(dcrModel.getClientId(), "oauth2", tenantDomain);
                Map<String, Object> requestAttributes =
                        (Map<String, Object>) gson.fromJson(dcrModel.getRequest(), Map.class);
                Map<String, Object> spProperties = new HashMap<>();
                boolean isSpPropertyModified = false;
                for (Map.Entry<String, Object> attribute : requestAttributes.entrySet()) {
                    boolean isSpPropertyAvailable = false;
                    if (serviceProvider.getSpProperties().length > 0) {
                        for (ServiceProviderProperty spProperty : serviceProvider.getSpProperties()) {
                            if (!(isSpPropertyModified)) {
                                modifySpProperty(spProperty, serviceProvider, tenantDomain);
                            }
                            if (spProperty.getName().equalsIgnoreCase(attribute.getKey())) {
                                isSpPropertyAvailable = true;
                                break;
                            }
                        }
                        if (!(isSpPropertyAvailable)) {
                            spProperties.put(attribute.getKey(), attribute.getValue());
                        }
                    }
                    isSpPropertyModified = true;
                }

                ServiceProviderProperty[] serviceProviderProperties = new ServiceProviderProperty[spProperties.size()];
                int index = 0;

                for (Map.Entry<String, Object> attribute : spProperties.entrySet()) {
                    ServiceProviderProperty serviceProviderProperty = new ServiceProviderProperty();
                    serviceProviderProperty.setName(attribute.getKey());
                    serviceProviderProperty.setValue(getAttributeValue(attribute.getValue()));
                    serviceProviderProperty.setDisplayName(attribute.getKey());
                    serviceProviderProperties[index] = serviceProviderProperty;
                    index++;
                }

                if (serviceProviderProperties.length > 0) {
                    serviceProvider.setSpProperties(serviceProviderProperties);
                    ApplicationDAO appDAO = ApplicationMgtSystemConfig.getInstance().getApplicationDAO();
                    appDAO.updateApplication(serviceProvider, tenantDomain);
                }

            } catch (IdentityApplicationManagementException e) {
                throw new MigrationClientException("exception", e);
            }
        }
    }

    private String getAttributeValue(Object value) {
        String alteredValue = "";
        if (value instanceof ArrayList<?>) {
            ArrayList<Object> list = (ArrayList<Object>) value;
            Object lastListElement = list.get(list.size() - 1);
            modifyArrayList(list);
            if (list.size() == 1) {
                alteredValue = list.get(0).toString().concat("#");
            } else {
                for (Object listElement : list) {
                    if (!lastListElement.equals(listElement)) {
                        alteredValue = alteredValue.concat(
                                listElement.toString().concat("#"));
                    } else {
                        alteredValue = alteredValue.concat(lastListElement.toString());
                    }
                }
            }
            return alteredValue;
        }
        return value.toString();
    }

    private void modifyArrayList(ArrayList<Object> value) {
        for (Object element : value) {
            if (element.toString().contains("{")) {
                (value).set((value).indexOf(element), gson.toJson(element));
            }
        }
    }

    private void modifySpProperty(ServiceProviderProperty property, ServiceProvider serviceProvider,
                                  String tenantDomain)
            throws IdentityApplicationManagementException {
        String name = property.getName();
        String displayName = property.getDisplayName();
        if (name.toLowerCase(Locale.ROOT).contains("_production") ||
                name.toLowerCase(Locale.ROOT).contains("_sandbox")) {
            property.setName(name.substring(0, name.lastIndexOf('_')));
            property.setDisplayName(displayName.substring(0, displayName.lastIndexOf('_')));
            ApplicationDAO appDAO = ApplicationMgtSystemConfig.getInstance().getApplicationDAO();
            appDAO.updateApplication(serviceProvider, tenantDomain);
        }
    }
}
