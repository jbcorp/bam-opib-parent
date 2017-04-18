package com.kpn.bam.opib.adapter.cordys;

import static com.kpn.bam.opib.utils.DBUtils.getConnectionByJNDI;
import static com.kpn.bam.opib.utils.DBUtils.getQueryInputBatches;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import com.kpn.bam.opib.connector.ConnectorException;
import com.kpn.bam.opib.connector.OPIBAdapter;
import com.kpn.opib.bam.model.CordysOrderDetails;
import com.kpn.opib.bam.model.CordysOrderStatus;
import com.kpn.opib.bam.model.DerivedCordysOrderStatus;
import com.kpn.opib.bam.model.FunctionalProduct;
import com.kpn.opib.bam.model.FunctionalProductStatus;
import com.kpn.opib.bam.model.FunctionalProductType;
import com.kpn.opib.bam.model.OPIBOrderDetails;

public class CordysAdapter implements OPIBAdapter {

	private static final String GET_CORDYS_DETAILS_OF_OPEN_ORDERS = "SELECT * FROM CORDYS_ZIPBDATA_OWNER.CO_ORDER_MASTER O , CORDYS_ZIPBDATA_OWNER.CO_ORDER_DETAILS OD  "
			+ "WHERE O.CO_ORDER_ID = OD.CO_ORDER_ID AND O.CRM_ORDER_ID IN ( ? )";

	private static final String CORDYS_DS_JNDI = "java:/CORDYS_DS";

	/**
	 * Get Cordys status of input order id list
	 * 
	 * @param orderIds
	 *            - List of orderIds for which the status needs to be fetched.
	 * @return List of {@link OPIBOrderDetails} with updated fusion status
	 */
	@Override
	public List<OPIBOrderDetails> getOrders(List<String> orderIds) {

		Map<String, OPIBOrderDetails> opibOrderMap = new HashMap<>();

		if (null != orderIds && !orderIds.isEmpty()) {
			List<String> orderIdList = getQueryInputBatches(orderIds);
			// Oracle have limit of 1000 items within IN clause

			System.out.println("Number of batches " + orderIdList.size());

			int batchCount = 0;
			for (String queryParameter : orderIdList) {
				batchCount++;
				String query = GET_CORDYS_DETAILS_OF_OPEN_ORDERS.replace("?", queryParameter);
				System.out.println("Before connection");
				try (Connection conn = getConnectionByJNDI(CORDYS_DS_JNDI);
						PreparedStatement prepStmt = conn.prepareStatement(query)) {
					System.out.println("After connection");
					System.out.println("---------Batch Count " + batchCount + "------------");
					System.out.println(query);
					System.out.println("---------------------");

					System.out.println("Before executeQuery");
					ResultSet rs = prepStmt.executeQuery();
					System.out.println("After executeQuery");

					FunctionalProduct functionalProduct;
					while (rs.next()) {
						String siebelOrderId = rs.getString("CRM_ORDER_ID");

						OPIBOrderDetails opibOrderDetails = opibOrderMap.get(siebelOrderId);
						functionalProduct = new FunctionalProduct();

						if (null == opibOrderDetails) {

							opibOrderDetails = new OPIBOrderDetails();
							opibOrderDetails.setSiebelOrderId(siebelOrderId);

							CordysOrderDetails cordysOrderDetails = new CordysOrderDetails();

							opibOrderDetails.setCordysOrderDetails(cordysOrderDetails);

							cordysOrderDetails.setComOrderId((rs.getString("CO_ORDER_ID")));
							cordysOrderDetails
									.setCordysStatus(CordysOrderStatus.valueOf(rs.getString("COM_STATUS")));
							opibOrderDetails.getCordysOrderDetails()
									.setFunctionalProducts(new ArrayList<FunctionalProduct>());
							opibOrderMap.put(siebelOrderId, opibOrderDetails);

						}

						functionalProduct.setComOrderId(rs.getString("CO_ORDER_ID"));
						functionalProduct
								.setFunctionalProductStatus(FunctionalProductStatus.valueOf(rs.getString("STATUS")));
						functionalProduct
								.setFunctionalProductType(FunctionalProductType.valueOf(rs.getString("FUNC_PROD")));
						functionalProduct.setIdentifier(rs.getString("IDENTIFIER_1"));
						functionalProduct.setSniOrderId(rs.getString("SNI_ORDER_ID"));
						functionalProduct.setSniInputXml(rs.getString("SNI_REQUEST"));
						functionalProduct.setAction(rs.getString("CO_ACTION"));
						functionalProduct.setOrderLineId(rs.getString("CO_ORDER_LINE_ID"));
						// TODO Derive in service layer.
						opibOrderDetails.getCordysOrderDetails().setCordysDerivedStatus(DerivedCordysOrderStatus.GREEN);
						System.out.println("Populating Cordys Order ");
						opibOrderDetails.getCordysOrderDetails().getFunctionalProducts().add(functionalProduct);
					}
					rs.close();
				} catch (SQLException | NamingException e) {
					throw new ConnectorException("Error while fetch Cordys delta orders" + e);
				}
			}

		}
		return Collections.list(Collections.enumeration(opibOrderMap.values()));
	}


}
