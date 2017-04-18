package com.kpn.opib.bam.batch.rest;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.batch.runtime.JobExecution;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.kpn.bam.opib.utils.BatchConstants;
import com.kpn.opib.bam.data.service.HeatMapNodeService;
import com.kpn.opib.bam.framework.BatchExecution;
import com.kpn.opib.bam.model.BatchDetails;
import com.kpn.opib.bam.model.ColorCode;
import com.kpn.opib.bam.model.HeatMapNode;
import com.kpn.opib.bam.model.HeatMapStatus;
import com.kpn.opib.bam.process.service.BatchService;

@Path("/heatMap")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class HeatMapController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	HeatMapNodeService channelService;

	@Inject
	BatchService batchService;

	@GET
	@Path("/HeatMapDetails")
	@Produces("application/json")
	public Response getHeatMapStatus() {
		//TODO remove sysout once logger is added
		System.out.println("IN The HeatMap Controller");
		List<HeatMapNode> listOfHeatMap = channelService.getHeatMapData();
		for (HeatMapNode heatMapNode : listOfHeatMap) {
			if (heatMapNode.getStatus().equals(HeatMapStatus.NOTOK)) {
				heatMapNode.setColorCode(ColorCode.RED);
				heatMapNode.setAppType(heatMapNode.getNodeDetails().getAppType().toString());
				heatMapNode.setNodeID(heatMapNode.getNodeDetails().getNodeID());
			} else if (heatMapNode.getStatus().equals(HeatMapStatus.OK)) {
				heatMapNode.setAppType(heatMapNode.getNodeDetails().getAppType().toString());
				heatMapNode.setColorCode(ColorCode.GREEN);
				heatMapNode.setNodeID(heatMapNode.getNodeDetails().getNodeID());
			} else {
				heatMapNode.setAppType(heatMapNode.getNodeDetails().getAppType().toString());
				heatMapNode.setColorCode(ColorCode.YELLOW);
				heatMapNode.setNodeID(heatMapNode.getNodeDetails().getNodeID());
			}
		}
		return (!listOfHeatMap.isEmpty()
				? Response.status(Status.OK).header("Access-Control-Allow-Origin", "*")
						.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
						.header("Access-Control-Allow-Credentials", "true")
						.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
						.header("Access-Control-Max-Age", "1209600").entity(listOfHeatMap).build()
				: Response.status(Status.NOT_FOUND).build());

	}

	@GET
	@Path("/bam-sync-heatmap")
	@Produces("application/json")
	public Response startBamSyncBatch() {
		System.out.println("Testing Batch");
		String jobName = BatchConstants.OPIB_BAM_HEATMAP_SYNC_JOB;
		Properties jobParameters = new Properties();
		BatchDetails batchDetails = batchService.startBatchJob(jobName, jobParameters);
		if (batchDetails.isConflict()) {
			System.out.println("Resource not modified");
			return Response.status(Status.CONFLICT)
					.entity(batchExecutionResponseMapper(batchService.getJobExecution(batchDetails.getExecutionId())))
					.build();
		} else {
			return Response.status(Status.CREATED)
					.entity(batchExecutionResponseMapper(batchService.getJobExecution(batchDetails.getExecutionId())))
					.build();
		}

	}

	private BatchExecution batchExecutionResponseMapper(JobExecution execution) {
		BatchExecution batchExecution = new BatchExecution();
		batchExecution.setExecutionId(execution.getExecutionId());
		batchExecution.setJobName(execution.getJobName());
		batchExecution.setBatchStatus(execution.getBatchStatus());
		batchExecution.setCreateTime(execution.getCreateTime());
		batchExecution.setCreateTime(execution.getEndTime());
		batchExecution.setExitStatus(execution.getExitStatus());
		return batchExecution;
	}
}
