
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
    "issue_name",
    "startTime",
    "dateEnd"
})
@Generated("jsonschema2pojo")
public class Issue__2 {

    @JsonProperty("issue_name")
    private String issueName;
    @JsonProperty("startTime")
    private String startTime;
    @JsonProperty("dateEnd")
    private String dateEnd;
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

}
