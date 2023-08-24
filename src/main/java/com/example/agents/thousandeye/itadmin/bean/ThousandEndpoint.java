package com.example.agents.thousandeye.itadmin.bean;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"cpu_utilization",
	"memory_utilization",
	"signal_quality",
	"throughput",
	"pageloadtime",
	"jitter",
	"latency",
	"packetLoss",
	"error",
	"issueTime"
})
@Generated("jsonschema2pojo")
public class ThousandEndpoint {

	@JsonProperty("cpu_utilization")
	private String cpuUtilization = "True";
	@JsonProperty("memory_utilization")
	private String memoryUtilization = "True";
	@JsonProperty("signal_quality")
	private String signalQuality = "True";
	@JsonProperty("throughput")
	private String throughput = "True";
	@JsonProperty("pageloadtime")
	private String pageloadtime = "True";
	@JsonProperty("jitter")
	private String jitter = "True";
	@JsonProperty("latency")
	private String latency = "True";
	@JsonProperty("packetLoss")
	private String packetLoss = "True";
	@JsonProperty("error")
	private String error = "True";
	@JsonProperty("issueTime")
	private String issueTime = "True";
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("cpu_utilization")
	public String getCpuUtilization() {
		return cpuUtilization;
	}

	@JsonProperty("cpu_utilization")
	public void setCpuUtilization(String cpuUtilization) {
		this.cpuUtilization = cpuUtilization;
	}

	@JsonProperty("memory_utilization")
	public String getMemoryUtilization() {
		return memoryUtilization;
	}

	@JsonProperty("memory_utilization")
	public void setMemoryUtilization(String memoryUtilization) {
		this.memoryUtilization = memoryUtilization;
	}

	@JsonProperty("signal_quality")
	public String getSignalQuality() {
		return signalQuality;
	}

	@JsonProperty("signal_quality")
	public void setSignalQuality(String signalQuality) {
		this.signalQuality = signalQuality;
	}

	@JsonProperty("throughput")
	public String getThroughput() {
		return throughput;
	}

	@JsonProperty("throughput")
	public void setThroughput(String throughput) {
		this.throughput = throughput;
	}

	@JsonProperty("pageloadtime")
	public String getPageloadtime() {
		return pageloadtime;
	}

	@JsonProperty("pageloadtime")
	public void setPageloadtime(String pageloadtime) {
		this.pageloadtime = pageloadtime;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
	@JsonProperty("jitter")
	public String getJitter() {
		return jitter;
	}
	@JsonProperty("jitter")
	public void setJitter(String jitter) {
		this.jitter = jitter;
	}
	@JsonProperty("latency")
	public String getLatency() {
		return latency;
	}
	@JsonProperty("latency")
	public void setLatency(String latency) {
		this.latency = latency;
	}
	@JsonProperty("packetLoss")
	public String getPacketLoss() {
		return packetLoss;
	}
	@JsonProperty("packetLoss")
	public void setPacketLoss(String packetLoss) {
		this.packetLoss = packetLoss;
	}
	@JsonProperty("error")
	public String getError() {
		return error;
	}
	@JsonProperty("error")
	public void setError(String error) {
		this.error = error;
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

