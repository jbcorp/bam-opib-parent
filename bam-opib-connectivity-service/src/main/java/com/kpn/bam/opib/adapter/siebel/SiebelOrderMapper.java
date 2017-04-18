package com.kpn.bam.opib.adapter.siebel;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpn.opib.bam.model.Corrections;
import com.kpn.opib.bam.model.Customer;
import com.kpn.opib.bam.model.OPIBOrderDetails;
import com.kpn.opib.bam.model.OrderType;
import com.kpn.opib.bam.model.SiebelOrderDetails;

/**
 * Class used to Map ResultSet data to {@link OPIBOrderDetails}
 * 
 * @author gidwa500
 *
 */
class SiebelOrderMapper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SiebelOrderMapper.class);

	private SiebelOrderMapper() {

	}
		

	protected static OPIBOrderDetails mapRow(ResultSet rs, String createdBy, String modifiedBy) throws SQLException {

		OPIBOrderDetails order = new OPIBOrderDetails();
	
//		Customer customer = new Customer();
//		customer.setAccountName(rs.getString("ACCOUNT_NAME"));
//		customer.setCkrNumber(rs.getString("CKR_NUMBER"));
//		order.setCustomer(customer);
		
		order.setCreatedBy(createdBy);
		order.setUpdatedBy(modifiedBy);
		order.setSiebelOrderId(rs.getString("row_id"));
		order.setSiebelOrderNumber(rs.getString("order_num"));
		order.setOrderType(getOrderType(rs.getString("x_sub_type")));
		order.setSiebelCreationDate(rs.getDate("created"));
		Corrections corrections = new Corrections();	
		order.setCorrections(corrections);
		SiebelOrderDetails siebelOrderDetails = new SiebelOrderDetails();
		siebelOrderDetails.setSiebelStatus(rs.getString("status_cd"));
		order.setSiebelOrderDetails(siebelOrderDetails);
		LOGGER.debug(order.toString());
		return order;
	}

	private static OrderType getOrderType(String order) {

		OrderType orderType;

		switch (order.toUpperCase()) {

		case "ORDER":
			orderType = OrderType.ADD;
			break;
		case "DISCONNECT":
			orderType = OrderType.DELETE;
			break;
		case "MOVE":
			orderType = OrderType.MOVE;
			break;
		case "CONVERT":
			orderType = OrderType.MIGRATE;
			break;
		case "COMMERCIAL MODIFICATION":
			orderType = OrderType.CANCEL;
			break;
		case "TRANSFER CONTRACT":
			orderType = OrderType.TRANSFER_CONTRACT;
			break;
		case "CHANGE BILLING PROFILE":
			orderType = OrderType.CHANGE_BILLING_PROFILE;
			break;
		case "ADMIN SYNC":
			orderType = OrderType.ADMIN_SYNC;
			break;
		default:
			orderType = OrderType.NOT_SUPPORTED;

		}

		return orderType;

	}

}
