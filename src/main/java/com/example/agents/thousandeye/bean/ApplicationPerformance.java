package com.example.agents.thousandeye.bean;


import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonPropertyOrder({
        "issue_name",
        "startTime",
        "dateEnd"
})
@Generated("jsonschema2pojo")
public class ApplicationPerformance {

    @JsonProperty("issue_name")
    private String issueName;
    @JsonProperty("startTime")
    private String startTime;
    @JsonProperty("dateEnd")
    private String dateEnd;
    @JsonProperty("message")
    private String message;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("issue_name")
    public String getIssueName() {
        return issueName;
    }

    @JsonProperty("issue_name")
    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    @JsonProperty("startTime")
    public String getStartTime() {
        return startTime;
    }

    @JsonProperty("startTime")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @JsonProperty("dateEnd")
    public String getDateEnd() {
        return dateEnd;
    }

    @JsonProperty("dateEnd")
    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }
}
