package com.kpn.opib.bam.framework;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EntityManagerProducer
{
	
	@PersistenceContext(unitName = "bamPU")
	private  EntityManager em;
	
	
	@Produces
	private EntityManager createEM()
	{
		return em;
	}
}