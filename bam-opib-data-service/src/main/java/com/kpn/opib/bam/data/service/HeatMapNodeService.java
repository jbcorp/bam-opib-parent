package com.kpn.opib.bam.data.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.kpn.opib.bam.model.HeatMapNode;
import com.kpn.opib.bam.model.NodeDetails;



@Stateless
public class HeatMapNodeService implements Serializable {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	@Inject
	EntityManager em;

	@Resource
	private SessionContext sessionContext;

	/*protected EntityManager getEntityManager() throws NamingException {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("bamPU");
		return emf.createEntityManager();
	}*/

	//@PersistenceContext(unitName ="persistence")
    public void setEntityManager(EntityManager entityManager){
        this.em = entityManager;
    }
	
	
	@SuppressWarnings("unchecked")
	public List<HeatMapNode> getHeatMapData() {
		Query query = em.createNamedQuery(HeatMapNode.getAllheatMapChannel, HeatMapNode.class);
		//Query query = em.createQuery("from HeatMapNode");
		return query.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<NodeDetails> getNodeDetailsList() {
		Query query = em.createNamedQuery(NodeDetails.getAllNode, NodeDetails.class);
		return query.getResultList();
	}

	public void updateHeatMapNode(List<HeatMapNode> heatMapNodes) {
		try {

			for (HeatMapNode heatMapNode : heatMapNodes) {
				heatMapNode.setTimeStamp(new Date());
				heatMapNode.setnID(null);
				em.merge(heatMapNode);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
