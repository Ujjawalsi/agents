package com.example.agents.thousandeye.itadmin.bean;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DnacEndpoint {

	@JsonProperty("client_health")
	private String client_health = "True";
	@JsonProperty("ap_health")
	private String ap_health = "True";
	@JsonProperty("issueTime")
	private String issueTime = "True";
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	public String getClient_health() {
		return client_health;
	}
	public void setClient_health(String client_health) {
		this.client_health = client_health;
	}

	public String getAp_health() {
		return ap_health;
	}
	public void setAp_health(String ap_health) {
		this.ap_health = ap_health;
	}
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	@JsonProperty("issueTime")
	public String getIssueTime() {
		return issueTime;
	}
	@JsonProperty("issueTime")
	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}
	
}
