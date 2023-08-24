package com.example.agents.thousandeye.bean;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Endpoint_Performance",
        "Application_performance"
})
@Generated("jsonschema2pojo")
public class ThousandEyeAlertBean {

    @JsonProperty("Endpoint Performance")
    private List<EndpointPerformance> endpointPerformance = null;
    @JsonProperty("Application Infrastructure")
    private List<ApplicationPerformance> applicationPerformance = null;
    @JsonProperty("Network Path to Application")
    private List<NetworkPathToApplication> networkPathToApplication = null;

    @JsonProperty("Gateway Connectivity")
    private List<GatewayConnectivity> gatewayConnectivity = null;

//	@JsonProperty("count")
//	private int count;



    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Endpoint Performance")
    public List<EndpointPerformance> getEndpointPerformance() {
        return endpointPerformance;
    }

    @JsonProperty("Endpoint Performance")
    public void setEndpointPerformance(List<EndpointPerformance> endpointPerformance) {
        this.endpointPerformance = endpointPerformance;
    }

    @JsonProperty("Application Infrastructure")
    public List<ApplicationPerformance> getApplicationPerformance() {
        return applicationPerformance;
    }

    @JsonProperty("Application Infrastructure")
    public void setApplicationPerformance(List<ApplicationPerformance> applicationPerformance) {
        this.applicationPerformance = applicationPerformance;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
    @JsonProperty("Network Path to Application")
    public List<NetworkPathToApplication> getNetworkPathToApplication() {
        return networkPathToApplication;
    }
    @JsonProperty("Network Path to Application")
    public void setNetworkPathToApplication(List<NetworkPathToApplication> networkPathToApplication) {
        this.networkPathToApplication = networkPathToApplication;
    }

    @JsonProperty("Gateway Connectivity")
    public List<GatewayConnectivity> getGatewayConnectivity() {
        return gatewayConnectivity;
    }
    @JsonProperty("Gateway Connectivity")
    public void setGatewayConnectivity(List<GatewayConnectivity> gatewayConnectivity) {
        this.gatewayConnectivity = gatewayConnectivity;
    }

//	public int getCount() {
//		return count;
//	}
//
//	public void setCount(int count) {
//		this.count = count;
//	}



}