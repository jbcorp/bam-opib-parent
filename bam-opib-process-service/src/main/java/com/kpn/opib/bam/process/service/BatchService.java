package com.kpn.opib.bam.process.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.batch.operations.NoSuchJobException;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpn.opib.bam.model.BatchDetails;

@Stateless
public class BatchService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public BatchDetails startBatchJob(String jobName, Properties jobParameters) {
		BatchDetails batchDetails = new BatchDetails();
		List<Long> executionIds;
		executionIds = getRunningExecutions(jobName);
		boolean conflict = true;

		if (executionIds.isEmpty()) {
			conflict = false;
			executionIds.add(startJobExecution(jobName, jobParameters));
		}
		batchDetails.setExecutionId(executionIds.get(0));
		batchDetails.setConflict(conflict);
		return batchDetails;
	}

	public JobExecution getJobExecution(long executionId) {
		return BatchRuntime.getJobOperator().getJobExecution(executionId);
	}

	public List<Long> getRunningExecutions(String jobName) {
		List<Long> executions = new ArrayList<Long>();
		try {
			executions = BatchRuntime.getJobOperator().getRunningExecutions(jobName);
		} catch (NoSuchJobException ne) {
			logger.info("No running execution found for batch " + jobName + ne);
		}
		return executions;
	}

	private long startJobExecution(String jobName, Properties jobParameters) {
		return BatchRuntime.getJobOperator().start(jobName, jobParameters);
	}

	public JobExecution getLastBatchExecution(String jobName) {
		
		try
		{
			int numberOfInstance = BatchRuntime.getJobOperator().getJobInstanceCount(jobName);
			logger.info("Number of instances found for batch " + jobName + " " + numberOfInstance);

			if (numberOfInstance < 1) {
				return null;
			}

			List<JobInstance> jobInstanceList = BatchRuntime.getJobOperator().getJobInstances(jobName, 0,
					numberOfInstance + 1);

			if (null != jobInstanceList && !jobInstanceList.isEmpty()) {
				
				List<JobExecution> executions = BatchRuntime.getJobOperator().getJobExecutions(jobInstanceList.get(0));

				if (null != executions && !executions.isEmpty()) {
					logger.info("Last Execution Id " + executions.get(0).getExecutionId());
					logger.info("Last Execution End time " + executions.get(0).getEndTime());
					return executions.get(0);
				}
			}

			return null;
		}
		catch(NoSuchJobException ne )
		{
			logger.error("No Job found " + ne);
			return null;
		}
		
	}

}
