package com.kpn.opib.bam.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.kpn.opib.bam.framework.BaseEntity;


@Entity
@Table(name="COM_FUNCTIONAL_PRODUCT")
public class FunctionalProduct extends BaseEntity
{
	
	@Column(name = "COM_ORDER_ID")
	String comOrderId;
	
	@Column(name = "FUNC_PROD_STATUS")
	@Enumerated(EnumType.STRING)
	FunctionalProductStatus functionalProductStatus; 
	
	@Column(name = "FUNC_PROD_TYPE")
	@Enumerated(EnumType.STRING)
	FunctionalProductType functionalProductType;
	
	@Column(name = "ACTION")
	String action;
	
	@Column(name = "IDENTIFIER")
	String identifier;
	
	@Column(name = "SNI_ORDER_ID")
	String sniOrderId;
	
	@Column(name = "SNI_INPUT")
	@Lob
	String sniInputXml;
	
	@Column(name = "SNI_OUTPUT")
	String sniOutputXml;
	
	@Column(name="ORDER_LINE_ID")
	String orderLineId;
	
	public String getComOrderId() {
		return comOrderId;
	}
	public void setComOrderId(String comOrderId) {
		this.comOrderId = comOrderId;
	}
	public FunctionalProductStatus getFunctionalProductStatus() {
		return functionalProductStatus;
	}
	public void setFunctionalProductStatus(FunctionalProductStatus functionalProductStatus) {
		this.functionalProductStatus = functionalProductStatus;
	}
	public FunctionalProductType getFunctionalProductType() {
		return functionalProductType;
	}
	public void setFunctionalProductType(FunctionalProductType functionalProductType) {
		this.functionalProductType = functionalProductType;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getSniInputXml() {
		return sniInputXml;
	}
	public void setSniInputXml(String sniInputXml) {
		this.sniInputXml = sniInputXml;
	}
	public String getSniOrderId() {
		return sniOrderId;
	}
	public void setSniOrderId(String sniOrderId) {
		this.sniOrderId = sniOrderId;
	}
	public String getSniOutputXml() {
		return sniOutputXml;
	}
	public void setSniOutputXml(String sniOutputXml) {
		this.sniOutputXml = sniOutputXml;
	}

	public String getOrderLineId() {
		return orderLineId;
	}
	public void setOrderLineId(String orderLineId) {
		this.orderLineId = orderLineId;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((comOrderId == null) ? 0 : comOrderId.hashCode());
		result = prime * result + ((orderLineId == null) ? 0 : orderLineId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if(null == obj)
			return false;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FunctionalProduct other = (FunctionalProduct) obj;
		if (comOrderId == null) {
			if (other.comOrderId != null)
				return false;
		} else if (!comOrderId.equals(other.comOrderId))
			return false;
		if (orderLineId == null) {
			if (other.orderLineId != null)
				return false;
		} else if (!orderLineId.equals(other.orderLineId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FunctionalProduct [comOrderId=" + comOrderId + ", functionalProductStatus=" + functionalProductStatus
				+ ", functionalProductType=" + functionalProductType + ", action=" + action + ", identifier="
				+ identifier + ", sniOrderId=" + sniOrderId + ", sniInputXml=" + sniInputXml + ", sniOutputXml="
				+ sniOutputXml + ", orderLineId=" + orderLineId + "]";
	}



	private static final long serialVersionUID = 7446646407166520212L;	
}
