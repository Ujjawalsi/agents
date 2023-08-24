package com.example.agents.drools.config.controller;

/*
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.agents.drools.config.model.DNACModel;
import com.example.agents.drools.config.model.RCAModel;
import com.example.agents.drools.config.model.TE_Endpoint;
import org.json.JSONObject;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.agents.drools.config.model.BullseyeDroolsModel;

@RestController
public class DepositRateController {

    @Autowired
    private KieContainer kieContainer;

    //	@RequestMapping(value = "/getInterestRate", method = RequestMethod.GET, produces = "application/json")
    public String getInterest(@RequestParam(required = true) String alerts) {
        System.out.println("Alerts:: "+ alerts);
        KieSession kieSession = kieContainer.newKieSession();
//		List<String> al = Arrays.asList(alerts.split(","));

        List<String> teEndpointList = new ArrayList<String>();
//		teEndpointList.add("TE_endpoint_memory_utilization");
        teEndpointList.add("TE_endpoint_pageloadtime");
//		teEndpointList.add("TE_endpoint_cpu_utilization");
//		teEndpointList.add("TE_endpoint_packet loss");

        List<String> dnacList = new ArrayList<String>();
        dnacList.add("None");

        TE_Endpoint teEndpointModel = new TE_Endpoint(teEndpointList);
        DNACModel dnacModel = new DNACModel(dnacList);
        RCAModel rcaModel = new RCAModel();

//		BullseyeDroolsModel bullseyeDroolsModel = new BullseyeDroolsModel(teEndpointList);
        kieSession.insert(teEndpointModel);
        kieSession.insert(dnacModel);
        kieSession.insert(rcaModel);
        kieSession.fireAllRules();
        kieSession.dispose();
        return new JSONObject().put("The RCA for this application is ", teEndpointModel.getRca()).toString();
    }
    @RequestMapping(value = "/getInterestRate", method = RequestMethod.GET, produces = "application/json")
    public String testDrools(@RequestParam(required = true) String alerts) {
        System.out.println("Alerts:: "+ alerts);
        KieSession kieSession = kieContainer.newKieSession();
        List<String> al = Arrays.asList(alerts.split(","));
        System.out.println(al+" List:: "+ al.size());
        BullseyeDroolsModel bullseyeDroolsModel = new BullseyeDroolsModel(al);
        kieSession.insert(bullseyeDroolsModel);
        kieSession.fireAllRules();
        kieSession.dispose();
        return new JSONObject().put("The RCA for this application is ", bullseyeDroolsModel.getRca()).toString();
    }
}

 */