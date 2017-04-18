package com.kpn.opib.bam.model;

import java.io.Serializable;

public class Link implements Serializable{

	
	private static final long serialVersionUID = 1L;

	private String url;
	
	private String rel;
	
	public static final String LINK_REL_SELF = "self";
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	
	

}
