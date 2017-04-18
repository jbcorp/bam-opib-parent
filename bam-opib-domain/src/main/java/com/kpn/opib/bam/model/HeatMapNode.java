package com.kpn.opib.bam.model;
/**
 * 
 * @author nikam500
 *
 */

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "OPIB_BAM_HEATMAP")
@NamedQueries(value = {
		@NamedQuery(name = HeatMapNode.getAllheatMapChannel, query = "SELECT h1 FROM HeatMapNode h1 WHERE "
				+ "h1.timeStamp = (SELECT max(h2.timeStamp) FROM HeatMapNode h2 WHERE h2.nodeDetails = h1.nodeDetails)") })
public class HeatMapNode implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "N_ID", nullable = false, unique = true)
	@JsonIgnore
	private Long nID;

	@Enumerated(EnumType.STRING)
	@Column(name = "NODE_STATUS")
	private HeatMapStatus status;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP", nullable = false)
	private Date timeStamp;

	@Column(name = "STATUS_CODE", nullable = false)
	private String httpCode;

	@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "NODE", referencedColumnName = "NODE_ID")
	private NodeDetails nodeDetails;

	@Transient
	private ColorCode colorCode;
	
	@Transient
	private String appType;
	
	@Transient
	private String nodeID;
	
	@Transient
	private String chainStatus;

	public NodeDetails getNodeDetails() {
		return nodeDetails;
	}

	public void setNodeDetails(NodeDetails nodeDetails) {
		this.nodeDetails = nodeDetails;
	}

	public static final String getAllheatMapChannel = "HeatMapNode.getAllheatMapChannel";

	public HeatMapStatus getStatus() {
		return status;
	}

	public void setStatus(HeatMapStatus status) {
		this.status = status;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}


	public ColorCode getColorCode() {
		return colorCode;
	}

	public void setColorCode(ColorCode colorCode) {
		this.colorCode = colorCode;
	}

	public Long getnID() {
		return nID;
	}

	public void setnID(Long nID) {
		this.nID = nID;
	}
	
	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(String httpCode) {
		this.httpCode = httpCode;
	}

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public String getChainStatus() {
		return chainStatus;
	}

	public void setChainStatus(String chainStatus) {
		this.chainStatus = chainStatus;
	}

}
