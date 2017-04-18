package com.kpn.opib.bam.framework;
import java.util.Date;

import javax.batch.runtime.BatchStatus;

public class BatchExecution {

	private long executionId;
	
	private String jobName;
	
	private BatchStatus batchStatus;
	
	private Date createTime;
	
	private String endTime;
	
	private String exitStatus;
	
	
	public long getExecutionId() {
		return executionId;
	}
	public void setExecutionId(long executionId) {
		this.executionId = executionId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public BatchStatus getBatchStatus() {
		return batchStatus;
	}
	public void setBatchStatus(BatchStatus batchStatus) {
		this.batchStatus = batchStatus;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getExitStatus() {
		return exitStatus;
	}
	public void setExitStatus(String exitStatus) {
		this.exitStatus = exitStatus;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	
	

}
