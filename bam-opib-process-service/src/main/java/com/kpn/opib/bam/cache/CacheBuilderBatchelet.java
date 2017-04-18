package com.kpn.opib.bam.cache;

import javax.batch.api.AbstractBatchlet;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpn.opib.bam.data.service.BamOrderCache;

/**
 * CacheBuilderBatchelet is responsible to build BamOrderCache. Cache is used to
 * reduce number of DB calls and improve performance.
 * 
 * @author gidwa500
 *
 */
@Named
public class CacheBuilderBatchelet extends AbstractBatchlet {

	private final Logger logger = LoggerFactory.getLogger(CacheBuilderBatchelet.class);

	private BamOrderCache bamOrderCache;

	@Inject
	public CacheBuilderBatchelet(BamOrderCache bamOrderCache) {
		this.bamOrderCache = bamOrderCache;
	}

	/**
	 *  Entry Method to build order cache.
	 *  
	 */
	@Override
	public String process() throws Exception {
		bamOrderCache.buildCache();		
		return null;
	}

}
