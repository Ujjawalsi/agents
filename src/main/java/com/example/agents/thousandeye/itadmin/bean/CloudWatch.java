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
	"memory_utilization"
})
@Generated("jsonschema2pojo")
public class CloudWatch {

	@JsonProperty("cpu_utilization")
	private String cpuUtilization = "True";
	@JsonProperty("memory_utilization")
	private String memoryUtilization = "98.7";
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

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
	
	public CloudWatch(String cpu_utilization,
			String memory_utilization) {
		this.cpuUtilization=null;
		this.memoryUtilization=null;
		// TODO Auto-generated constructor stub
	}
	
	public CloudWatch() {
		// TODO Auto-generated constructor stub
	}

}
