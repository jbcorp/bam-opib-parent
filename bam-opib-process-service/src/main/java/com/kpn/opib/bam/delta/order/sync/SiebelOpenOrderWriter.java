package com.kpn.opib.bam.delta.order.sync;

import java.util.ArrayList;
import java.util.List;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpn.opib.bam.data.service.BAMOrderDataService;
import com.kpn.opib.bam.model.OPIBOrderDetails;

@Named
public class SiebelOpenOrderWriter extends AbstractItemWriter {

	private BAMOrderDataService bamOrderDataService;

	private final Logger logger = LoggerFactory.getLogger(SiebelOpenOrderWriter.class);

	public SiebelOpenOrderWriter() {
		// Default constructor for Java EE Only.
	}

	@Inject
	public SiebelOpenOrderWriter(BAMOrderDataService bamOrderDataService) {
		this.bamOrderDataService = bamOrderDataService;
	}

	@Override
	public void writeItems(List<Object> items) throws Exception {

		logger.info("Writing OneCrm Open orders to BAM ");
		List<OPIBOrderDetails> orderList = mapObjectListToOrderList(items);
		bamOrderDataService.createOrUpdateOrders(orderList);

	}

	// :-( => Java EE batch dont support generics.
	private List<OPIBOrderDetails> mapObjectListToOrderList(List<Object> items) {

		List<OPIBOrderDetails> orderList = new ArrayList<>();
		for (Object item : items) {
			orderList.add((OPIBOrderDetails) item);
		}
		return orderList;
	}

}
