package com.kpn.opib.bam.delta.order.sync;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpn.opib.bam.model.OPIBOrderDetails;
import com.kpn.opib.bam.model.OrderType;

@Named
public class SiebelOpenOrderProcessor implements ItemProcessor {

	private final Logger logger = LoggerFactory.getLogger(SiebelOpenOrderProcessor.class);

	@Override
	public Object processItem(Object item) throws Exception {
		
	

		OPIBOrderDetails order;
		logger.info("Processing OneCrm Order " + item);

		if (!(item instanceof OPIBOrderDetails)) {
			logger.error("Error item is not of type " + OPIBOrderDetails.class);
			return null;
		} else {
			order = (OPIBOrderDetails) item;
			OrderType orderType = order.getOrderType();
			if (orderType == OrderType.ADMIN_SYNC || orderType == OrderType.CHANGE_BILLING_PROFILE
					|| orderType == OrderType.TRANSFER_CONTRACT || orderType == OrderType.NOT_SUPPORTED) {
				return null;
			}
		}

		return item;
	}

}
