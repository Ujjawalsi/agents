
package com.example.agents.thousandeye.bean2;

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
    "Endpoint_Performance",
    "Gateway_Connectivity",
    "Network_path",
    "Application_infra"
})
@Generated("jsonschema2pojo")
public class ThousandEyeAlertBean {

    @JsonProperty("Endpoint Performance")
    private EndpointPerformance endpointPerformance;
    @JsonProperty("Gateway Connectivity")
    private GatewayConnectivity gatewayConnectivity;
    @JsonProperty("Network Path")
    private NetworkPathToApplication networkPath;
    @JsonProperty("Application Infrastructure")
    private ApplicationPerformance applicationInfra;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Endpoint Performance")
    public EndpointPerformance getEndpointPerformance() {
        return endpointPerformance;
    }

    @JsonProperty("Endpoint Performance")
    public void setEndpointPerformance(EndpointPerformance endpointPerformance) {
        this.endpointPerformance = endpointPerformance;
    }

    @JsonProperty("Gateway Connectivity")
    public GatewayConnectivity getGatewayConnectivity() {
        return gatewayConnectivity;
    }

    @JsonProperty("Gateway Connectivity")
    public void setGatewayConnectivity(GatewayConnectivity gatewayConnectivity) {
        this.gatewayConnectivity = gatewayConnectivity;
    }

    @JsonProperty("Network Path")
    public NetworkPathToApplication getNetworkPath() {
        return networkPath;
    }

    @JsonProperty("Network Path")
    public void setNetworkPath(NetworkPathToApplication networkPath) {
        this.networkPath = networkPath;
    }

    @JsonProperty("Application Infrastructure")
    public ApplicationPerformance getApplicationInfra() {
        return applicationInfra;
    }

    @JsonProperty("Application Infrastructure")
    public void setApplicationInfra(ApplicationPerformance applicationInfra) {
        this.applicationInfra = applicationInfra;
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
