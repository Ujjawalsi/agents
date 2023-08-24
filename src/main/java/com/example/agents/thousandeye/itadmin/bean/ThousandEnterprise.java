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
	"pageloadtime",
	"latency",
	"loss",
	"jitter"
})
@Generated("jsonschema2pojo")
public class ThousandEnterprise {

	@JsonProperty("pageloadtime")
	private String pageloadtime = "True";
	@JsonProperty("latency")
	private String latency = "True";
	@JsonProperty("loss")
	private String loss = "True";
	@JsonProperty("jitter")
	private String jitter = "True";
	@JsonProperty("throughput")
	private String throughput = "True";
	@JsonProperty("issueTime")
	private String issueTime = "True";
	@JsonProperty("responseTime")
	private String responseTime = "True";
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("pageloadtime")
	public String getPageloadtime() {
		return pageloadtime;
	}

	@JsonProperty("pageloadtime")
	public void setPageloadtime(String pageloadtime) {
		this.pageloadtime = pageloadtime;
	}

	@JsonProperty("latency")
	public String getLatency() {
		return latency;
	}

	@JsonProperty("latency")
	public void setLatency(String latency) {
		this.latency = latency;
	}

	@JsonProperty("loss")
	public String getLoss() {
		return loss;
	}

	@JsonProperty("loss")
	public void setLoss(String loss) {
		this.loss = loss;
	}

	@JsonProperty("jitter")
	public String getJitter() {
		return jitter;
	}

	@JsonProperty("jitter")
	public void setJitter(String jitter) {
		this.jitter = jitter;
	}
	
	@JsonProperty("throughput")
	public String getThroughput() {
		return throughput;
	}
	@JsonProperty("throughput")
	public void setThroughput(String throughput) {
		this.throughput = throughput;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
	@JsonProperty("issueTime")
	public String getIssueTime() {
		return issueTime;
	}
	@JsonProperty("issueTime")
	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
}

