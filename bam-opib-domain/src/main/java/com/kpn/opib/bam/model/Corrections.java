package com.kpn.opib.bam.model;



import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Embeddable
public class Corrections implements Serializable {
		

	@Column(name = "ACKNOWLEDGED_SIEBEL")
	private boolean siebelAcknowledged;

	@Column(name = "ACKNOWLEDGED_CORDYS")
	private boolean cordysAcknowledged;

	@Column(name = "ACKNOWLEDGED_FUSION")
	private boolean fusionAcknowledged;

	@Column(name = "CORRECTED_SIEBEL")
	private boolean siebelCorrected;

	@Column(name = "CORRECTED_CORDYS")
	private boolean cordysCorrected;

	@Column(name = "CORRECTED_FUSION")
	private boolean fusionCorrected;

	@Column(name = "CORRECTION_ETA")
	private String eta;

	@Column(name = "CORRECTION_WORKAROUND")
	private String workaround;

	@Column(name = "CORRECTION_COMMENT")
	private String comment;
	
	@Column(name = "CORRECTION_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date correctionTime;
	
	@Transient
	boolean changed;
	
	private static final long serialVersionUID = 1L;

	public boolean isSiebelAcknowledged() {
		return siebelAcknowledged;
	}

	public void setSiebelAcknowledged(boolean siebelAcknowledged) {
		this.siebelAcknowledged = siebelAcknowledged;
	}

	public boolean isCordysAcknowledged() {
		return cordysAcknowledged;
	}

	public void setCordysAcknowledged(boolean cordysAcknowledged) {
		this.cordysAcknowledged = cordysAcknowledged;
	}

	public boolean isFusionAcknowledged() {
		return fusionAcknowledged;
	}

	public void setFusionAcknowledged(boolean fusionAcknowledged) {
		this.fusionAcknowledged = fusionAcknowledged;
	}

	public boolean isSiebelCorrected() {
		return siebelCorrected;
	}

	public void setSiebelCorrected(boolean siebelCorrected) {
		this.siebelCorrected = siebelCorrected;
	}

	public boolean isCordysCorrected() {
		return cordysCorrected;
	}

	public void setCordysCorrected(boolean cordysCorrected) {
		this.cordysCorrected = cordysCorrected;
	}

	public boolean isFusionCorrected() {
		return fusionCorrected;
	}

	public void setFusionCorrected(boolean fusionCorrected) {
		this.fusionCorrected = fusionCorrected;
	}

	public String getEta() {
		return eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getWorkaround() {
		return workaround;
	}

	public void setWorkaround(String workaround) {
		this.workaround = workaround;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public Date getCorrectionTime() {
		return correctionTime;
	}

	public void setCorrectionTime(Date correctionTime) {
		this.correctionTime = correctionTime;
	}
	
	

}
