package com.kpn.opib.bam.batch.rest;

import static com.kpn.opib.bam.rest.utils.OrderHATEOSUtil.updateHATEOS_Links;

import java.util.List;

import javax.ejb.DuplicateKeyException;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpn.opib.bam.data.service.BAMOrderDataService;
import com.kpn.opib.bam.data.service.OrderNotFoundException;
import com.kpn.opib.bam.model.Link;
import com.kpn.opib.bam.model.OPIBOrderDetails;

@Path("/order")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })

/**
 * Rest Resource representation for orders in BAM database.
 * 
 * @author gidwa500
 *
 */

public class BAMOrderResource {

	private BAMOrderDataService bamOrderDataService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public BAMOrderResource() {

	}

	/**
	 * Constructor to inject BAMOrderDataService
	 * 
	 * @param bamOrderDataService
	 *            Instance of BAMOrderDataService to perform business
	 *            operations.
	 * @return - None
	 */
	@Inject
	public BAMOrderResource(BAMOrderDataService bamOrderDataService) {
		assert null != bamOrderDataService;
		this.bamOrderDataService = bamOrderDataService;
		logger.debug("Initializing " + BAMOrderResource.class.getName());
	}

	@GET
	@Path("/{siebelOrderId}")
	/**
	 * 
	 * @param siebelOrderId
	 * @param uriInfo
	 * @return
	 */
	public Response getOrder(@PathParam("siebelOrderId") String siebelOrderId, @Context UriInfo uriInfo) {

		OPIBOrderDetails order = bamOrderDataService.getOrderBySiebelOrderId(siebelOrderId);
		if (null != order) {
			Link selfLink = new Link();
			selfLink.setUrl(uriInfo.getAbsolutePath().toString());
			selfLink.setRel(Link.LINK_REL_SELF);
			order.getLinks().add(selfLink);
			return Response.status(Status.OK).entity(order).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}

	}

	/**
	 * Fetches all open orders present in BAM database.
	 * 
	 * @param -
	 *            UriInfo Injected by Framework Context.
	 * @return Response containing all open orders present in BAM database.
	 * 
	 */
	@GET
	public Response getOpenOrders(@Context UriInfo uriInfo) {

		logger.debug("Retrieving open orders list from service");
		boolean fromCache = true;
		List<OPIBOrderDetails> orders = bamOrderDataService.getOpenOrders(fromCache);
		updateHATEOS_Links(orders, uriInfo);
		clearFunctionalProducts(orders);
		logger.debug("Retrieved open orders List : Count " + orders.size());
		return !orders.isEmpty() ? Response.status(Status.OK).entity(orders).build()
				: Response.status(Status.NOT_FOUND).build();
	}

	@POST
	public Response createOrder(OPIBOrderDetails order) {

		try {
			return Response.status(Status.CREATED).entity(bamOrderDataService.createOrder(order)).build();
		} catch (DuplicateKeyException e) {
			logger.error("Order already exists" + e);
			return Response.status(Status.CONFLICT).entity(order).build();
		}
	}

	@PUT
	@Path("/{siebelOrderId}")
	public Response updateOrder(@PathParam("siebelOrderId") String siebelOrderId, OPIBOrderDetails order) {
		order.setSiebelOrderId(siebelOrderId);
		try {
			return Response.status(Status.OK).entity(bamOrderDataService.updateOrder(order)).build();
		} catch (OrderNotFoundException e) {
			logger.error("Order Not found " + e);
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@DELETE
	@Path("/{siebelOrderId}")
	public Response deleteOrder(@PathParam("siebelOrderId") String siebelOrderId) {

		try 
		{
			bamOrderDataService.deleteOrder(siebelOrderId);
			return Response.status(Status.NO_CONTENT).build();
		} catch (OrderNotFoundException e) {
			logger.error("Order Not found " + e);
			return Response.status(Status.NOT_FOUND).build();
		}

	}
	
	
	
	/**
	 * Fetches all orders that are under correction or corrected in past.
	 * 
	 * @param -
	 *            UriInfo Injected by Framework Context.
	 * @return Response containing all open orders present in BAM database.
	 * 
	 */
	@GET
	@Path("/corrected")
	public Response getCorrectedOrders(@Context UriInfo uriInfo) {

		logger.debug("Retrieving corrected orders list from service");		
		List<OPIBOrderDetails> orders = bamOrderDataService.getCorrectedOrders();
		clearFunctionalProducts(orders);
		updateHATEOS_Links(orders, uriInfo);
		logger.debug("Retrieved open orders List : Count " + orders.size());
		
		
		
		return !orders.isEmpty() ? Response.status(Status.OK).entity(orders).build()
				: Response.status(Status.NOT_FOUND).build();
	}
	
	/**
	 * Functional products are not sent  for performance reasons so they are set
	 * to null.
	 * @param orders 
	 * 
	 */
	private void clearFunctionalProducts(List<OPIBOrderDetails> orders) {
		for (OPIBOrderDetails bamOrder : orders) {
			if (null != bamOrder.getCordysOrderDetails()
					&& null != bamOrder.getCordysOrderDetails().getFunctionalProducts()) {
				bamOrder.getCordysOrderDetails().setFunctionalProducts(null);
			}
		}
	}

}
