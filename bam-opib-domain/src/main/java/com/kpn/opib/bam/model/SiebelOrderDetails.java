package com.kpn.opib.bam.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SiebelOrderDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "SIEBEL_STATUS")
	private String siebelStatus; // TODO enum

	private float revenue; // TODO

	public String getSiebelStatus() {
		return siebelStatus;
	}

	public void setSiebelStatus(String siebelStatus) {
		this.siebelStatus = siebelStatus;
	}

	public float getRevenue() {
		return revenue;
	}

	public void setRevenue(float revenue) {
		this.revenue = revenue;
	}

	@Override
	public String toString() {
		return "SiebelOrderDetails [siebelStatus=" + siebelStatus + ", revenue=" + revenue + "]";
	}

}
