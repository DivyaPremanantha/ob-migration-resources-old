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
package org.wso2.carbon.ob.migration;

import org.wso2.carbon.ob.migration.service.v200.V200Migration;

import java.util.ArrayList;
import java.util.List;

/**
 * Holder class to hold version migrator objects.
 */
public class VersionMigrationHolder {

    private static VersionMigrationHolder versionMigrationHolder = new VersionMigrationHolder();
    private List<VersionMigration> versionMigrationList = new ArrayList<>();

    private VersionMigrationHolder() {

        versionMigrationList.add(new V200Migration());
    }

    public static VersionMigrationHolder getInstance() {

        return VersionMigrationHolder.versionMigrationHolder;
    }

    public List<VersionMigration> getVersionMigrationList() {

        return versionMigrationList;
    }
}
