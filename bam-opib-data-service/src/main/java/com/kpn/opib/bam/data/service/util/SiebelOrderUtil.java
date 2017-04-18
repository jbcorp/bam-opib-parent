package com.kpn.opib.bam.data.service.util;

import com.kpn.opib.bam.model.OPIBOrderDetails;

public final class SiebelOrderUtil {

	private SiebelOrderUtil() {

	}

	public static void mergeSiebelData(OPIBOrderDetails order, OPIBOrderDetails existingOrder) {
		if (null != order.getSiebelOrderDetails() && null != order.getSiebelOrderDetails().getSiebelStatus()) {
			existingOrder.getSiebelOrderDetails().setSiebelStatus(order.getSiebelOrderDetails().getSiebelStatus());
		}
	}

}
