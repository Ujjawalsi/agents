package com.example.agents.reports.service;

import com.example.agents.reports.entities.ThousandEyeAlert;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public interface ThousandEyeAlertService {

    void updateRecord(String newEventId, JSONObject dateEnd);

    void save(ThousandEyeAlert thousandEyeAlert);

    List<ThousandEyeAlert> findAlertsByCriteria(String startTime, String endTime, String agentName, String application, String domainName);

    List<ThousandEyeAlert> findAlertsByCriteria1(String startTime, String endTime, String agentName, String application, String domainName);

    List<ThousandEyeAlert> findAlertsForApplicationIssues(String startTime, String endTime, String enterpriseAgentName, String application);

    void process(HttpServletRequest l_request);

    public void addAlertName(List<JSONObject> sevalert, HashSet<String> appname, List<JSONObject> allalerts);

    public JSONArray categorizeAlerts(List<JSONObject> allalerts, HashSet<String> appname);

    public JSONObject getThousandEyeAlerts(String startTime, String endTime, String agentName,
                                           String application, String domainName);
    List<ThousandEyeAlert> getAlertsByTimeRange(String startTime, String endTime);

    List<ThousandEyeAlert> findByTimeGap(String ahead, String startdate);

}
