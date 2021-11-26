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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class to generate a file based report.
 */
public class ReportUtil {

    private static final Logger log = LoggerFactory.getLogger(ReportUtil.class);
    private static final int MAX_LENGTH = 10000000;

    private String fileName;
    private String filePath;
    private StringBuilder builder = new StringBuilder();

    public ReportUtil(String filePath) {

        this.filePath = filePath;
    }

    public void writeMessage(String message) {

        writeMessage(message, true);
    }

    public void writeMessage(String message, boolean addNewlineAtEnd) {

        builder.append(message);
        if (addNewlineAtEnd) {
            builder.append("\n");
        }

        if (builder.length() > MAX_LENGTH) {
            Thread thread = new Thread(() -> {
                try {
                    commit();
                    log.info("Report file with name {} created inside {}.", fileName, filePath);
                } catch (IOException e) {
                    log.error("Error while writing the report file.", e);
                }
            });
            thread.start();
        }
    }

    public synchronized void commit() throws IOException {

        commit(false);
    }

    public synchronized void commit(boolean newFile) throws IOException {

        Path path;
        if (fileName != null) {
            path = Paths.get(filePath, fileName);
        } else {
            path = Paths.get(filePath);
        }

        if (Files.isDirectory(path) || Files.notExists(path) || newFile) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
            fileName = sdf.format(new Date()) + ".txt";
            path = Paths.get(filePath, fileName);
            Files.createFile(path);
        }

        Files.write(path, builder.toString().getBytes(), StandardOpenOption.APPEND);
        builder = new StringBuilder();
    }
}
