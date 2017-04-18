package com.kpn.opib.bam.data.service.util;

import java.util.ArrayList;
import java.util.List;

import com.kpn.opib.bam.model.DerivedCordysOrderStatus;
import com.kpn.opib.bam.model.FunctionalProduct;
import com.kpn.opib.bam.model.OPIBOrderDetails;

public class CordysOrderUtil {

	private CordysOrderUtil() {

	}

	public static void mergeCordysData(OPIBOrderDetails order, OPIBOrderDetails existingOrder) {

		if (null != order.getCordysOrderDetails()) {
			mergeOrderLevelCordysData(order, existingOrder);

			if (null != order.getCordysOrderDetails().getFunctionalProducts()
					&& !order.getCordysOrderDetails().getFunctionalProducts().isEmpty()) {

				List<FunctionalProduct> existingFunctionalProductList = existingOrder.getCordysOrderDetails()
						.getFunctionalProducts();

				List<FunctionalProduct> deltaFunctionalProductList = order.getCordysOrderDetails()
						.getFunctionalProducts();

				mergeFunctionalProductList(existingFunctionalProductList, deltaFunctionalProductList);

			}
		}

	}

	// TODO Implement this method as per the algo provided by Cordys Team.
	private static DerivedCordysOrderStatus deriveCodysOrderColour(OPIBOrderDetails order) {
		int amber_count =0;
		for (FunctionalProduct functionalProduct : order.getCordysOrderDetails().getFunctionalProducts()) {

			switch (functionalProduct.getFunctionalProductStatus()) {

			case COM_ERROR:
				return DerivedCordysOrderStatus.RED;
				
			case SNI_REJECT:
				return DerivedCordysOrderStatus.RED;
				
			case SNI_ERROR:
				return DerivedCordysOrderStatus.RED;

			case SNI_TIMEOUT:
				return DerivedCordysOrderStatus.RED;

			case TECH_ERROR:
				return DerivedCordysOrderStatus.RED;

			case COM_IN_PROC:
				amber_count++;
				break;

			case NOT_REQUESTED:
				amber_count++;
				break;

			case SENT_TO_SNI:
				amber_count++;
				break;

			case SNI_PROVISIONED:
				break;

			case CANCELLED_CRM:
				break;

			case SNI_CANCELLED:
				break;

			case PORTING_SYNC:
				break;

			case RETRY_SENT:
				break;

			case SNI_IN_PROC:
				break;

			case CANCELLED_CRM_AFPROV:
				break;

			case PLN_RECD:
				break;

			case SNI_CANCELLED_BY_COM:
				break;

			case PLN_COMPLETE:
				break;

			case CANCELLED_CRM_DUPROV:
				break;

			case WAIT_PORTING_SYNC:
				break;

			case INITIAL:
				break;

			case PORT_ANS_RECD:
				break;

			default:
				//TODO
				break;
			}
		
	
		}
		if(amber_count > 0)
		{
			return DerivedCordysOrderStatus.AMBER;	
		}
		
			return DerivedCordysOrderStatus.GREEN;
		
	}

	private static void mergeFunctionalProductList(List<FunctionalProduct> existingFunctionalProductList,
			List<FunctionalProduct> deltaFunctionalProductList) {

		for (FunctionalProduct deltaFunctionalProduct : deltaFunctionalProductList) {

			boolean matchFound = false;

			for (FunctionalProduct existingFunctionalProduct : existingFunctionalProductList) {
				if (existingFunctionalProduct.getOrderLineId().equals(deltaFunctionalProduct.getOrderLineId())) {
					matchFound = true;
					mergeFunctionalProduct(existingFunctionalProduct, deltaFunctionalProduct);
					break;
				}
			}
			if (!matchFound) {
				existingFunctionalProductList.add(deltaFunctionalProduct);
			}
		}
	}

	private static void mergeOrderLevelCordysData(OPIBOrderDetails order, OPIBOrderDetails existingOrder) {

		// Set Order level Status
		existingOrder.getCordysOrderDetails()
				.setCordysStatus(order.getCordysOrderDetails().getCordysStatus());

		existingOrder.getCordysOrderDetails().setCordysDerivedStatus(deriveCodysOrderColour(order));

		initializeFunctionalProduct(existingOrder);

	}

	private static void initializeFunctionalProduct(OPIBOrderDetails existingOrder) {
		if (null == existingOrder.getCordysOrderDetails()
				|| null == existingOrder.getCordysOrderDetails().getFunctionalProducts()) {
			existingOrder.getCordysOrderDetails().setFunctionalProducts(new ArrayList<FunctionalProduct>());
		}
	}

	private static void mergeFunctionalProduct(FunctionalProduct existingFunctionalProduct,
			FunctionalProduct deltaFunctionalProduct) {
		existingFunctionalProduct.setAction(deltaFunctionalProduct.getAction());
		existingFunctionalProduct.setFunctionalProductStatus(deltaFunctionalProduct.getFunctionalProductStatus());
		existingFunctionalProduct.setSniInputXml(deltaFunctionalProduct.getSniInputXml());
	}

}
