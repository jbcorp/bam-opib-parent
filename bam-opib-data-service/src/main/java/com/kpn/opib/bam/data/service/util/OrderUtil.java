package com.kpn.opib.bam.data.service.util;

import com.kpn.opib.bam.model.Corrections;
import com.kpn.opib.bam.model.OPIBOrderDetails;

public class OrderUtil {

	private OrderUtil() {

	}

	public static void mergeOrder(OPIBOrderDetails order, OPIBOrderDetails existingOrder) {

		mergeCorrections(order, existingOrder);
		SiebelOrderUtil.mergeSiebelData(order, existingOrder);
		FusionOrderUtil.mergeFusionData(order, existingOrder);
		CordysOrderUtil.mergeCordysData(order, existingOrder);
	}

	private static void mergeCorrections(OPIBOrderDetails order, OPIBOrderDetails existingOrder) {

		if (null != order && null != order.getCorrections() && order.getCorrections().isChanged()) {
			if (null == existingOrder.getCorrections()) {
				existingOrder.setCorrections(new Corrections());
			}

			existingOrder.getCorrections().setSiebelAcknowledged(order.getCorrections().isSiebelAcknowledged());
			existingOrder.getCorrections().setCordysAcknowledged(order.getCorrections().isCordysAcknowledged());
			existingOrder.getCorrections().setFusionAcknowledged(order.getCorrections().isFusionAcknowledged());

			existingOrder.getCorrections().setSiebelCorrected(order.getCorrections().isSiebelCorrected());
			existingOrder.getCorrections().setCordysCorrected(order.getCorrections().isCordysCorrected());
			existingOrder.getCorrections().setFusionCorrected(order.getCorrections().isFusionCorrected());

			existingOrder.getCorrections().setEta(order.getCorrections().getEta());
			existingOrder.getCorrections().setWorkaround(order.getCorrections().getWorkaround());
			existingOrder.getCorrections().setComment(order.getCorrections().getComment());

		}
	}

}
