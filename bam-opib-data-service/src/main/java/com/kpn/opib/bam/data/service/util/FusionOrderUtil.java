package com.kpn.opib.bam.data.service.util;

import com.kpn.opib.bam.model.FusionOrderDetails;
import com.kpn.opib.bam.model.OPIBOrderDetails;

public class FusionOrderUtil {

	private FusionOrderUtil() {

	}

	public static void mergeFusionData(OPIBOrderDetails order, OPIBOrderDetails existingOrder) {
		if (null != order.getFusionOrderDetails() && null != order.getFusionOrderDetails().getFusionStatus()) {
			if (null == existingOrder.getFusionOrderDetails()) {
				existingOrder.setFusionOrderDetails(new FusionOrderDetails());
			}
			existingOrder.getFusionOrderDetails().setFusionStatus(order.getFusionOrderDetails().getFusionStatus());
		}
	}

}
