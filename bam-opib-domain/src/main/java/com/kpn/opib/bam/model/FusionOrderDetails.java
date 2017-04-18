package com.kpn.opib.bam.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Embeddable
public class FusionOrderDetails implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Enumerated(EnumType.STRING)
	@Column(name="FUSION_STATUS")
	@NotNull
	private FusionStatus fusionStatus;

	public FusionStatus getFusionStatus() {
		return fusionStatus;
	}

	public void setFusionStatus(FusionStatus fusionStatus) {
		this.fusionStatus = fusionStatus;
	}
	
	
	
}
