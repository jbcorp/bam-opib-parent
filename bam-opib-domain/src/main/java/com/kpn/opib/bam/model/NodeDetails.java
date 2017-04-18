package com.kpn.opib.bam.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "NODE_DETAILS")
@NamedQueries(value = { @NamedQuery(name = NodeDetails.getAllNode, query = "select o from NodeDetails o ") })
public class NodeDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String getAllNode = "NodeDetails.getAllNode";

	@Id
	@Column(name = "NODE_ID", nullable = false, unique = true)
	private String nodeID;

	@Column(name = "URL", nullable = false, unique = true)
	private String url;

	@Column(name = "CHAINSTATUS", nullable = false)
	private String chainStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "SYS_NAME", nullable = false)
	private SystemName sysName;

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getChainStatus() {
		return chainStatus;
	}

	public void setChainStatus(String chainStatus) {
		this.chainStatus = chainStatus;
	}

	public SystemName getAppType() {
		return sysName;
	}

	public void setAppType(SystemName appType) {
		this.sysName = appType;
	}

}
