package com.kpn.opib.bam.status.update.sync;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.kpn.opib.bam.data.service.BAMOrderDataService;

/**
 * HouseKeeping class to set open order Ids(Siebel Order Id) in jobContext. This list is used by
 * downstream steps in the batch.
 * 
 * @author gidwa500
 *
 */
@Named
public class PopulateJobOperatorBatchelet extends AbstractBatchlet {

	private JobContext jobCtx;

	private BAMOrderDataService bamOrderDataService;

	@Inject
	public PopulateJobOperatorBatchelet(JobContext jobCtx, BAMOrderDataService bamOrderDataService) {
		this.jobCtx = jobCtx;
		this.bamOrderDataService = bamOrderDataService;
	}

	/**
	 * Entry Method to initialize open orders list.
	 * 
	 * @return null;
	 * @throws Exception
	 */
	@Override
	public String process() throws Exception {

		BatchRuntime.getJobOperator().getParameters(jobCtx.getExecutionId()).put("openOrderIdList",
				bamOrderDataService.getOpenOrderIDs());
		return null;
	}

}
