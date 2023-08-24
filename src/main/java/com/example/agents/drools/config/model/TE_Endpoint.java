package com.example.agents.drools.config.model;

import java.util.List;

public class TE_Endpoint {
    private List<String> alerts;
    private String rca;

    public String getRca() {
        return rca;
    }

    public void setRca(String rca) {
        this.rca = rca;
    }

    public List<String> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<String> alerts) {
        this.alerts = alerts;
    }

    public TE_Endpoint(List<String> alerts) {
        super();
        this.alerts = alerts;
    }

}
