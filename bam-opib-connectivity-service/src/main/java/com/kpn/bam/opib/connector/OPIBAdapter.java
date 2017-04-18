package com.kpn.bam.opib.connector;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.kpn.opib.bam.model.OPIBOrderDetails;

public interface OPIBAdapter {

	public List<OPIBOrderDetails> getOrders(List<String> orderIds);

	public default List<OPIBOrderDetails> getOpenOrders(Date onOrAfterCreationDate) {
		return Collections.emptyList();
	}

}
