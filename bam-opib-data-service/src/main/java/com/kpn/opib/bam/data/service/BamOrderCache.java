package com.kpn.opib.bam.data.service;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpn.opib.bam.model.OPIBOrderDetails;

/**
 * BamOrderCache is used for performance improvement for retrieving all open
 * orders in BAM.
 * 
 * Cache is built at the end of sync job and it is used (not limited to ) by
 * services exposed to Front end.
 * 
 * @author gidwa500
 *
 */
@ApplicationScoped
@Startup
public class BamOrderCache {

	private List<OPIBOrderDetails> bamOrders = Collections.<OPIBOrderDetails>emptyList();

	@Inject
	private BAMOrderDataService bamOrderDataService;
	
	private final Logger logger = LoggerFactory.getLogger(BamOrderCache.class);

	/**
	 * Method to retrieve cached open orders.
	 * 
	 * @return List of cached open orders
	 *
	 */
	public List<OPIBOrderDetails> getOpenOrders() {
		if (null == bamOrders || bamOrders.isEmpty()) {
			buildCache();
		}
		return bamOrders;
	}

	/**
	 * 
	 * Method to build cache
	 * 
	 */
	@PostConstruct
	public void buildCache() {
		boolean fromCache = false;
		bamOrders = bamOrderDataService.getOpenOrders(fromCache);
		//clearFunctionalProducts();
		Collections.reverse(bamOrders);
		logger.info("OrderCache Built Successfully");
	}

	/**
	 * Method to clear cache
	 * 
	 */

	public void clearCache() {
		bamOrders = Collections.<OPIBOrderDetails>emptyList();
	}

	public void updateCache(OPIBOrderDetails order) {
		int index = bamOrders.indexOf(order);
		System.out.println("Index of order " + order + " = " + index);
		if (index >= 0) {
			if (null != order.getCordysOrderDetails()
					&& null != order.getCordysOrderDetails().getFunctionalProducts()) {
				order.getCordysOrderDetails().setFunctionalProducts(null);
			}

			bamOrders.set(index, order);
		}
	}
	
	

//	/**
//	 * Functional products are not cached fo performance reasons so they are set
//	 * to null.
//	 * 
//	 */
//	private void clearFunctionalProducts() {
//		for (OPIBOrderDetails bamOrder : bamOrders) {
//			if (null != bamOrder.getCordysOrderDetails()
//					&& null != bamOrder.getCordysOrderDetails().getFunctionalProducts()) {
//				bamOrder.getCordysOrderDetails().setFunctionalProducts(null);
//			}
//		}
//	}

}
