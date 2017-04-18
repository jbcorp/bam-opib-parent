package com.kpn.bam.opib.connector;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpn.opib.bam.model.OPIBOrderDetails;

/**
 * 
 * @author gidwa500
 *
 */

public class Connector {

	private OPIBAdapter adapter;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public Connector(OPIBAdapter adapter) {

		if (null == adapter) {
			throw new IllegalArgumentException("Adapter cannot be null !");
		}
		this.adapter = adapter;
	}

	/**
	 * Get Order Data from Target system for the input siebel order id list
	 * 
	 * @param orderIds
	 *            - List of orderIds for which the status needs to be fetched.
	 * @return List of {@link OPIBOrderDetails} with updated order based in
	 *         input adapter.
	 */
	public List<OPIBOrderDetails> getOrderData(List<String> orderIds) {

		List<OPIBOrderDetails> orderList = Collections.emptyList();

		if (null != orderIds && !orderIds.isEmpty()) {
			logger.info("Order Data Retrieved using " + adapter.getClass().getTypeName() + " adapter");
			orderList = adapter.getOrders(orderIds);
			logger.info("Input List Size = " + orderIds.size() + " Output List Size " + orderList.size());
			logger.info("Loss of Orders = " + (orderIds.size() - orderList.size()));
		}

		return orderList;
	}

	public List<OPIBOrderDetails> getOpenOrders(Date onOrAfterCreationDate) {

		return adapter.getOpenOrders(onOrAfterCreationDate);
	}

}