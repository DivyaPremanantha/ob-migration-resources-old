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
package org.wso2.carbon.ob.migration.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.identity.core.migrate.MigrationClientException;
import org.wso2.carbon.ob.migration.config.Config;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Util class.
 */
public class Utility {

    private static final Logger log = LoggerFactory.getLogger(Utility.class);

    public static String getMigrationResourceDirectoryPath() {

        Path path = Paths.get(System.getProperty(Constant.CARBON_HOME), Constant.MIGRATION_RESOURCE_HOME);
        return path.toString();
    }

    public static Config loadMigrationConfig(String configFilePath) throws MigrationClientException {

        Config config;
        Path path = Paths.get(configFilePath);
        if (Files.exists(path)) {
            try {
                Reader in = new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8);
                Yaml yaml = new Yaml();
                yaml.setBeanAccess(BeanAccess.FIELD);
                config = yaml.loadAs(in, Config.class);
                if (config == null) {
                    throw new MigrationClientException("Provider is not loaded correctly.");
                }
            } catch (IOException e) {
                String errorMessage =
                        "Error occurred while loading the " + Config.class + " yaml file, " + e.getMessage();
                log.error(errorMessage, e);
                throw new MigrationClientException(errorMessage, e);
            }
        } else {
            throw new MigrationClientException(Constant.MIGRATION_CONFIG_FILE_NAME + " file does not exist at: " +
                    configFilePath);
        }
        return config;
    }
}
