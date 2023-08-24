package com.example.agents.reports.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
//import com.thosandeye.bean2.ApplicationPerformance;
//import com.thosandeye.bean2.NetworkPathToApplication;
//import com.thosandeye.bean2.EndpointPerformance;
//import com.thosandeye.bean2.GatewayConnectivity;
//import com.thosandeye.bean2.Issue;
//import com.thosandeye.bean2.ThousandEyeAlertBean;


public class ProcessResponseJSON {

//	private JSONObject processJSONData(JSONArray arrData, String agentName, String application) {
//		JSONObject rtnString = new JSONObject();
//		try {
//			JSONArray _jsonResponse = arrData;
//			JSONObject _final = new JSONObject();
//			ThousandEyeAlertBean bean = new ThousandEyeAlertBean();
//			EndpointPerformance endPoint = new EndpointPerformance();
//			ApplicationPerformance appPerf = new ApplicationPerformance();
//			NetworkPathToApplication networkApp = new NetworkPathToApplication();
//			GatewayConnectivity gatewayApp = new GatewayConnectivity();
//			List<Issue> issues = new ArrayList<Issue>();
//			for (int i=0; i<_jsonResponse.length(); i++) {
//				JSONArray issuesArr = new JSONArray();
//				JSONObject _jObj = _jsonResponse.getJSONObject(i);
//				JSONObject _alertJson =_jObj.getJSONObject("alert");
//				JSONArray _agentsArray = _alertJson.getJSONArray("agents");
//				System.out.println("_agentsArray: "+_agentsArray);
//				for (int j=0; j<_agentsArray.length(); j++) {
//					Issue issue = new Issue();
//					JSONObject _json = new JSONObject();
//					if(_agentsArray.getJSONObject(j).getString("agentName").equalsIgnoreCase(agentName)) {
//						//CPU/Memory/Client health/(Throughput)
//						String issueName = "";
//						String _startTime = "";
//						String _dateEnd = "";
//						if(_agentsArray.getJSONObject(j).has("metricsAtStart")) {
//							issueName = _agentsArray.getJSONObject(j).getString("metricsAtStart");
//							_startTime = _agentsArray.getJSONObject(j).getString("dateStart");
//
//							if(_jObj.has("dateEnd")) {
//								_dateEnd = _agentsArray.getJSONObject(j).getString("dateEnd");
//								_json.put("dateEnd", _dateEnd);
//							}else {
//								_json.put("dateEnd", "");
//							}
//						}else {
//							issueName = _alertJson.getString("ruleName");
//							_startTime =_alertJson.getString("dateStart");
//						}
//						//						Application Infrastructure	Endpoint	Network Path to Application	Gateway Connectivity
//
//						if( issueName.contains("CPU")|| issueName.contains("Memory") ||
//								issueName.contains("health")) {
//
//							issue.setStartTime(_startTime);
//							issue.setDateEnd(_dateEnd);
//							issue.setIssueName(issueName);
//							endPoint.setMessage("Endpoint for the given time is not performing optimally");
//							endPoint.set
//
//						}else if(issueName.contains("Packet Loss")) {
//							networkApp.setStartTime(_startTime);
//							networkApp.setDateEnd(_dateEnd);
//							networkApp.setIssueName("High Packet Loss");
//							networkApp.setMessage("Path for the given time is not performing optimally");
//							networkPathList.add(networkApp);
//						}else if(issueName.contains("Jitter")) {
//							networkApp.setStartTime(_startTime);
//							networkApp.setDateEnd(_dateEnd);
//							networkApp.setIssueName("High Jitter");
//							networkApp.setMessage("Path for the given time is not performing optimally");
//							networkPathList.add(networkApp);
//						}else if(issueName.contains("Latency")) {
//							networkApp.setStartTime(_startTime);
//							networkApp.setDateEnd(_dateEnd);
//							networkApp.setIssueName("High Latency");
//							networkApp.setMessage("Path for the given time is not performing optimally");
//							networkPathList.add(networkApp);
//						}
//						else if(issueName.contains("Throughput")) {
//							gatewayApp.setStartTime(_startTime);
//							gatewayApp.setDateEnd(_dateEnd);
//							gatewayApp.setIssueName("Low Throughput");
//							gatewayApp.setMessage("Gateway Connectivity for the given time is not performing optimally");
//							gatewayConnectList.add(gatewayApp);
//						}else if(issueName.contains("Page Load")) {
//							appPerf.setStartTime(_startTime);
//							appPerf.setDateEnd(_dateEnd);
//							appPerf.setIssueName("High Page Load Time");
//							appPerf.setMessage("Application for the given time is not performing optimally");
//							appPrfList.add(appPerf);
//						}
//					}
//				}
//			}
//			if(appPrfList.size()>0)
//				bean.setApplicationPerformance(appPrfList);
//			else {
//				ApplicationPerformance appPerf1 = new ApplicationPerformance();
//				appPerf1.setMessage("Application for the given time is Good.");
//				appPrfList.add(appPerf1);
//				bean.setApplicationPerformance(appPrfList);
//			}
//			if(endpointPrfList.size()>0)
//				bean.setEndpointPerformance(endpointPrfList);
//			else{
//				EndpointPerformance endPoint1 = new EndpointPerformance();
//				endPoint1.setMessage("EndPoint for the given time is Good.");
//				endpointPrfList.add(endPoint1);
//				bean.setEndpointPerformance(endpointPrfList);
//			}
//			if(gatewayConnectList.size()>0)
//				bean.setGatewayConnectivity(gatewayConnectList);
//			else{
//				GatewayConnectivity gateway = new GatewayConnectivity();
//				gateway.setMessage("Gateway for the given time is Good");
//				gatewayConnectList.add(gateway);
//				bean.setGatewayConnectivity(gatewayConnectList);
//			}
//			if(networkPathList.size()>0)
//				bean.setNetworkPathToApplication(networkPathList);
//			else{
//				NetworkPathToApplication nwpath = new NetworkPathToApplication();
//				nwpath.setMessage("Path for the given time is Good");
//				networkPathList.add(nwpath);
//				bean.setNetworkPathToApplication(networkPathList);
//			}
//			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//			String json = ow.writeValueAsString(bean);
//			System.out.println("JSON: "+json);
//			rtnString = new JSONObject(json);
//			System.out.println("final JSON Reponse: "+rtnString.toString());
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//		return rtnString;
//	}

}
