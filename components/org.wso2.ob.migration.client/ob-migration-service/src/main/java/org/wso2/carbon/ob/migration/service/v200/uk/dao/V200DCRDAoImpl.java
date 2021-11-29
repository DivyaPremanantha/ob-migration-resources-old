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
package org.wso2.carbon.ob.migration.service.v200.uk.dao;

import com.wso2.openbanking.accelerator.common.exception.OpenBankingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.ob.migration.service.v200.uk.model.DCRModel;
import org.wso2.carbon.ob.migration.service.v200.uk.queries.DCRSQLStatements;
import org.wso2.carbon.ob.migration.service.v200.uk.util.ConsentDaoUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of V200DCRDao class
 */
public class V200DCRDAoImpl implements V200DCRDao {

    private static Log log = LogFactory.getLog(V200DCRDAoImpl.class);
    protected DCRSQLStatements sqlStatements;


    public V200DCRDAoImpl(DCRSQLStatements sqlStatements) {

        this.sqlStatements = sqlStatements;
    }

    @Override
    public List<DCRModel> getDCRDetails(Connection connection) throws OpenBankingException {
        String sqlStatements = this.sqlStatements.getDCRDetails();
        List<DCRModel> dcrModels = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatements)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    dcrModels.add(ConsentDaoUtil.mapToDCRModel(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving consent initiations" + e);
            throw new OpenBankingException("Error occurred while retrieving consent initiations", e);
        }

        return dcrModels;
    }
}
