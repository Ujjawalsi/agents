package com.example.agents.thousandeye.itadmin.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"cloudWatch",
	"vm",
	"thousand_endpoint",
	"thousand_enterprise",
	"dnac_endpoint",
	"user_info"
})
@Generated("jsonschema2pojo")
public class ITAdminBean {

	@JsonProperty("cloudWatch")
	private CloudWatch cloudWatch;
	@JsonProperty("vm")
	private Vm vm;
	@JsonProperty("thousand_endpoint")
	private List<ThousandEndpoint> thousandEndpoint;
	@JsonProperty("thousand_enterprise")
	private List<ThousandEnterprise> thousandEnterprise;
	@JsonProperty("dnac_endpoint")
	private List<DnacEndpoint> dnacEndpoint;
	
	@JsonProperty("user_info")
	private UserInfo userInfo;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("cloudWatch")
	public CloudWatch getCloudWatch() {
		return cloudWatch;
	}

	@JsonProperty("cloudWatch")
	public void setCloudWatch(CloudWatch cloudWatch) {
		this.cloudWatch = cloudWatch;
	}

	@JsonProperty("vm")
	public Vm getVm() {
		return vm;
	}

	@JsonProperty("vm")
	public void setVm(Vm vm) {
		this.vm = vm;
	}

	@JsonProperty("thousand_endpoint")
	public List<ThousandEndpoint> getThousandEndpoint() {
		return thousandEndpoint;
	}

	@JsonProperty("thousand_endpoint")
	public void setThousandEndpoint(List<ThousandEndpoint> thousandEndpoint) {
		this.thousandEndpoint = thousandEndpoint;
	}

	@JsonProperty("thousand_enterprise")
	public List<ThousandEnterprise> getThousandEnterprise() {
		return thousandEnterprise;
	}

	@JsonProperty("thousand_enterprise")
	public void setThousandEnterprise(List<ThousandEnterprise> thousandEnterprise) {
		this.thousandEnterprise = thousandEnterprise;
	}

	@JsonProperty("user_info")
	public UserInfo getUserInfo() {
		return userInfo;
	}

	@JsonProperty("user_info")
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public List<DnacEndpoint> getDnacEndpoint() {
		return dnacEndpoint;
	}

	public void setDnacEndpoint(List<DnacEndpoint> dnacEndpoint) {
		this.dnacEndpoint = dnacEndpoint;
	}

	@Override
	public String toString() {
		return "ITAdminBean [cloudWatch=" + cloudWatch + ", vm=" + vm + ", thousandEndpoint=" + thousandEndpoint
				+ ", thousandEnterprise=" + thousandEnterprise + ", dnacEndpoint=" + dnacEndpoint + ", userInfo="
				+ userInfo + ", additionalProperties=" + additionalProperties + "]";
	}
	
}

