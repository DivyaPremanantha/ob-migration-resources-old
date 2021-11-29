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
package org.wso2.carbon.ob.migration.config;

import org.wso2.carbon.ob.migration.util.Specification;

import java.util.Properties;

/**
 * Migrator config bean.
 */
public class MigratorConfig {

    private String name;
    private Specification spec;
    private int order;
    private Properties parameters = new Properties();

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public int getOrder() {

        return order;
    }

    public void setOrder(int order) {

        this.order = order;
    }

    public Properties getParameters() {

        return parameters;
    }

    public void setParameters(Properties parameters) {

        this.parameters = parameters;
    }

    public String getParameterValue(String parameterKey) {

        return getParameters().getProperty(parameterKey);
    }

    public Specification getSpec() {
        return spec;
    }

    public void setSpec(Specification spec) {
        this.spec = spec;
    }

    /**
     * Comparator implementation for Migration config.
     */
    public static class Comparator implements java.util.Comparator<MigratorConfig> {

        @Override
        public int compare(MigratorConfig migratorConfigOne, MigratorConfig migratorConfigTwo) {

            if (migratorConfigOne.getOrder() > migratorConfigTwo.getOrder()) {
                return 1;
            } else if (migratorConfigOne.getOrder() > migratorConfigTwo.getOrder()) {
                return -1;
            }
            return 0;
        }
    }
}
