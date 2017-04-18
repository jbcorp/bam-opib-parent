package com.kpn.opib.bam.model;

import javax.persistence.Entity;

import com.kpn.opib.bam.framework.BaseEntity;

@Entity
public class Customer extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	private String ckrNumber;
	
	private String accountName;
	
	public String getCkrNumber() {
		return ckrNumber;
	}
	public void setCkrNumber(String ckrNumber) {
		this.ckrNumber = ckrNumber;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	

}
