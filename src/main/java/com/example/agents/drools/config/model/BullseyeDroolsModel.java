package com.example.agents.drools.config.model;

import java.util.List;

public class BullseyeDroolsModel {
    private List<String> alerts;
    private String rca;

    public BullseyeDroolsModel(List<String> alerts) {
        super();
        this.alerts = alerts;
    }

    public List<String> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<String> alerts) {
        this.alerts = alerts;
    }

    public String getRca() {
        return rca;
    }

    public void setRca(String rca) {
        this.rca = rca;
    }

}
