package com.kpn.opib.bam.data.service;

import java.util.Date;
import java.util.List;

import javax.ejb.DuplicateKeyException;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpn.opib.bam.dao.OPIBOrderRepository;
import com.kpn.opib.bam.data.service.util.OrderUtil;
import com.kpn.opib.bam.model.OPIBOrderDetails;

/**
 * Service class used to expose business operations related to BAM Database.
 * 
 * @author gidwa500
 *
 */
@Stateless
public class BAMOrderDataService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private BamOrderCache bamOrderCache;

	private OPIBOrderRepository orderRepository;

	public BAMOrderDataService() {

	}

	@Inject
	public BAMOrderDataService(BamOrderCache bamOrderCache, OPIBOrderRepository orderDAO) {
		this.bamOrderCache = bamOrderCache;
		this.orderRepository = orderDAO;
	}

	/**
	 * Retrieves all open orders either from BAM Order cache or from BAM
	 * Database.
	 * 
	 * @param Boolean
	 *            fromCache - if fromCache is true get orders from BAM Order
	 *            Cache if fromCache is false get orders from BAM database.
	 * @return List<OPIBOrderDetails> containing open orders
	 */
	public List<OPIBOrderDetails> getOpenOrders(boolean fromCache) {

		logger.info("Retrieving Open Orders -> fromCache = " + fromCache);
		return fromCache ? bamOrderCache.getOpenOrders() : orderRepository.getOpenOrders();
	}

	public List<OPIBOrderDetails> getCorrectedOrders() {

		return orderRepository.getCorrectedOrders();
	}

	/**
	 * Retrieves all open order siebel Ids from BAM Database.
	 * 
	 * 
	 * @return List<String> containing siebel order ids
	 */
	public List<String> getOpenOrderIDs() {
		return orderRepository.getOpenOrderIDs();

	}

	/**
	 * get Order (including functional products) from Siebel order id.
	 * 
	 * @param siebelOrderId
	 * @return If order is present in database then returns instance of class
	 *         OPIBOrderDetails - otherwise returns null.
	 */
	public OPIBOrderDetails getOrderBySiebelOrderId(String siebelOrderId) {

		logger.info("Retrieving Order  -> siebelOrderId = " + siebelOrderId);
		return orderRepository.getOrderFromSiebelOrderId(siebelOrderId);
	}

	/**
	 * Create Order if doesn't exists , Update Order if exists in BAM DB.
	 * 
	 * @param opibOrderDetailsList
	 *            List containing orders to create or merge.
	 */
	public void createOrUpdateOrders(List<OPIBOrderDetails> opibOrderDetailsList) {

		for (OPIBOrderDetails order : opibOrderDetailsList) {

			OPIBOrderDetails existingOrder = getOrderBySiebelOrderId(order.getSiebelOrderId());
			if (null == existingOrder) {
				orderRepository.createOrder(order);
			} else {
				OrderUtil.mergeOrder(order, existingOrder);
				orderRepository.updateOrder(existingOrder);
			}
		}
	}

	public OPIBOrderDetails createOrder(OPIBOrderDetails order) throws DuplicateKeyException {

		OPIBOrderDetails existingOrder = getOrderBySiebelOrderId(order.getSiebelOrderId());

		if (null != existingOrder) {
			throw new DuplicateKeyException(
					"Duplicate Order: Order with Siebel id  " + existingOrder.getSiebelOrderId() + " already exists");
		} else {
			return orderRepository.createOrder(order);
		}
	}

	public OPIBOrderDetails updateOrder(OPIBOrderDetails order) throws OrderNotFoundException {

		if (null == order || null == order.getSiebelOrderId()) {
			throw new IllegalArgumentException("Order/Siebel order Id cannot be null");
		}

		OPIBOrderDetails existingOrder = getOrderBySiebelOrderId(order.getSiebelOrderId());

		if (null == existingOrder) {
			throw new OrderNotFoundException("Order with Siebel id  " + order.getSiebelOrderId() + " doesnot  exists");
		} else {
			if (null != order.getCorrections() && order.getCorrections().isChanged()) {
				order.getCorrections().setCorrectionTime(new Date());
			}
			 orderRepository.updateOrder(order);
			 bamOrderCache.updateCache(order);
			 return order;
		}
	}

	public void deleteOrder(String siebelOrderId) throws OrderNotFoundException {

		if (null == siebelOrderId) {
			throw new IllegalArgumentException("Siebel order Id cannot be null");
		}

		OPIBOrderDetails existingOrder = getOrderBySiebelOrderId(siebelOrderId);

		if (null == existingOrder) {
			throw new OrderNotFoundException("Order with Siebel id  " + siebelOrderId + " doesnot  exists");
		} else {
			orderRepository.deleteOrder(existingOrder);
		}
	}

	public void resetAcknowledgments() {

		orderRepository.resetSiebelAcknowledgements();
		orderRepository.resetCordysAcknowledgements();
		orderRepository.resetFusionAcknowledgements();
	}

}
