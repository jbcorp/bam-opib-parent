package com.kpn.opib.bam.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.kpn.opib.bam.framework.BaseEntityAudit;

/**
 * This class represent the OPIB order domain object.
 * Contains data of OneCRM(Siebel), Fusion and Cordys 
 * 
 * @author gidwa500
 *
 */
@Entity
@Table(name = "OPIB_ORDER_DETAILS")
@NamedQueries(value = {
		@NamedQuery(name = OPIBOrderDetails.FIND_BY_CRM_ORDER_ID, query = "select o from OPIBOrderDetails o where o.siebelOrderId = :siebelOrderId "),		
		@NamedQuery(name = OPIBOrderDetails.FIND_ORDERS_NOT_FINAL, query = "select o from OPIBOrderDetails o where o.siebelOrderDetails.siebelStatus <> 'Final' "),
		@NamedQuery(name = OPIBOrderDetails.FIND_CORRECTED_ORDERS, query = "select o from OPIBOrderDetails o where o.corrections.siebelCorrected = true or o.corrections.cordysCorrected = true or o.corrections.fusionCorrected = true "),
		@NamedQuery(name = OPIBOrderDetails.FIND_ORDER_IDS_NOT_FINAL, query = "select o.siebelOrderId from OPIBOrderDetails o where o.siebelOrderDetails.siebelStatus <> 'Final' "),
		@NamedQuery(name = OPIBOrderDetails.RESET_CORDYS_ACK, query = "update OPIBOrderDetails O SET O.corrections.cordysAcknowledged = false , O.corrections.cordysCorrected = true where O.cordysOrderDetails.cordysStatus in ('COMPLETE') and (O.corrections.cordysAcknowledged = true) "),
		@NamedQuery(name = OPIBOrderDetails.RESET_SIEBEL_ACK, query = "update OPIBOrderDetails O SET O.corrections.siebelAcknowledged = false , O.corrections.siebelCorrected = true where O.siebelOrderDetails.siebelStatus in ('FINAL') and O.corrections.siebelAcknowledged = true "),
		@NamedQuery(name = OPIBOrderDetails.RESET_FUSION_ACK, query = "update OPIBOrderDetails O SET O.corrections.fusionAcknowledged = false , O.corrections.fusionCorrected = true where O.fusionOrderDetails.fusionStatus NOT IN  ('ERROR') and O.corrections.fusionAcknowledged = true"),
		@NamedQuery(name = OPIBOrderDetails.UPDATE_FUSION_STATUS, query = "update OPIBOrderDetails O SET O.fusionOrderDetails.fusionStatus = :fusionStatus WHERE O.siebelOrderId = :siebelOrderId ")})
public class OPIBOrderDetails extends BaseEntityAudit {

	@Column(name = "SIEBEL_ORDER_ID", nullable = false, unique = true)
	@NotNull
	private String siebelOrderId;

	@Column(name = "SIEBEL_ORDER_NUMBER", nullable = false, unique = true)
	private String siebelOrderNumber;

	@OneToOne(cascade = CascadeType.ALL)
	private Customer customer; // TODO

	@Enumerated(EnumType.STRING)
	@Column(name = "ORDER_TYPE", nullable = false)
	@NotNull
	private OrderType orderType; 

	@Column(name = "SIEBEL_CREATION_DATE", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date siebelCreationDate;

	@Embedded
	private SiebelOrderDetails siebelOrderDetails;

	@Embedded
	private FusionOrderDetails fusionOrderDetails;

	@Embedded
	private CordysOrderDetails cordysOrderDetails;
	
	@Embedded
	@NotNull
	private Corrections corrections;
	
	@Transient
	private List<Link> links = new ArrayList<>();

	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_CRM_ORDER_ID = "OPIBOrderDetails.findByCrmOrderId";
	public static final String FIND_ORDERS_NOT_FINAL = "OPIBOrderDetails.findOrders_NotFinal";
	public static final String UPDATE_FUSION_STATUS = "OPIBOrderDetails.updateFusionStatus";
	public static final String FIND_ORDER_IDS_NOT_FINAL = "OPIBOrderDetails.findOrderIDS_NotFinal";
	public static final String RESET_CORDYS_ACK = "OPIBOrderDetails.resetCordysAck";
	public static final String RESET_SIEBEL_ACK = "OPIBOrderDetails.resetSiebelAck";
	public static final String RESET_FUSION_ACK = "OPIBOrderDetails.resetFusionAck";
	public static final String FIND_CORRECTED_ORDERS = "OPIBOrderDetails.findCorrectedOrders";
	
	

	public String getSiebelOrderId() {
		return siebelOrderId;
	}

	public void setSiebelOrderId(String siebelOrderId) {
		this.siebelOrderId = siebelOrderId;
	}

	public String getSiebelOrderNumber() {
		return siebelOrderNumber;
	}

	public void setSiebelOrderNumber(String siebelOrderNumber) {
		this.siebelOrderNumber = siebelOrderNumber;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public Date getSiebelCreationDate() {
		return siebelCreationDate;
	}

	public void setSiebelCreationDate(Date siebelCreationDate) {
		this.siebelCreationDate = siebelCreationDate;
	}
	

	public SiebelOrderDetails getSiebelOrderDetails() {
		return siebelOrderDetails;
	}

	public void setSiebelOrderDetails(SiebelOrderDetails siebelOrderDetails) {
		this.siebelOrderDetails = siebelOrderDetails;
	}

	public FusionOrderDetails getFusionOrderDetails() {
		return fusionOrderDetails;
	}

	public void setFusionOrderDetails(FusionOrderDetails fusionOrderDetails) {
		this.fusionOrderDetails = fusionOrderDetails;
	}

	public CordysOrderDetails getCordysOrderDetails() {
		return cordysOrderDetails;
	}

	public void setCordysOrderDetails(CordysOrderDetails cordysOrderDetails) {
		this.cordysOrderDetails = cordysOrderDetails;
	}
	

	public Corrections getCorrections() {
		return corrections;
	}

	public void setCorrections(Corrections corrections) {
		this.corrections = corrections;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((siebelOrderId == null) ? 0 : siebelOrderId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		OPIBOrderDetails other = (OPIBOrderDetails) obj;
		if (siebelOrderId == null) {
			if (other.siebelOrderId != null)
				return false;
		} else if (!siebelOrderId.equals(other.siebelOrderId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OPIBOrderDetails [siebelOrderId=" + siebelOrderId + ", siebelOrderNumber=" + siebelOrderNumber + ", customer="
				+ customer + ", orderType=" + orderType + ", siebelCreationDate=" + siebelCreationDate + ", correctionStatus="
				+ ", siebelOrderDetails=" + siebelOrderDetails + ", fusionOrderDetails="
				+ fusionOrderDetails + ", cordysOrderDetails=" + cordysOrderDetails + "]";
	}

}
