package com.kpn.bam.opib.adapter.fusion;

import static com.kpn.bam.opib.utils.DBUtils.getQueryInputBatches;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpn.bam.opib.connector.ConnectorException;
import com.kpn.bam.opib.connector.OPIBAdapter;
import com.kpn.bam.opib.utils.DBUtils;
import com.kpn.opib.bam.model.FusionOrderDetails;
import com.kpn.opib.bam.model.FusionStatus;
import com.kpn.opib.bam.model.OPIBOrderDetails;

public class FusionAdapter implements OPIBAdapter {

	private static final String FMW_STATUS_QUERY = "SELECT ORDER_ID,STATUS,JMSTIMESTAMP from JMSUSER11g.AIA_SIEBEL_SEQ_TBL o1 WHERE JMSTIMESTAMP = "
			+ " (SELECT MAX(JMSTIMESTAMP) FROM JMSUSER11g.AIA_SIEBEL_SEQ_TBL o2 WHERE o1.order_id = o2.order_id) "
			+ "and ORDER_ID in( ? ) GROUP BY order_id,JMSTIMESTAMP,STATUS";

	private static final String FMW_STATUS_IN_PROGRESS = "IN PROGRESS";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String FUSION_DS_JNDI = "java:/FUSION_DS";
	
	@Override
	public List<OPIBOrderDetails> getOrders(List<String> orderIds) {

		List<OPIBOrderDetails> orderListWithFusionData = new ArrayList<>();
		List<String> queryInputList = getQueryInputBatches(orderIds);

		int batchCount = 0;

		for (String queryParameter : queryInputList) {
			batchCount++;
			String query = FMW_STATUS_QUERY.replace("?", queryParameter);

			try (Connection conn = DBUtils.getConnectionByJNDI(FUSION_DS_JNDI);
					PreparedStatement prepStmt = conn.prepareStatement(query)) {
				logger.debug("---------Executing Batch Number " + batchCount + "------------");
				logger.debug("Fusion Query " + query);

				ResultSet rs = prepStmt.executeQuery();

				populateOrderFromResultSet(orderListWithFusionData, rs);
				rs.close();
			} catch (SQLException | NamingException e) {
				throw new ConnectorException("Error while fetch OneCRM delta orders", e);
			}
		}

		return orderListWithFusionData;
	}

	private void populateOrderFromResultSet(List<OPIBOrderDetails> orderListWithFusionData, ResultSet rs)
			throws SQLException {

		while (rs.next()) {
			try
			{
				OPIBOrderDetails order = new OPIBOrderDetails();
				order.setSiebelOrderId(rs.getString("ORDER_ID"));
				FusionOrderDetails fusionOrderDetails = new FusionOrderDetails();
				fusionOrderDetails.setFusionStatus(FusionStatus.valueOf(
						rs.getString("STATUS").equals(FMW_STATUS_IN_PROGRESS) ? "IN_PROGRESS" : rs.getString("STATUS")));
				order.setFusionOrderDetails(fusionOrderDetails);
				orderListWithFusionData.add(order);
			}
			catch(java.lang.IllegalArgumentException ie)
			{
				logger.error("Unknown status type " + ie);
			}
			
		}
	}

}
