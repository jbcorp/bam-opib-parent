package com.kpn.bam.db.framework;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Ignore;
import org.junit.Test;

import com.kpn.opib.bam.model.OPIBOrderDetails;

public class OPIBOrderRepositoryTest {


	@Test
	@Ignore
	public void createTaxCategory()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("bamTestPU");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		OPIBOrderDetails order  = new OPIBOrderDetails();		
		em.persist(order);
		tx.commit();
		em.close();
		emf.close();
		
	}
}


