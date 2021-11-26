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
package org.wso2.carbon.ob.migration.service.v200.uk.util;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.ob.migration.service.v200.uk.constants.UKCommonConstants;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * Utils class for UK migration
 */
public class UKUtils {

    private static final Logger log = LoggerFactory.getLogger(UKUtils.class);

    public static long getExpirationTimeFromReceipt(String request) {

        JSONObject receiptJSON = null;
        try {
            receiptJSON = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).
                    parse(request);
            JSONObject data = null;
            if (receiptJSON.containsKey("Data")) {
                data = (JSONObject) receiptJSON.get("Data");
            }
            if (data != null && data.containsKey(UKCommonConstants.CONSENT_EXPIRY_TIME_ATTRIBUTE)) {
                String expireTime = data.get(UKCommonConstants.CONSENT_EXPIRY_TIME_ATTRIBUTE).toString();
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(expireTime);
                // Retrieve the UTC timestamp in long from expiry time.
                return Instant.from(zonedDateTime).getEpochSecond();
            }
        } catch (ParseException e) {
            log.error("Invalid consent receipt data to fetch expiration time : " + request);
            return 0;
        }
        log.error("Invalid consent receipt data to fetch expiration time : " + request);
        return 0;
    }
}
