package com.kpn.opib.bam.status.update.sync;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.kpn.opib.bam.model.HeatMapNode;
import com.kpn.opib.bam.model.HeatMapStatus;



public class URLStatusChecker {
	static Map<String, Integer> countMap = new HashMap<String, Integer>();

	public static HeatMapNode isAlive(URL url, HeatMapNode heatMapNode) {
		return isAlive(url, null, heatMapNode);
	}

	public static HeatMapNode isAlive(URL url, Proxy proxy, HeatMapNode heatMapNode) {
		boolean isAlive;
		HttpURLConnection urlCon = null;
		System.setProperty("https.protocols", "TLSv1");
		System.setProperty("http.nonProxyHosts", "swnc7r065.kpnnl.local");

		try {
			if (null != proxy) {
				urlCon = (HttpURLConnection) url.openConnection(proxy);
			} else {
				urlCon = (HttpURLConnection) url.openConnection();
			}
			urlCon.setReadTimeout(20000);
			urlCon.setConnectTimeout(20000);
			urlCon.connect();
			Integer httpCode = urlCon.getResponseCode();
			heatMapNode.setHttpCode(httpCode.toString());

			System.out.println("Connecting to Url " + urlCon.getURL() + " Got HTTP Code " + httpCode);
			if (httpCode == HttpURLConnection.HTTP_OK) {
				if (heatMapNode.getHttpCode() == null)
					heatMapNode.setHttpCode("200");
				heatMapNode.setStatus(HeatMapStatus.OK);
				isAlive = true;
			} else {
				if (countMap.containsKey(url.toString())) {
					countMap.put(url.toString(), ((Integer) countMap.get(url.toString())) + 1);
				} else {
					countMap.put(url.toString(), 0);
				}
				if (((Integer) countMap.get(url.toString())) < 3) {
					TimeUnit.SECONDS.sleep(1);
					System.out.println(
							"Attempt Made to connect for Failed URL is : " + (countMap.get(url.toString()).intValue()+1));
					URLStatusChecker.isAlive(url, heatMapNode);
				}
				
				if (heatMapNode.getHttpCode() == null)
					heatMapNode.setHttpCode("404");
				heatMapNode.setStatus(HeatMapStatus.NOTOK);
				isAlive = false;

			}

		} catch (Exception e) {
			heatMapNode.setHttpCode("400");
			heatMapNode.setStatus(HeatMapStatus.UNKNOWN);
			e.printStackTrace();
			isAlive = false;
		} finally {
			if (null != urlCon) {
				urlCon.disconnect();

			}
		}
		countMap.clear();
		return heatMapNode;
	}

}
