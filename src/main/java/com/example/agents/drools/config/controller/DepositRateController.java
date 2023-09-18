package com.example.agents.drools.config.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.agents.drools.config.model.DNACModel;
import com.example.agents.drools.config.model.RCAModel;
import com.example.agents.drools.config.model.TE_Endpoint;
import org.json.JSONObject;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.agents.drools.config.model.BullseyeDroolsModel;

@RestController
public class DepositRateController {

    @Autowired
    private KieContainer kieContainer;

    //	@RequestMapping(value = "/getInterestRate", method = RequestMethod.GET, produces = "application/json")
    public String getInterest(@RequestParam(required = true) String alerts) {
        System.out.println("Alerts:: "+ alerts);
        KieSession kieSession = kieContainer.newKieSession();
        List<String> teEndpointList = new ArrayList<String>();
        teEndpointList.add("TE_endpoint_pageloadtime");
        List<String> dnacList = new ArrayList<String>();
        dnacList.add("None");

        TE_Endpoint teEndpointModel = new TE_Endpoint(teEndpointList);
        DNACModel dnacModel = new DNACModel(dnacList);
        RCAModel rcaModel = new RCAModel();
        kieSession.insert(teEndpointModel);
        kieSession.insert(dnacModel);
        kieSession.insert(rcaModel);
        kieSession.fireAllRules();
        kieSession.dispose();
        return new JSONObject().put("The RCA for this application is ", teEndpointModel.getRca()).toString();
    }
    @RequestMapping(value = "/getInterestRate", method = RequestMethod.GET, produces = "application/json")
    public String testDrools(@RequestParam(required = true) String alerts) {
        KieSession kieSession = kieContainer.newKieSession();
        List<String> al = Arrays.asList(alerts.split(","));
        BullseyeDroolsModel bullseyeDroolsModel = new BullseyeDroolsModel(al);
        FactHandle insert = kieSession.insert(bullseyeDroolsModel);
        int i = kieSession.fireAllRules();
        kieSession.dispose();
        return new JSONObject().put("The RCA for this application is ", bullseyeDroolsModel.getRca()).toString();
    }


    @GetMapping("/drool")
    public String test(){
        KieSession kieSession = kieContainer.newKieSession();
        System.out.println(kieSession);
//    List<String> issuesList = Arrays.asList("TE_endpoint_memory_utilization","TE_endpoint_memory_utilization","Any KPI breach", "TE_endpoint_cpu_utilization", "TE_endpoint_pageloadtime", "Any KPI breach");
//      List<String> issuesList = Arrays.asList("TE_endpoint_Jitter","Any KPI breach","TE_endpoint_memory_utilization","TE_endpoint_packet_loss","TE_endpoint_throughput","TE_endpoint_SSL_Error","TE_endpoint_Latency","DNAC_AP_Health" , "DNAC_Client_health");
////        System.out.println(issuesList1.size());
       List<String> issuesList = Arrays.asList();
        boolean empty = issuesList.isEmpty();
        System.out.println(empty);
        BullseyeDroolsModel bullseyeDroolsModel = new BullseyeDroolsModel(issuesList);
        kieSession.insert(bullseyeDroolsModel);
        kieSession.fireAllRules();
        kieSession.dispose();
        return bullseyeDroolsModel.getRca();
    }
}



