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
	"vm_name",
	"vm_performance",
	"cpu_utilization",
	"memory_utilization",
	"vm_os",
	"vm_address",
	"time"
})
@Generated("jsonschema2pojo")
public class Vm {

	@JsonProperty("vm_name")
	private String vmName = "Vel_ERP";
	@JsonProperty("vm_performance")
	private String vmPerformance = "vSmart";
	@JsonProperty("cpu_utilization")
	private String cpuUtilization = "True";
	@JsonProperty("memory_utilization")
	private String memoryUtilization = "97.8%";
	@JsonProperty("vm_os")
	private String vmOs = "Ubuntu 20.04";
	@JsonProperty("vm_address")
	private String vmAddress = "14.140.8.71";
	@JsonProperty("time")
	private String time = "True";
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("vm_name")
	public String getVmName() {
		return vmName;
	}

	@JsonProperty("vm_name")
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	@JsonProperty("vm_performance")
	public String getVmPerformance() {
		return vmPerformance;
	}

	@JsonProperty("vm_performance")
	public void setVmPerformance(String vmPerformance) {
		this.vmPerformance = vmPerformance;
	}

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

	@JsonProperty("vm_os")
	public String getVmOs() {
		return vmOs;
	}

	@JsonProperty("vm_os")
	public void setVmOs(String vmOs) {
		this.vmOs = vmOs;
	}

	@JsonProperty("vm_address")
	public String getVmAddress() {
		return vmAddress;
	}

	@JsonProperty("vm_address")
	public void setVmAddress(String vmAddress) {
		this.vmAddress = vmAddress;
	}

	@JsonProperty("time")
	public String getTime() {
		return time;
	}

	@JsonProperty("time")
	public void setTime(String time) {
		this.time = time;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

