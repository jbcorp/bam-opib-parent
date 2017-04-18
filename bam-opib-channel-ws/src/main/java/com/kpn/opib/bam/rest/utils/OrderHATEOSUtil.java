package com.kpn.opib.bam.rest.utils;

import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.kpn.opib.bam.model.Link;
import com.kpn.opib.bam.model.OPIBOrderDetails;

public class OrderHATEOSUtil {
	
	public static void updateHATEOS_Links(List<OPIBOrderDetails> orders, UriInfo uriInfo) {
		for (OPIBOrderDetails order : orders) {
			if (order.getLinks().isEmpty()) {
				updateUriForSelf(uriInfo, order);
			}
		}
	}

	public static  OPIBOrderDetails updateUriForSelf(UriInfo uriInfo, OPIBOrderDetails order) {
		Link selfLink = new Link();
		selfLink.setUrl(uriInfo.getAbsolutePathBuilder().path(order.getSiebelOrderId()).build().toString());
		selfLink.setRel(Link.LINK_REL_SELF);
		order.getLinks().add(selfLink);
		return order;
	}

}
