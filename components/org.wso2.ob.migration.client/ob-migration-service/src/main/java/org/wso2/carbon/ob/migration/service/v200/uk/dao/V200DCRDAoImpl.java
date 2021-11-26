package org.wso2.carbon.ob.migration.service.v200.uk.dao;

import com.wso2.openbanking.accelerator.common.exception.OpenBankingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.ob.migration.service.v200.uk.model.DCRModel;
import org.wso2.carbon.ob.migration.service.v200.uk.model.UKConsentInitiationModel;
import org.wso2.carbon.ob.migration.service.v200.uk.queries.DCRSQLStatements;
import org.wso2.carbon.ob.migration.service.v200.uk.util.ConsentDaoUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<DCRModel> updateSPMetadata(Connection connection) throws OpenBankingException {
        return null;
    }
}
