package com.kpn.opib.bam.status.update.sync;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.batch.api.AbstractBatchlet;
import javax.inject.Inject;
import javax.inject.Named;

import com.kpn.opib.bam.data.service.HeatMapNodeService;
import com.kpn.opib.bam.model.HeatMapNode;
import com.kpn.opib.bam.model.HeatMapStatus;
import com.kpn.opib.bam.model.NodeDetails;


/**
 * 
 * @author nikam500
 *
 */
@Named
public class HeatMapNodeStatusBatchelet extends AbstractBatchlet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private HeatMapNodeService heatMapNodeServices;

	@SuppressWarnings("unchecked")
	@Override
	public String process() throws Exception {
		List<HeatMapNode> heatMapNodes = getHeatMapNode();
		if (!heatMapNodes.isEmpty()) {
			checkURLStatus(getHeatMapNode());
		} else {

			List<NodeDetails> nodeDetails = getNodeDetailsList();
			checkFirstTimeURLStatus(nodeDetails);
		}
		return "END";
	}

	private List<NodeDetails> getNodeDetailsList() {
		return heatMapNodeServices.getNodeDetailsList();
	}

	private void checkFirstTimeURLStatus(List<NodeDetails> nodeDetails) {
		List<HeatMapNode> heatMapNodes = new ArrayList<HeatMapNode>();
		for (NodeDetails nodeDetails2 : nodeDetails) {
			HeatMapNode node = new HeatMapNode();
			try {
				node = URLStatusChecker.isAlive(new URL(nodeDetails2.getUrl()), node);
			} catch (MalformedURLException e) {
				if (node.getHttpCode() == null)
					node.setHttpCode("400");
				node.setStatus(HeatMapStatus.UNKNOWN);
			} catch (Exception e) {
				if (node.getHttpCode() == null)
					node.setHttpCode("400");
				node.setStatus(HeatMapStatus.UNKNOWN);
			}
			node.setNodeDetails(nodeDetails2);
			heatMapNodes.add(node);
		}
		if (!heatMapNodes.isEmpty()) {
			heatMapNodeServices.updateHeatMapNode(heatMapNodes);
		}
	}

	private List<HeatMapNode> getHeatMapNode() {
		return heatMapNodeServices.getHeatMapData();

	}

	private void checkURLStatus(List<HeatMapNode> heatMapNodes) {

		List<HeatMapNode> heatMapNewList = new ArrayList<HeatMapNode>();
		for (HeatMapNode heatMapNode : heatMapNodes) {
			try {
				heatMapNode = URLStatusChecker.isAlive(new URL(heatMapNode.getNodeDetails().getUrl()), heatMapNode);
			} catch (MalformedURLException e) {
				if (heatMapNode.getHttpCode() == null)
					heatMapNode.setHttpCode("400");
				heatMapNode.setStatus(HeatMapStatus.UNKNOWN);
			} catch (Exception e) {
				if (heatMapNode.getHttpCode() == null)
					heatMapNode.setHttpCode("400");
				heatMapNode.setStatus(HeatMapStatus.UNKNOWN);
			}

			heatMapNode.setTimeStamp(new Date());
			heatMapNewList.add(heatMapNode);
		}

		heatMapNodeServices.updateHeatMapNode(heatMapNewList);
	}

}
