package com.kpn.bam.opib.adapter.siebel;

import static com.kpn.bam.opib.utils.DBUtils.getConnectionByJNDI;
import static com.kpn.bam.opib.utils.DBUtils.getQueryInputBatches;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpn.bam.opib.connector.ConnectorException;
import com.kpn.bam.opib.connector.OPIBAdapter;
import com.kpn.bam.opib.utils.BatchConstants;
import com.kpn.opib.bam.model.OPIBOrderDetails;
import com.kpn.opib.bam.model.SiebelOrderDetails;

public class SiebelAdapter implements OPIBAdapter {

	
	
	private static final String DELTA_ORDERS_FETCH_QUERY = "SELECT * FROM  SIEBEL.S_ORDER WHERE X_KPN_ORDER_SOORT = 'Order' "
			+ "AND STATUS_CD NOT IN('Final','Other System','Abandoned','Pre Submission','Open','Cancelled')";
			
//	private static final String DELTA_ORDERS_FETCH_QUERY = "SELECT o.* , c.ou_num CKR_NUMBER , c.NAME ACCOUNT_NAME FROM  SIEBEL.S_ORDER o, SIEBEL.S_ORG_EXT c  WHERE o.X_KPN_ORDER_SOORT = 'Order' "
//			+ "AND o.STATUS_CD NOT IN('Final','Other System','Abandoned','Pre Submission','Open','Cancelled') and c.PAR_ROW_ID = o.ACCNT_ID";

	private static final String CREATED_DATE_CLAUSE = " AND o.CREATED >= ? ";

	private static final String SIEBEL_STATUS_QUERY = "SELECT A.ROW_ID ORDER_ID ,A.STATUS_CD STATUS FROM SIEBEL.S_ORDER A "
			+ " WHERE A.X_KPN_ORDER_SOORT = 'Order' AND  A.ROW_ID IN( ? )";
	
//	select a.row_id ORDER_ROWID, a.order_num ORDER_NUMBER,a.created,a.x_sub_type ORDER_TYPE,a.status_cd ORDER_STATUS, b.ou_num CKR_NUMBER ,b.NAME ACCOUNT_NAME
//	from SIEBEL.s_order a,SIEBEL.S_ORG_EXT b 
//	where b.PAR_ROW_ID = a.ACCNT_ID
//	and a.x_kpn_order_soort = 'Order'
//	AND a.row_id IN ('1-MJXZEKF'); 

	private static final Logger logger = LoggerFactory.getLogger(SiebelAdapter.class);

	private static final String SIEBEL_DS_JNDI = "java:/SIEBEL_DS";

	/**
	 * Fetches all open orders from OneCRM that are created on or after the
	 * specified input date
	 * 
	 * @param onOrAfterCreationDate
	 *            - date on or after which the order is created - Fetches all
	 *            open orders currently if this paramater is null
	 * @return List<OPIBOrderDetails> list of orders that are open and created
	 *         on or after input date.
	 * 
	 */
	@Override
	public List<OPIBOrderDetails> getOpenOrders(Date onOrAfterCreationDate) {

		logger.debug("Fetching open orders created on or after date _" + onOrAfterCreationDate);
		String openOrdersQuery = DELTA_ORDERS_FETCH_QUERY;
		List<OPIBOrderDetails> openOrders = new ArrayList<>();

		if (null != onOrAfterCreationDate) {
			openOrdersQuery += CREATED_DATE_CLAUSE;
		}

		ResultSet rs = null;

		try (Connection conn = getConnectionByJNDI(SIEBEL_DS_JNDI);
				PreparedStatement prepStmt = conn.prepareStatement(openOrdersQuery)) {

			if (null != onOrAfterCreationDate) {

				prepStmt.setDate(1, new java.sql.Date(onOrAfterCreationDate.getTime()));
			}

			rs = prepStmt.executeQuery();
			while (rs.next()) {

				OPIBOrderDetails order = SiebelOrderMapper.mapRow(rs, BatchConstants.OPIB_BAM_SYNC_JOB,
						BatchConstants.OPIB_BAM_SYNC_JOB);
				openOrders.add(order);
			}

			logger.debug("Count of Open Orders in OneCRM " + openOrders.size());
		} catch (SQLException | NamingException e) {
			throw new ConnectorException("Error while fetch OneCRM delta orders" + e);
		} finally {
			closeResultSet(rs);
		}
		return openOrders;

	}

	/**
	 * Method to get Siebel Status
	 * 
	 * @param orderIds
	 *            - List of Siebel Order IDs.
	 * @return List of {@link OPIBOrderDetails} objects with Siebel status
	 *         updated.
	 */
	@Override
	public List<OPIBOrderDetails> getOrders(List<String> orderIds) {

		List<OPIBOrderDetails> opibOrderDetailsList = new ArrayList<>();

		if (null != orderIds && !orderIds.isEmpty()) {
			List<String> orderIdList = getQueryInputBatches(orderIds);

			logger.debug("Number of batches " + orderIdList.size());

			int batchCount = 0;
			for (String queryParameter : orderIdList) {
				batchCount++;
				logger.debug("---------Batch Count " + batchCount + "------------");
				populateOrderList(opibOrderDetailsList, queryParameter);
			}

		}

		return opibOrderDetailsList;
	}

	/* Private Methods */

	private void populateOrderList(List<OPIBOrderDetails> opibOrderDetailsList, String queryParameter) {
		String query = SIEBEL_STATUS_QUERY.replace("?", queryParameter);
		try (Connection conn = getConnectionByJNDI(SIEBEL_DS_JNDI);
				PreparedStatement prepStmt = conn.prepareStatement(query)) {

			logger.debug(query);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {
				populateOrderFromResultSet(opibOrderDetailsList, rs);
			}
			rs.close();
		} catch (SQLException | NamingException e) {
			throw new ConnectorException("Error while fetch OneCRM delta orders" + e);
		}
	}

	private void populateOrderFromResultSet(List<OPIBOrderDetails> opibOrderDetailsList, ResultSet rs)
			throws SQLException {
		OPIBOrderDetails order = new OPIBOrderDetails();
		order.setSiebelOrderId(rs.getString("ORDER_ID"));

		order.setSiebelOrderDetails(new SiebelOrderDetails());
		order.getSiebelOrderDetails().setSiebelStatus(rs.getString("STATUS"));
		opibOrderDetailsList.add(order);
	}

	private void closeResultSet(ResultSet rs) {
		if (null != rs) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new ConnectorException("Error closing result set " + e);
			}
		}
	}
}
