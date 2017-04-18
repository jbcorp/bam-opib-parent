package com.kpn.bam.opib.crm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.kpn.bam.opib.connector.Connector;
import com.kpn.bam.opib.connector.OPIBAdapter;
import com.kpn.opib.bam.model.FusionOrderDetails;
import com.kpn.opib.bam.model.FusionStatus;
import com.kpn.opib.bam.model.OPIBOrderDetails;

public class ConnectorTest {

	private Connector connector = null;

	@Mock
	private OPIBAdapter adapter;

	private List<String> orderIds = new ArrayList<>();

	private List<OPIBOrderDetails> orderList = new ArrayList<>();

	@Before
	public void setUp() {
		System.out.println();

		orderIds = new ArrayList<>();
		orderIds.add("78-QJ9NSTO");
		orderIds.add("78-OUFWP1I");

		OPIBOrderDetails order1 = new OPIBOrderDetails();
		order1.setSiebelOrderId("78-QJ9NSTO");
		FusionOrderDetails fusionOrderDetails = new FusionOrderDetails();
		fusionOrderDetails.setFusionStatus(FusionStatus.IN_PROGRESS);
		order1.setFusionOrderDetails(fusionOrderDetails);

		OPIBOrderDetails order2 = new OPIBOrderDetails();
		order2.setSiebelOrderId("78-OUFWP1I");
		FusionOrderDetails fusionOrderDetails1 = new FusionOrderDetails();
		fusionOrderDetails1.setFusionStatus(FusionStatus.IN_PROGRESS);
		order2.setFusionOrderDetails(fusionOrderDetails1);

		orderList.add(order1);
		orderList.add(order2);

		MockitoAnnotations.initMocks(this);

		connector = new Connector(adapter);

	}

	@After
	public void tearDown() {
		connector = null;
		orderIds = new ArrayList<>();
		orderList = new ArrayList<>();
	}

	@Test
	public void getOrderData_should_get_order_data() {

		when(adapter.getOrders(orderIds)).thenReturn(orderList);
		List<OPIBOrderDetails> orders = connector.getOrderData(orderIds);
		assertNotNull(orders);
		assertEquals(2, orders.size());
		verify(adapter).getOrders(orderIds);

	}

	@Test
	public void getOrderData_should_return_empty_list_NULL_INPUT() {

		List<OPIBOrderDetails> orders = connector.getOrderData(null);
		assertNotNull(orders);
		assertEquals(0, orders.size());

	}

	@Test
	public void getOrderData_should_return_empty_list() {

		List<OPIBOrderDetails> orders = connector.getOrderData(Collections.emptyList());
		assertNotNull(orders);
		assertEquals(0, orders.size());

	}

	@Test(expected = IllegalArgumentException.class)
	public void connector_default_constructor_should_return_impl() {
		Connector connector = new Connector(null);
	}

	@Test
	public void getOpenOrders_should_return_OrderList() {		
		Connector connector = new Connector(adapter);
		when(adapter.getOpenOrders(null)).thenReturn(orderList);
		List<OPIBOrderDetails> orders = connector.getOpenOrders(null);
		assertNotNull(orders);
		assertEquals(2, orders.size());
		verify(adapter).getOpenOrders(null);
	}

}
