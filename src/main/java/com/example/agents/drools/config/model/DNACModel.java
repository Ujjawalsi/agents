package com.example.agents.drools.config.model;

import java.util.List;

public class DNACModel {
    private List<String> alerts;

    public DNACModel(List<String> alerts) {
        super();
        this.alerts = alerts;
    }

    public List<String> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<String> alerts) {
        this.alerts = alerts;
    }



}
