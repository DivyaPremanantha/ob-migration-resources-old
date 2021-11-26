package org.wso2.carbon.ob.migration.service.v200.uk.dao;

import com.wso2.openbanking.accelerator.common.exception.OpenBankingException;
import org.wso2.carbon.ob.migration.service.v200.uk.model.DCRModel;

import java.sql.Connection;
import java.util.List;

public interface V200DCRDao {

    List<DCRModel> getDCRDetails(Connection connection) throws OpenBankingException;
    List<DCRModel> updateSPMetadata(Connection connection) throws OpenBankingException;

}
