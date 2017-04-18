package com.kpn.opib.bam.delta.order.sync;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.JobExecution;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpn.bam.opib.adapter.siebel.SiebelAdapter;
import com.kpn.bam.opib.connector.Connector;
import com.kpn.bam.opib.utils.BatchConstants;
import com.kpn.opib.bam.model.OPIBOrderDetails;
import com.kpn.opib.bam.process.service.BatchService;

/**
 * Class used to read open orders from Siebel Database.
 * 
 * @author gidwa500
 *
 */
@Named
public class SiebelOpenOrderReader extends AbstractItemReader {

	private List<OPIBOrderDetails> openOrders = new ArrayList<>();

	private BatchService batchService;

	private final Logger logger = LoggerFactory.getLogger(SiebelOpenOrderReader.class);

	public SiebelOpenOrderReader() {
		/* Constructor for Java EE Object creation only. */ }

	@Inject
	public SiebelOpenOrderReader(BatchService batchService) {
		this.batchService = batchService;
	}

	@Override
	public void open(Serializable checkpoint) throws Exception {

		if (openOrders.isEmpty()) {
			logger.info("-------------Creating " + Connector.class + " -----------");
			Connector connector = new Connector(new SiebelAdapter());
			logger.info("-------------Created " + Connector.class + " -----------");
			logger.info("-------------Fetching  Failed/Open orders  -----------");
			JobExecution lastExecution = batchService.getLastBatchExecution(BatchConstants.OPIB_BAM_SYNC_JOB);
			Date lastSync = null;
			if (null != lastExecution) {
				lastSync = lastExecution.getEndTime();
			}
			openOrders = connector.getOpenOrders(lastSync);
			// openOrders = connector.getOpenOrders(new Date()); //TODO
			logger.info("Failed/Open orders Count " + openOrders.size());
		}
	}

	@Override
	public Object readItem() throws Exception {
		if (!openOrders.isEmpty()) {
			Object orderToProcess = openOrders.get(0);
			openOrders.remove(0);
			logger.info("Reading OneCRm Order " + orderToProcess);
			return orderToProcess;
		}
		return null;
	}

}
