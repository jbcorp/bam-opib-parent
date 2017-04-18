// TODO Hateos implementation ->  Hateos link with atleast execution id of job to track status.
// TODO Single order sync. 

package com.kpn.opib.bam.batch.rest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.batch.runtime.JobExecution;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpn.bam.opib.utils.BatchConstants;
import com.kpn.opib.bam.framework.BatchExecution;
import com.kpn.opib.bam.model.BatchDetails;
import com.kpn.opib.bam.process.service.BatchService;

@Path("/process")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
/**
 * Rest Resource representing process (batch jobs).
 * 
 * @author mahaj503
 *
 */
public class ProcessResource {

	private BatchService batchService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ProcessResource() {

	}

	/**
	 * 
	 * @param batchService
	 *            - Instance of BatchService to performs operations related to
	 *            batch jobs.
	 */
	@Inject
	public ProcessResource(BatchService batchService) {
		this.batchService = batchService;
	}

	/**
	 * Get the details of any Running instance of BAM Sync job. If No process in
	 * running then returns 404.
	 * 
	 * @return Response containing job execution details (BatchExecution) or
	 *         404.
	 */
	@GET
	@Path("/bam-sync-process")
	public Response getBamSyncBatchRunningInstance() {

		List<Long> executionIds = batchService.getRunningExecutions(BatchConstants.OPIB_BAM_SYNC_JOB);

		return !executionIds.isEmpty()
				? Response.status(Status.OK)
						.entity(batchExecutionResponseMapper(batchService.getJobExecution(executionIds.get(0)))).build()
				: Response.status(Status.NOT_FOUND).build();
	}

	/**
	 * Starts the BAM Sync batch job if it is not already running.
	 * 
	 * @return Response containing job execution details (BatchExecution).
	 */
	@POST
	@Path("/bam-sync-process")
	public Response startBamSyncBatch() {

		String jobName = BatchConstants.OPIB_BAM_SYNC_JOB;
		Properties jobParameters = new Properties();
		BatchDetails batchDetails = batchService.startBatchJob(jobName, jobParameters);
		if (batchDetails.isConflict()) {
			logger.info(BatchConstants.OPIB_BAM_SYNC_JOB + " batch is already executing");
			return Response.status(Status.CONFLICT)
					.entity(batchExecutionResponseMapper(batchService.getJobExecution(batchDetails.getExecutionId())))
					.build();
		} else {
			logger.info(BatchConstants.OPIB_BAM_SYNC_JOB + " batch started");
			return Response.status(Status.CREATED)
					.entity(batchExecutionResponseMapper(batchService.getJobExecution(batchDetails.getExecutionId())))
					.build();
		}

	}

	@GET
	@Path("/bam-sync-process/last-sync-details")
	public Response getLastBatchExecution() {

		JobExecution lastExecution = batchService.getLastBatchExecution(BatchConstants.OPIB_BAM_SYNC_JOB);

		return null != lastExecution
				? Response.status(Status.OK).entity(batchExecutionResponseMapper(lastExecution)).build()
				: Response.status(Status.NOT_FOUND).build();
	}

	/**
	 * Maps Field required in Response object - Gets value from Framework
	 * provided JobExecution to Custom BatchExecution object. Custom Object is
	 * required to minimize output and stop recursion while marshalling Java
	 * Object to JSON.
	 * 
	 * @param execution
	 *            - JobExecution instance
	 * @return BatchExecution custom object
	 */
	private BatchExecution batchExecutionResponseMapper(JobExecution execution) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

		BatchExecution batchExecution = new BatchExecution();
		batchExecution.setExecutionId(execution.getExecutionId());
		batchExecution.setJobName(execution.getJobName());
		batchExecution.setBatchStatus(execution.getBatchStatus());
		batchExecution.setCreateTime(execution.getCreateTime());
		batchExecution.setCreateTime(execution.getEndTime());
		batchExecution.setExitStatus(execution.getExitStatus());

		if (null != execution.getEndTime()) {
			batchExecution.setEndTime(sdf.format(execution.getEndTime()));
		}

		return batchExecution;
	}

}
