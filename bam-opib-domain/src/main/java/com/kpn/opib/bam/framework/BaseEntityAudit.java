package com.kpn.opib.bam.framework;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class BaseEntityAudit extends BaseEntity
{

	private static final long serialVersionUID = 1L;

	@Column(name = "row_creation_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	//@Size(max = 20)
	@Column(name = "row_created_by", length = 20)
	private String createdBy;

	@Column(name = "row_updation_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	//@Size(max = 20)
	@Column(name = "row_updated_by", length = 20)
	private String updatedBy;

	/**
	 * Sets createdAt before insert
	 */
	@PrePersist
	public void setCreationDate()
	{
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}

	/**
	 * Sets updatedAt before update
	 */
	@PreUpdate
	public void setChangeDate()
	{
		this.updatedAt = new Date();
	}

	public Date getCreatedAt()
	{
		return createdAt;
	}

	public void setCreatedAt(Date createdAt)
	{
		this.createdAt = createdAt;
	}

	public String getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(String createdBy)
	{
		this.createdBy = createdBy;
	}

	public Date getUpdatedAt()
	{
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt)
	{
		this.updatedAt = updatedAt;
	}

	public String getUpdatedBy()
	{
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy)
	{
		this.updatedBy = updatedBy;
	}

}
