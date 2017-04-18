package com.kpn.opib.bam.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpn.opib.bam.model.OPIBOrderDetails;

public class OPIBOrderRepository {

	private EntityManager em;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public OPIBOrderRepository() {

	}

	@Inject
	public OPIBOrderRepository(EntityManager em) {
		this.em = em;
	}

	/**
	 * get Order (including functional products) from Siebel order id.
	 * 
	 * @param siebelOrderId
	 * @return If order is present in database then returns instance of class
	 *         OPIBOrderDetails - otherwise returns null.
	 */
	public OPIBOrderDetails getOrderFromSiebelOrderId(String siebelOrderId) {

		if (null == siebelOrderId) {
			return null;
		}

		TypedQuery<OPIBOrderDetails> query = em.createNamedQuery(OPIBOrderDetails.FIND_BY_CRM_ORDER_ID,
				OPIBOrderDetails.class);
		query.setParameter("siebelOrderId", siebelOrderId);

		try {
			OPIBOrderDetails fetchedOrder = query.getSingleResult();
			if (null != fetchedOrder.getCordysOrderDetails()) {
				fetchedOrder.getCordysOrderDetails().getFunctionalProducts().size();
			}
			return fetchedOrder;
		} catch (javax.persistence.NoResultException ne) {
			logger.info("No Order found in BAM DB with CrmOrderId " + siebelOrderId + " " + ne);
			return null;
		}
	}

	public List<OPIBOrderDetails> getOpenOrders() {

		TypedQuery<OPIBOrderDetails> query = em.createNamedQuery(OPIBOrderDetails.FIND_ORDERS_NOT_FINAL,
				OPIBOrderDetails.class);
		return query.getResultList();

	}
	
	public List<OPIBOrderDetails> getCorrectedOrders() {

		TypedQuery<OPIBOrderDetails> query = em.createNamedQuery(OPIBOrderDetails.FIND_CORRECTED_ORDERS,
				OPIBOrderDetails.class);
		return query.getResultList();

	}

	public List<String> getOpenOrderIDs() {

		TypedQuery<String> query = em.createNamedQuery(OPIBOrderDetails.FIND_ORDER_IDS_NOT_FINAL, String.class);
		return query.getResultList();

	}

	public OPIBOrderDetails createOrder(OPIBOrderDetails order) {
		logger.info("Creating new order record in database " + order);
		em.persist(order);
		return order;
	}

	public OPIBOrderDetails updateOrder(OPIBOrderDetails order) {
		logger.debug("Merging existing order " + order);
		em.merge(order);
		return order;
	}

	public void deleteOrder(String siebelOrderId) {
		logger.debug("Deleting  order " + siebelOrderId);
		em.remove(em.merge(getOrderFromSiebelOrderId(siebelOrderId)));
	}

	public void deleteOrder(OPIBOrderDetails order) {
		logger.debug("Deleting  order " + order);
		em.remove(em.merge(order));
	}

	public int resetSiebelAcknowledgements() {
		return executeUpdateQuery(OPIBOrderDetails.RESET_SIEBEL_ACK);
	}

	public int resetCordysAcknowledgements() {
		return executeUpdateQuery(OPIBOrderDetails.RESET_CORDYS_ACK);
	}

	public int resetFusionAcknowledgements() {
		return executeUpdateQuery(OPIBOrderDetails.RESET_FUSION_ACK);
	}

	private int executeUpdateQuery(String query) {
		return em.createNamedQuery(query).executeUpdate();

	}

}
