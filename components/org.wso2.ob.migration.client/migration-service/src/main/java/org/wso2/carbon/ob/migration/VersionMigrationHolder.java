/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
