package com.kpn.opib.bam.status.update.sync;

import java.util.List;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.google.common.collect.Lists;
import com.kpn.bam.opib.adapter.cordys.CordysAdapter;
import com.kpn.bam.opib.adapter.fusion.FusionAdapter;
import com.kpn.bam.opib.adapter.siebel.SiebelAdapter;
import com.kpn.bam.opib.connector.Connector;
import com.kpn.bam.opib.connector.OPIBAdapter;
import com.kpn.opib.bam.data.service.BAMOrderDataService;
import com.kpn.opib.bam.model.OPIBOrderDetails;

@Named
public class StatusUpdateBatchelet extends AbstractBatchlet {

	private JobContext jobCtx;

	@Inject
	@BatchProperty(name = "targetSystem")
	String targetSystem;

	@Inject
	StepContext stepContext;

	private BAMOrderDataService bamOrderDataService;

	@Inject
	public StatusUpdateBatchelet(JobContext jobCtx, BAMOrderDataService bamOrderDataService) {
		this.jobCtx = jobCtx;
		this.bamOrderDataService = bamOrderDataService;
	}

	private OPIBAdapter getAdapter() {

		String targetSystem = stepContext.getProperties().getProperty("targetSystem");
		// String value1 = stepProperties.getProperty("stepProp1");

		// String targetSystem =
		// BatchRuntime.getJobOperator().getParameters(jobCtx.getExecutionId()).getProperty("targetSystem");

		OPIBAdapter adapter = null;
		System.out.println("--- Target System----" + targetSystem);
		switch (targetSystem) {
		case "SIEBEL":
			adapter = new SiebelAdapter();
			break;
		case "CORDYS":
			adapter = new CordysAdapter();
			break;
		case "FUSION":
			adapter = new FusionAdapter();
			break;

		}

		return adapter;

	}

	@Override
	public String process() throws Exception {
		Connector connector = new Connector(getAdapter());
		List<String> orderIds = getOpenOrderList();
		List<OPIBOrderDetails> orders = connector.getOrderData(orderIds);
		List<List<OPIBOrderDetails>> splitedListOfOrders = Lists.partition(orders, 100);
		for (List<OPIBOrderDetails> orderList : splitedListOfOrders) {
			
			bamOrderDataService.createOrUpdateOrders(orderList);
		}

		return "COMPLETED";
	}

	@SuppressWarnings("unchecked")
	private List<String> getOpenOrderList() {
		return (List<String>) BatchRuntime.getJobOperator().getParameters(jobCtx.getExecutionId())
				.get("openOrderIdList");

	}

	public String getTargetSystem() {
		return targetSystem;
	}

	public void setTargetSystem(String targetSystem) {
		this.targetSystem = targetSystem;
	}

}
