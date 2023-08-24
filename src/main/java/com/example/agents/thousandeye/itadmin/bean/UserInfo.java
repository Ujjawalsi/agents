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
	"agent_user",
	"agent_location"
})
@Generated("jsonschema2pojo")
public class UserInfo {

	@JsonProperty("agent_user")
	private String agentUser;
	@JsonProperty("agent_location")
	private String agentLocation;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("agent_user")
	public String getAgentUser() {
		return agentUser;
	}

	@JsonProperty("agent_user")
	public void setAgentUser(String agentUser) {
		this.agentUser = agentUser;
	}

	@JsonProperty("agent_location")
	public String getAgentLocation() {
		return agentLocation;
	}

	@JsonProperty("agent_location")
	public void setAgentLocation(String agentLocation) {
		this.agentLocation = agentLocation;
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

