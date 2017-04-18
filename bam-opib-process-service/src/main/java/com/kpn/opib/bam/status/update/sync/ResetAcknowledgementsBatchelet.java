package com.kpn.opib.bam.status.update.sync;

import javax.batch.api.AbstractBatchlet;
import javax.inject.Inject;
import javax.inject.Named;

import com.kpn.opib.bam.data.service.BAMOrderDataService;

@Named
public class ResetAcknowledgementsBatchelet extends AbstractBatchlet {

	private BAMOrderDataService bamOrderDataService;

	@Inject
	public ResetAcknowledgementsBatchelet(BAMOrderDataService bamOrderDataService) {
		this.bamOrderDataService = bamOrderDataService;
	}

	public ResetAcknowledgementsBatchelet() {
		// Default for JavaEE
	}

	@Override
	public String process() throws Exception {
		bamOrderDataService.resetAcknowledgments();
		return null;
	}

}
