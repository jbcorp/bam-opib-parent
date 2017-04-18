package com.kpn.opib.bam.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 * Domain class to store Order Data of OPIB Cordys application. 
 * 
 * @author gidwa500
 *
 */
@Embeddable
public class CordysOrderDetails implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Column(name="COM_ORDER_ID",unique=true)
	private String comOrderId;
	
	@Column(name="DERIVED_CORDYS_ORDER_STATUS")
	@Enumerated(EnumType.STRING)
	DerivedCordysOrderStatus cordysDerivedStatus;	
	//TODO Implement this. 
	
	@Column(name="CORDYS_ORDER_STATUS")
	@Enumerated(EnumType.STRING)
	private CordysOrderStatus cordysStatus;
	
	@OneToMany(orphanRemoval = false, cascade = { CascadeType.ALL }, fetch=FetchType.LAZY)
	private List<FunctionalProduct> functionalProducts;

	public String getComOrderId() {
		return comOrderId;
	}

	public void setComOrderId(String comOrderId) {
		this.comOrderId = comOrderId;
	}	

	public CordysOrderStatus getCordysStatus() {
		return cordysStatus;
	}

	public void setCordysStatus(CordysOrderStatus cordysStatus) {
		this.cordysStatus = cordysStatus;
	}

	public List<FunctionalProduct> getFunctionalProducts() {
		return functionalProducts;
	}

	public void setFunctionalProducts(List<FunctionalProduct> functionalProducts) {
		this.functionalProducts = functionalProducts;
	}

	public DerivedCordysOrderStatus getCordysDerivedStatus() {
		return cordysDerivedStatus;
	}

	public void setCordysDerivedStatus(DerivedCordysOrderStatus cordysDerivedStatus) {
		this.cordysDerivedStatus = cordysDerivedStatus;
	}
		

	@Override
	public String toString() {
		return "CordysOrderDetails [comOrderId=" + comOrderId + ", cordysOrderStatus=" + cordysStatus
				+ ", cordysDerivedStatus=" + ", functionalProductList=" + functionalProducts + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comOrderId == null) ? 0 : comOrderId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CordysOrderDetails other = (CordysOrderDetails) obj;
		if (comOrderId == null) {
			if (other.comOrderId != null)
				return false;
		} else if (!comOrderId.equals(other.comOrderId))
			return false;
		return true;
	}

	
}
