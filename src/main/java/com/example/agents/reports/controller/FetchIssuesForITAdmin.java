package com.example.agents.reports.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import com.example.agents.agentsandmonitors.AgentsAndMonitorsModel;
import com.example.agents.agentsandmonitors.AgentsAndMonitorsService;
import com.example.agents.application.entities.Application;
import com.example.agents.application.service.ApplicationService;
import com.example.agents.dnacNetworkHealthData.DnacNetworkHealthDataModel;
import com.example.agents.dnacNetworkHealthData.DnacNetworkHealthDataService;
import com.example.agents.endpointAgent.EndPointAgentsService;
import com.example.agents.endpointAgent.EndpointAgentModel;
import com.example.agents.constant.Constant;
import com.example.agents.reports.entities.ThousandEyeAlert;
import com.example.agents.reports.repository.ThousandEyeAlertRepo;
import com.example.agents.reports.service.DnacClientService;
import com.example.agents.reports.service.ThousandEyeAlertService;
import com.example.agents.teUsage.TeUsageModel;
import com.example.agents.teUsage.TeUsageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.agents.customDate.DateTimeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.agents.thousandeye.itadmin.bean.DnacEndpoint;
import com.example.agents.thousandeye.itadmin.bean.ITAdminBean;
import com.example.agents.vel.common.connector.service.impl.BUSAPIConnectorImpl;
import com.example.agents.ldap.GetUserDetailFromLDAPByEmail;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

@Component
@Controller
//@PropertySource("classpath:com/example/agents/vel/common/connector/service/constants.properties")
public class FetchIssuesForITAdmin {

//    @Value("${dnac_health_api}")
//    String dnac_health_api;
//    @Value("${awsFlag}")
//    boolean awsFlag;

//	Properties prop = new Properties();
//	InputStream input=getClass().getClassLoader().getResourceAsStream("com/vel/te/scheduler/constants.properties");
//	prop.load(input);

//    @Autowired
//    private KieContainer kieContainer;
    @Autowired
    CreateReports reports;

    @Autowired
    private EndPointAgentsService endPointAgentsService;

    @Autowired
    private ApplicationService applicationService;


    @Autowired
    public ThousandEyeAlertRepo thousandEyeAlertRepo;

    @Autowired
    private TeUsageService teUsageService;

    @Autowired
    private DnacNetworkHealthDataService dnacNetworkHealthDataService;

    @Autowired
    private ThousandEyeAlertService thousandEyeAlertService;

    @Autowired
    private DnacClientService dnacClientService;

    @Autowired
    private AgentsAndMonitorsService agentsAndMonitorsService;
    //	@Autowired
//	IBUSAPIConnectorService service;
    BUSAPIConnectorImpl service = new BUSAPIConnectorImpl();
    DateTimeUtil timeutil = new DateTimeUtil();

    static Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    static {
        root.setLevel(Level.INFO);
    }

    //Mostly Done --- please check before or after ....during final run
    @RequestMapping(value = "BullsEye/fetchIssuesAdmin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public String fetchIssuesFromTEITadmin(@RequestParam(value = "application", required = false) String application,
                                           @RequestParam(value = "host_name", required = true) String agent_name,
                                           @RequestParam(value = "start_time", required = true) String start_time,
                                           @RequestParam(value = "end_time", required = true) String end_time,
                                           @RequestParam(value = "email", required = true) String email) {
        String checkIssuesWithUser = "";
//        Properties prop = new Properties();
//        InputStream input=AuthenticateUserAPI.class.getClassLoader().getResourceAsStream("com/example/agents/ldap/ldap.properties");
//        try {
//            prop.load(input);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        String flag = prop.getProperty("isLdap");

        if (Constant.flag.equals("true")) {
            GetUserDetailFromLDAPByEmail l = new GetUserDetailFromLDAPByEmail();
            checkIssuesWithUser = l.getEmailDetailFromLdap(email);
        }else {
            checkIssuesWithUser = email.substring(0,email.indexOf('@'));
        }
        if (checkIssuesWithUser == "" || checkIssuesWithUser == null) {
            return new JSONArray().toString();
        }
        System.out.println("checkIssuesWithUser: " + checkIssuesWithUser);

//        String domainName = new FetchIssues().getDomainName(checkIssuesWithUser);
      String domainName = endPointAgentsService.getDomainName(checkIssuesWithUser);

        System.out.println("domainName: " + domainName);
        if (domainName == null) {
            return new JSONArray().toString();
        }

        System.out.println("Getting allerts to check!!");
        List<DnacEndpoint> dnacDataList = new ArrayList<DnacEndpoint>();
        ITAdminBean admin_bean = new ITAdminBean();
        JSONObject processedRes = thousandEyeAlertService.getThousandEyeAlerts(start_time, end_time, checkIssuesWithUser, application,
                domainName);
        JSONObject endPointAgentJson = processTEEndPointData(processedRes.getJSONArray("thousand_endpoint"));
        JSONObject enterpriseAgentJson = processTEEnterpriseData(processedRes.getJSONArray("thousand_enterprise"));
        JSONObject dnacJson = new JSONObject();
        if (!processedRes.getJSONArray("dnac_endpoint").toList().isEmpty()) {
            dnacJson = processDnacOutputData(processedRes.getJSONArray("dnac_endpoint"));
        }
        processedRes.remove("thousand_endpoint");
        processedRes.put("thousand_endpoint", endPointAgentJson);
        processedRes.remove("thousand_enterprise");
        processedRes.put("thousand_enterprise", enterpriseAgentJson);
        processedRes.remove("dnac_endpoint");
        processedRes.put("dnac_endpoint", dnacJson);
        System.out.println("processedRes=== " + processedRes);
        return new JSONArray().put(processedRes).toString();
    }

    private JSONObject processDnacOutputData(JSONArray jsonArray) {
        // {"client_health":"1","ap_health":"True"}
        JSONObject finalDnacArray = new JSONObject();
        JSONArray clientHealthArray = new JSONArray();
        JSONArray apHealthArray = new JSONArray();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jobj = jsonArray.getJSONObject(i);
            String clientHealth = jobj.getString("client_health");
            String apHealth = jobj.getString("ap_health");
            String issueTime = jobj.getString("issueTime");
            if (!clientHealth.equalsIgnoreCase("True")) {
                JSONObject health = new JSONObject();
                health.put("client_health", clientHealth);
                health.put("issueTime", issueTime);
                clientHealthArray.put(health);
            } else if (!apHealth.equalsIgnoreCase("True")) {
                JSONObject aphealth = new JSONObject();
                aphealth.put("ap_health", apHealth);
                aphealth.put("issueTime", issueTime);
                apHealthArray.put(aphealth);
            }
        }
        finalDnacArray.put("client_health", clientHealthArray);
        finalDnacArray.put("ap_health", apHealthArray);

        return finalDnacArray;
    }

    private JSONObject processTEEnterpriseData(JSONArray jsonArray) {
        // {"loss":"2%","jitter":"True","latency":"257.6 ms","throughput":"0
        // kbps","pageloadtime":"True"}
        JSONObject finalTEEnterpriseJson = new JSONObject();
        JSONArray packetLossArray = new JSONArray();
        JSONArray jitterArray = new JSONArray();
        JSONArray latencyArray = new JSONArray();
        JSONArray throughputArray = new JSONArray();
        JSONArray pageloadtimeArray = new JSONArray();
        JSONArray responseTimeArray = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jobj = jsonArray.getJSONObject(i);
            String packetLoss = jobj.getString("loss");
            String jitter = jobj.getString("jitter");
            String latency = jobj.getString("latency");
            String throughput = jobj.getString("throughput");
            String pageloadtime = jobj.getString("pageloadtime");
            String responseTime = jobj.getString("responseTime");
            String issueTime = jobj.getString("issueTime");
            if (!jitter.equalsIgnoreCase("True")) {
                JSONObject jitterJson = new JSONObject();
                jitterJson.put("jitter", jitter);
                jitterJson.put("issueTime", issueTime);
                jitterArray.put(jitterJson);
            } else if (!packetLoss.equalsIgnoreCase("True")) {
                JSONObject packetLossJson = new JSONObject();
                packetLossJson.put("packetLoss", packetLoss);
                packetLossJson.put("issueTime", issueTime);
                packetLossArray.put(packetLossJson);
            } else if (!latency.equalsIgnoreCase("True")) {
                JSONObject latencyJson = new JSONObject();
                latencyJson.put("latency", latency);
                latencyJson.put("issueTime", issueTime);
                latencyArray.put(latencyJson);
            } else if (!throughput.equalsIgnoreCase("True")) {
                JSONObject throughputJson = new JSONObject();
                throughputJson.put("throughput", throughput);
                throughputJson.put("issueTime", issueTime);
                throughputArray.put(throughputJson);
            } else if (!pageloadtime.equalsIgnoreCase("True")) {
                JSONObject pageloadtimeJson = new JSONObject();
                pageloadtimeJson.put("pageloadtime", pageloadtime);
                pageloadtimeJson.put("issueTime", issueTime);
                pageloadtimeArray.put(pageloadtimeJson);
            } else if (!responseTime.equalsIgnoreCase("True")) {
                JSONObject responseTimeJson = new JSONObject();
                responseTimeJson.put("pageloadtime", pageloadtime);
                responseTimeJson.put("issueTime", issueTime);
                responseTimeArray.put(responseTimeJson);
            }
        }
        finalTEEnterpriseJson.put("throughput", throughputArray);
        finalTEEnterpriseJson.put("pageloadtime", pageloadtimeArray);
        finalTEEnterpriseJson.put("jitter", jitterArray);
        finalTEEnterpriseJson.put("latency", latencyArray);
        finalTEEnterpriseJson.put("loss", packetLossArray);
        finalTEEnterpriseJson.put("responseTime", responseTimeArray);
        return finalTEEnterpriseJson;
    }

    private JSONObject processTEEndPointData(JSONArray jsonArray) {
        JSONObject finalTEJson = new JSONObject();
        JSONArray cpuArray = new JSONArray();
        JSONArray memoryArray = new JSONArray();
        JSONArray jitterArray = new JSONArray();
        JSONArray packetLossArray = new JSONArray();
        JSONArray latencyArray = new JSONArray();
        JSONArray signalArray = new JSONArray();
        JSONArray throughputArray = new JSONArray();
        JSONArray errorArray = new JSONArray();
        JSONArray pageloadtimeArray = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jobj = jsonArray.getJSONObject(i);
            String cpu = jobj.getString("cpu_utilization");
            String memory = jobj.getString("memory_utilization");
            String packetLoss = jobj.getString("packetLoss");
            String latency = jobj.getString("latency");
            String signal_quality = jobj.getString("signal_quality");
            String throughput = jobj.getString("throughput");
            String error = jobj.getString("error");
            String pageloadtime = jobj.getString("pageloadtime");
            String jitter = jobj.getString("jitter");

            String issueTime = jobj.getString("issueTime");
            // System.out.println("cpu: "+cpu);
            if (!cpu.equalsIgnoreCase("True")) {
                JSONObject cpuJson = new JSONObject();
                cpuJson.put("cpu_utilization", jobj.getString("cpu_utilization"));
                cpuJson.put("issueTime", issueTime);
                cpuArray.put(cpuJson);
            } else if (!memory.equalsIgnoreCase("True")) {
                JSONObject memoryJson = new JSONObject();
                memoryJson.put("memory_utilization", memory);
                memoryJson.put("issueTime", issueTime);
                memoryArray.put(memoryJson);
            } else if (!jitter.equalsIgnoreCase("True")) {
                JSONObject jitterJson = new JSONObject();
                jitterJson.put("jitter", jitter);
                jitterJson.put("issueTime", issueTime);
                jitterArray.put(jitterJson);
            } else if (!packetLoss.equalsIgnoreCase("True")) {
                JSONObject packetLossJson = new JSONObject();
                packetLossJson.put("packetLoss", packetLoss);
                packetLossJson.put("issueTime", issueTime);
                packetLossArray.put(packetLossJson);
            } else if (!latency.equalsIgnoreCase("True")) {
                JSONObject latencyJson = new JSONObject();
                latencyJson.put("latency", latency);
                latencyJson.put("issueTime", issueTime);
                latencyArray.put(latencyJson);
            } else if (!signal_quality.equalsIgnoreCase("True")) {
                JSONObject signalJson = new JSONObject();
                signalJson.put("signal_quality", signal_quality);
                signalJson.put("issueTime", issueTime);
                signalArray.put(signalJson);
            } else if (!throughput.equalsIgnoreCase("True")) {
                JSONObject throughputJson = new JSONObject();
                throughputJson.put("throughput", throughput);
                throughputJson.put("issueTime", issueTime);
                throughputArray.put(throughputJson);
            } else if (!error.equalsIgnoreCase("True")) {
                JSONObject errorJson = new JSONObject();
                errorJson.put("error", error);
                errorJson.put("issueTime", issueTime);
                errorArray.put(errorJson);
            } else if (!pageloadtime.equalsIgnoreCase("True")) {
                JSONObject pageloadtimeJson = new JSONObject();
                pageloadtimeJson.put("pageloadtime", pageloadtime);
                pageloadtimeJson.put("issueTime", issueTime);
                pageloadtimeArray.put(pageloadtimeJson);
            }
        }
        finalTEJson.put("cpu_utilization", cpuArray);
        finalTEJson.put("memory_utilization", memoryArray);
        finalTEJson.put("signal_quality", signalArray);
        finalTEJson.put("throughput", throughputArray);
        finalTEJson.put("pageloadtime", pageloadtimeArray);
        finalTEJson.put("jitter", jitterArray);
        finalTEJson.put("latency", latencyArray);
        finalTEJson.put("packetLoss", packetLossArray);
        finalTEJson.put("error", errorArray);
        return finalTEJson;
    }


    // fetchAgentsSummaryCount
    //Done -----> Done
    @RequestMapping(value = "/BullsEye/fetchAgentsSummaryCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> fetchAgentsSummaryCount(@RequestParam(value = "start_time", required = false) String start_time,
                                          @RequestParam(value = "end_time", required = false) String end_time) {

        System.out.println("start time " + start_time);

        JSONArray jsonarray = new JSONArray();
        int disabled_agents = 0;
        int enabled_agents = 0;
        int free = 0;

        List<EndpointAgentModel> endpointAgentList = endPointAgentsService.getAll();
        for (EndpointAgentModel user : endpointAgentList) {

            JSONObject jsonObject = new JSONObject(user.getAgentData());
            String status = jsonObject.getString("status");

            if ("disabled".equalsIgnoreCase(status)) {
                disabled_agents++;
            } else if ("enabled".equalsIgnoreCase(status)) {
                enabled_agents++;
            } else {
                free++;
            }
        }

        JSONObject json1 = new JSONObject();
        json1.put("disabled_agent", disabled_agents);
        json1.put("enabled_agent", enabled_agents);
        json1.put("total_agent", disabled_agents + enabled_agents);

        JSONObject json_final = new JSONObject();
        json_final.put("endpoint_agent_count", json1);

        System.out.println("disabled: " + disabled_agents + " enabled: " + enabled_agents + " free " + free);

        return new ResponseEntity<>(json_final.toString(), HttpStatus.OK);



    }


//Complete Done
    // fetchAgentsSummary
    @RequestMapping(value = "/BullsEye/fetchAgentsSummary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> fetchAgentsSummary(@RequestParam(value = "type", required = true) String type // type_values:
                                     // disabled,
                                     // enabled, overall
//	@RequestParam(value = "end_time", required = true) String end_time,
//	@RequestParam(value = "start_time", required = true) String start_time)
    ) {

        JSONArray jsonarrayEnabled = new JSONArray();
        JSONArray jsonarrayDisabled = new JSONArray();
        JSONArray jsonOverAll = new JSONArray();
        int disabled_agent = 0;
        int enabled_agent = 0;
        int free = 0;

        List<EndpointAgentModel> endpointAgentList = endPointAgentsService.getAll();
        for (EndpointAgentModel user : endpointAgentList) {
            JSONObject jsonObject = new JSONObject(user.getAgentData());
            String status = jsonObject.getString("status");

            if ("disabled".equalsIgnoreCase(status)) {
                disabled_agent++;
                jsonarrayDisabled.put(new JSONObject(user.getAgentData())); // Add the user to the JSON array
                jsonOverAll.put(new JSONObject(user.getAgentData()));
            } else if ("enabled".equalsIgnoreCase(status)) {
                enabled_agent++;
                jsonarrayEnabled.put(new JSONObject(user.getAgentData())); // Add the user to the JSON array
                jsonOverAll.put(new JSONObject(user.getAgentData()));
            } else {
                free++;
            }
        }
        JSONObject jobj2 = new JSONObject();

        if (type.equalsIgnoreCase("enabled")) {
            jobj2.put(type, jsonarrayEnabled);
        } else if (type.equalsIgnoreCase("disabled")) {
            jobj2.put(type, jsonarrayDisabled);
        } else if (type.equalsIgnoreCase("overall")) {
            jobj2.put(type, jsonOverAll);
        }

        System.out.println("json_array_disabled: " + disabled_agent);
        System.out.println("json_array_enabled: " + enabled_agent);

        return new ResponseEntity<>(jobj2.toString(), HttpStatus.OK);

    }



//Done --> Complete
    @GetMapping("/BullsEye/fetchUsageSummaryCount")
    public ResponseEntity<String> fetchUsageSummaryCount(
            @RequestParam(value = "start_time", required = true) String start_time,
            @RequestParam(value = "end_time", required = true) String end_time) {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestampStr = start_time.replace("%20", " ");
        LocalDateTime startime = LocalDateTime.parse(formattedTimestampStr, formatter);
        String formattedTimestampStr1 = end_time.replace("%20", " ");
        LocalDateTime endTime = LocalDateTime.parse(formattedTimestampStr1, formatter);

        List<TeUsageModel> teUsageModelsList = teUsageService.findByTimeRange(startime,endTime);


        JSONObject jobjre = new JSONObject();

        for (TeUsageModel usageSummary : teUsageModelsList) {
            String jsonDocument = usageSummary.getJsonDocument();
            System.out.println(jsonDocument);

            //Json Filtering According to use
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = null;
            try {
                 root = objectMapper.readTree(jsonDocument);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            JsonNode usageNode = root.get("usage");
            JsonNode quotaNode = usageNode.get("quota");
            Integer endpointAgentsIncluded = quotaNode.get("endpointAgentsIncluded").asInt();
            Integer endpointAgentsUsed = usageNode.get("endpointAgentsUsed").asInt();
            Integer enterpriseUnitsUsed = usageNode.get("enterpriseUnitsUsed").asInt();
            Integer enterpriseUnitsProjected = usageNode.get("enterpriseUnitsProjected").asInt();
            Integer cloudUnitsIncluded = quotaNode.get("cloudUnitsIncluded").asInt();
            Integer  cloudUnitsUsed = usageNode.get("cloudUnitsUsed").asInt();
            Integer cloudUnitsProjected = usageNode.get("cloudUnitsProjected").asInt();

            JSONObject jobjEndpoint = new JSONObject();
            jobjEndpoint.put("max", endpointAgentsIncluded);
            jobjEndpoint.put("used", endpointAgentsUsed);

            JSONObject jobjEnpriseAgentUtilization = new JSONObject();
            jobjEnpriseAgentUtilization.put("used", enterpriseUnitsUsed);
            jobjEnpriseAgentUtilization.put("projected", enterpriseUnitsProjected);

            JSONObject jobjCloudAgentUtilization = new JSONObject();
            jobjCloudAgentUtilization.put("quota", cloudUnitsIncluded);
            jobjCloudAgentUtilization.put("used", cloudUnitsUsed);
            jobjCloudAgentUtilization.put("projected", cloudUnitsProjected);

            jobjre.put("endpointAgents", jobjEndpoint);
            jobjre.put("enterpriseAgentUtilization", jobjEnpriseAgentUtilization);
            jobjre.put("cloudAgentUtilization", jobjCloudAgentUtilization);
        }

        return new ResponseEntity<>(jobjre.toString(), HttpStatus.OK);
    }






    // fetchUsageSummary
    //Done ----> Completed
    @RequestMapping(value = "/BullsEye/fetchUsageSummary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public String fetchUsageSummary(@RequestParam(value = "type", required = false) String type,
                                    @RequestParam(value = "start_time", required = true) String start_time,
                                    @RequestParam(value = "end_time", required = true) String end_time) {
        // type_value: enterpriseAgentUnits, tests
        JSONArray json_array = new JSONArray();
        JSONObject jobjre = new JSONObject();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTimestampStr = start_time.replace("%20", " ");
            LocalDateTime startime = LocalDateTime.parse(formattedTimestampStr, formatter);
            String formattedTimestampStr1 = end_time.replace("%20", " ");
            LocalDateTime endTime = LocalDateTime.parse(formattedTimestampStr1, formatter);

            List<TeUsageModel> teUsageModelList = teUsageService.findByTimeRange(startime, endTime);

            for (TeUsageModel usage : teUsageModelList) {

                String doc = usage.getJsonDocument();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = null;
                try {
                    root = objectMapper.readTree(doc);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                JsonNode usageNode = root.get("usage");

                JsonNode quotaNode = usageNode.get(type);
                jobjre.put(type, quotaNode);
                System.out.println(type + "-------" + quotaNode.toString());
                System.out.println("jobjre-------" + jobjre.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobjre.toString();

    }


    // fetchAgentsMonitorsCount
    //Done -----complete
    @GetMapping("/BullsEye/fetchAgentsMonitorsCount")
    public ResponseEntity<String> fetchAgentsMonitorsCount(
            @RequestParam(value = "start_time", required = true) String start_time,
            @RequestParam(value = "end_time", required = true) String end_time) {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestampStr = start_time.replace("%20", " ");
        LocalDateTime startime = LocalDateTime.parse(formattedTimestampStr, formatter);
        String formattedTimestampStr1 = end_time.replace("%20", " ");
        LocalDateTime endTime = LocalDateTime.parse(formattedTimestampStr1, formatter);

        List<AgentsAndMonitorsModel> agentsAndMonitorsModelList = agentsAndMonitorsService.findByTimeRange(startime, endTime);

        JSONObject jobjRes = new JSONObject();
        JSONObject jobj3 = new JSONObject();

        for (AgentsAndMonitorsModel agentMonitor : agentsAndMonitorsModelList) {

            List<String> agentStates = new ArrayList<>();
            JSONObject jsonObject1 = new JSONObject(agentMonitor.getJsonDocument());
            JSONArray agents = jsonObject1.getJSONArray("agents");

            for (int i = 0; i < agents.length(); i++) {
                JSONObject agent = agents.getJSONObject(i);
                String agentState = agent.optString("agentState", "NoAgent");
                System.out.println("Agent State: " + agentState);
                agentStates.add(agentState);
            }

            int online_agents = 0;
            int offline_agents = 0;
            int disabled_agents = 0;
            int no_agents = 0;


            for (String state : agentStates) {
                if ("online".equalsIgnoreCase(state)) {
                    online_agents++;
                } else if ("offline".equalsIgnoreCase(state)) {
                    offline_agents++;
                } else if ("disabled".equalsIgnoreCase(state)) {
                    disabled_agents++;
                } else {
                    no_agents++;
                }
            }

            jobj3.put("online", online_agents);
            jobj3.put("offline", offline_agents);
            jobj3.put("disabled", disabled_agents);
            jobj3.put("no_agents_info", no_agents);
            jobjRes.put("agentState", jobj3);
        }

        return new ResponseEntity<>(jobjRes.toString(), HttpStatus.OK);
    }


    //Done ---complete
    @GetMapping("/BullsEye/fetchAgentsMonitors")
    public ResponseEntity<String> fetchAgentsMonitors(
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "start_time", required = true) String start_time,
            @RequestParam(value = "end_time", required = true) String end_time) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestampStr = start_time.replace("%20", " ");
        LocalDateTime startime = LocalDateTime.parse(formattedTimestampStr, formatter);
        String formattedTimestampStr1 = end_time.replace("%20", " ");
        LocalDateTime endTime = LocalDateTime.parse(formattedTimestampStr1, formatter);

        List<AgentsAndMonitorsModel> agentsAndMonitorsModelList = agentsAndMonitorsService.findByTimeRange(startime,endTime);
        System.out.println(agentsAndMonitorsModelList.size());


        JSONObject jobjRes = new JSONObject();
        JSONArray jArrayRes = new JSONArray();

        JSONArray jsonArray = new JSONArray(agentsAndMonitorsModelList);

       for (AgentsAndMonitorsModel agentMonitor : agentsAndMonitorsModelList) {

           JSONObject jsonObject = new JSONObject(agentMonitor.getJsonDocument());
           System.out.println(jsonObject);
            JSONArray agents = jsonObject.getJSONArray("agents");
            for (int j = 0; j < agents.length(); j++) {
                JSONObject jsonObject1 = agents.getJSONObject(j);
                if (jsonObject1.has("agentState")) {
                    String agentState = jsonObject1.getString("agentState");
                    if ((agentState.equalsIgnoreCase("online")) && (type.equalsIgnoreCase("online"))) {
                        jArrayRes.put(jsonObject1);
                    } else if (agentState.equalsIgnoreCase("offline") && (type.equalsIgnoreCase("offline"))) {
                        jArrayRes.put(jsonObject1);
                    } else if (agentState.equalsIgnoreCase("disabled") && (type.equalsIgnoreCase("disabled"))) {
                        jArrayRes.put(jsonObject1);
                    }
                }
            }
        }

        jobjRes.put(type, jArrayRes);

        return new ResponseEntity<>(jobjRes.toString(), HttpStatus.OK);
    }




//DOne ----> complete
    // fetchDnacClientHealthCount
    @RequestMapping(value = "/BullsEye/fetchDnacClientHealthCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public String fetchDnacClientHealthCount(@RequestParam(value = "application", required = false) String application,
                                             @RequestParam(value = "start_time", required = true) String start_time,
                                             @RequestParam(value = "end_time", required = true) String end_time,
                                             @RequestParam(value = "type", required = true) String type) {
//	                          //type_value: wireless, wired

        int wired = 0;
        int poor_wired = 0;
        int fair_wired = 0;
        int good_wired = 0;
        int wireless = 0;
        int poor_wireless = 0;
        int fair_wireless = 0;
        int good_wireless = 0;


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = simpleDateFormat.parse(start_time);
            endTime = simpleDateFormat.parse(end_time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        JSONArray j_arr = dnacClientService.getDnacClientData(startTime, endTime);
        for (int i = 0; i < j_arr.length(); i++) {
            JSONObject j_obj =  j_arr.getJSONObject(i);


            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode;
            try {
                rootNode = objectMapper.readTree(String.valueOf(j_obj));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            String jsonDocument = rootNode.get("jsonDocument").asText();
            JsonNode innerNode;
            try {
                innerNode = objectMapper.readTree(jsonDocument);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            String deviceType = innerNode.get("deviceType").asText();

            if (deviceType.equalsIgnoreCase(type) && deviceType.equalsIgnoreCase("wired")) {
                wired++;

                int averageHealthScore_min = innerNode.get("averageHealthScore_min").asInt();

                if (averageHealthScore_min < 4) {
                    poor_wired++;
                } else if ((averageHealthScore_min >= 4) && (averageHealthScore_min < 7)) {
                    fair_wired++;
                } else if ((averageHealthScore_min >= 7)) {
                    good_wired++;
                }

            } else if (deviceType.equalsIgnoreCase(type) && deviceType.equalsIgnoreCase("wireless")) {
                wireless++;
                int averageHealthScore_min = innerNode.get("averageHealthScore_min").asInt();

                if (averageHealthScore_min < 4) {
                    poor_wireless++;
                } else if ((averageHealthScore_min >= 4) && (averageHealthScore_min < 7)) {
                    fair_wireless++;
                } else if ((averageHealthScore_min >= 7)) {
                    good_wireless++;
                }
            }
        }

        JSONObject j_obj2 = new JSONObject();
        if (type.equalsIgnoreCase("wired")) {

            JSONObject j_obj3 = new JSONObject();

            j_obj3.put("wired", wired);
            j_obj3.put("poor_wired", poor_wired);
            j_obj3.put("fair_wired", fair_wired);
            j_obj3.put("good_wired", good_wired);
            j_obj2.put("deviceType_wired", j_obj3);

        } else if (type.equalsIgnoreCase("wireless")) {

            JSONObject j_obj3 = new JSONObject();

            j_obj3.put("wireless", wireless);
            j_obj3.put("poor_wireless", poor_wireless);
            j_obj3.put("fair_wireless", fair_wireless);
            j_obj3.put("good_wireless", good_wireless);
            j_obj2.put("deviceType_wireless", j_obj3);

        }

        return j_obj2.toString();
    }


    //Done---> Complete
    // fetchDnacClientHealth
    @RequestMapping(value = "/BullsEye/fetchDnacClientHealth", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public String fetchDnacClientHealth(@RequestParam(value = "health", required = true) String health,
                                        @RequestParam(value = "type", required = true) String type,
                                        @RequestParam(value = "start_time", required = true) String start_time,
                                        @RequestParam(value = "end_time", required = true) String end_time

    ) {
        //
        int wired = 0;
        int poor_wired = 0;
        int fair_wired = 0;
        int good_wired = 0;
        int wireless = 0;
        int poor_wireless = 0;
        int fair_wireless = 0;
        int good_wireless = 0;
        JSONArray j_array = new JSONArray();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = simpleDateFormat.parse(start_time);
            endTime = simpleDateFormat.parse(end_time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        JSONArray j_arr = dnacClientService.getDnacClientData(startTime, endTime);
        for (int i = 0; i < j_arr.length(); i++) {
            JSONObject jobj = j_arr.getJSONObject(i);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode;
            try {
                rootNode = objectMapper.readTree(String.valueOf(jobj));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            String jsonDocument = rootNode.get("jsonDocument").asText();
            JsonNode innerNode;
            try {
                innerNode = objectMapper.readTree(jsonDocument);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            String deviceType = innerNode.get("deviceType").asText();

            int healthScore = innerNode.get("averageHealthScore_min").asInt();
            if ((deviceType.equalsIgnoreCase(type)) && (health.equalsIgnoreCase("poor")) && (healthScore < 4)) {
                j_array.put(jobj);
            } else if ((deviceType.equalsIgnoreCase(type)) && (health.equalsIgnoreCase("fair"))
                    && (healthScore >= 4 && healthScore < 7)) {
                j_array.put(jobj);
            } else if ((deviceType.equalsIgnoreCase(type)) && (health.equalsIgnoreCase("good")) && (healthScore >= 7)) {
                j_array.put(jobj);
            }

        }

        JSONObject j_obj2 = new JSONObject();
        JSONObject j_obj3 = new JSONObject();
        j_obj2.put(health, j_array);
        j_obj3.put(type, j_obj2);

        return j_obj3.toString();
    }

    // fetchDnacClientHealth 96 work in progress
    //Done ---Complete
    @RequestMapping(value = "/BullsEye/fetchDnacClientHealthDay", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public String fetchDnacClientHealthDay(@RequestParam(value = "type", required = true) String type,
                                           @RequestParam(value = "date", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date) {
        JSONArray jarr = new JSONArray();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FetchIssuesForITAdmin fs = new FetchIssuesForITAdmin();
        JSONArray jArray = new JSONArray();
        for (int i = 0; i < 96; i++) {
            double poorPercent = 0;
            double fairPercent = 0;
            double goodPercent = 0;
            double poor_device = 0;
            double fair_device = 0;
            double good_device = 0;
            JSONArray jarrdevice_poor = new JSONArray();
            JSONArray jarrdevice_fair = new JSONArray();
            JSONArray jarrdevice_good = new JSONArray();
            double total_device = 0;
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date);
            calendar1.add(Calendar.MINUTE, -15);
            System.out.println(calendar1);
            Date endDDate = calendar1.getTime();
            Date olddate = date;
            Date newdate = endDDate;

            date = endDDate;
            System.out.println(date);
            jArray= dnacClientService.getDnacOneDayClientData(olddate,newdate);
            System.out.println(jArray.length());
            for (int j = 0; j < jArray.length(); j++) {
                JSONObject jObj = jArray.getJSONObject(j);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode;
                try {
                    rootNode = objectMapper.readTree(String.valueOf(jObj));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                String jsonDocument = rootNode.get("jsonDocument").asText();
                JsonNode innerNode;
                try {
                    innerNode = objectMapper.readTree(jsonDocument);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                String deviceType = innerNode.get("deviceType").asText();
                int healthScore = innerNode.get("averageHealthScore_min").asInt();

                System.out.println("Device Type: " + deviceType);
                System.out.println("Average Health Score Min: " + healthScore);
                if (deviceType.equalsIgnoreCase(type)) {
                    if (healthScore < 4) {
                        total_device++;
                        poor_device++;
                        jarrdevice_poor.put(jObj);
                    } else if (healthScore >= 4 && healthScore < 7) {
                        total_device++;
                        fair_device++;
                        jarrdevice_fair.put(jObj);
                    } else if (healthScore > 7) {
                        total_device++;
                        good_device++;
                        jarrdevice_good.put(jObj);
                    }
                }
            }

            if (total_device > 0) {
                poorPercent = (poor_device / total_device) * (double) 100;
                fairPercent = (fair_device / total_device) * (double) 100;
                goodPercent = (good_device / total_device) * (double) 100;
            }
            int pPercent = (int) poorPercent;
            int fPercent = (int) fairPercent;
            int gPercent = (int) goodPercent;

            JSONObject jRes = new JSONObject();
            jRes.put("jarrdevice_poor_percent", pPercent);
            jRes.put("jarrdevice_fair_percent", fPercent);
            jRes.put("jarrdevice_good_percent", gPercent);
            jRes.put("jarrdevice_poor_count", poor_device);
            jRes.put("jarrdevice_fair_count", fair_device);
            jRes.put("jarrdevice_good_count", good_device);
            jRes.put("jarrdevice_total_count", total_device);

            jarr.put(jRes);

        }
        System.out.println("jarr  ===" + jarr.toString());

        return jarr.toString();
    }



//Done--completed
    @RequestMapping(value = "/BullsEye/fetchDnacNetworkHealthDay", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public String fetchDnacNetworkHealthDay(
            @RequestParam(value = "date", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date) {
        JSONArray jarr = new JSONArray();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FetchIssuesForITAdmin fs = new FetchIssuesForITAdmin();
        JSONArray jArray = new JSONArray();
        List<DnacNetworkHealthDataModel> dataModelList = new ArrayList<>();
        for (int i = 0; i < 96; i++) {
            double poorPercent = 0;
            double fairPercent = 0;
            double goodPercent = 0;
            double poor_count = 0;
            double fair_count = 0;
            double good_count = 0;
            double noHealthPercent = 0;
            double nohealth_count = 0;

            double total_count = 0;
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date);
            calendar1.add(Calendar.MINUTE, -15);
            Date endDDate = calendar1.getTime();
            Date olddate = date;
            Date newdate = endDDate;
            date = endDDate;
           dataModelList =    dnacNetworkHealthDataService.getDnacOneDayNetworkData(olddate,newdate);
            System.out.println(dataModelList.size());

            for (DnacNetworkHealthDataModel model : dataModelList) {
                String jObj =  model.getResponse();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = null;
                try {
                    rootNode = objectMapper.readTree(jObj);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                int count_fair = rootNode.get("fairCount").asInt();
                int count_poor = rootNode.get("badCount").asInt();
                int count_good = rootNode.get("goodCount").asInt();
                int count_nohealth = rootNode.get("noHealthCount").asInt();
                int count_total = rootNode.get("totalCount").asInt();
                poor_count += count_poor;
                fair_count += count_fair;
                good_count += count_good;
                nohealth_count += count_nohealth;
                total_count += count_total;

            }

            if (total_count > 0) {
                poorPercent = (poor_count / total_count) * (double) 100;
                fairPercent = (fair_count / total_count) * (double) 100;
                goodPercent = (good_count / total_count) * (double) 100;
                noHealthPercent = (nohealth_count / total_count) * (double) 100;
            }
            int pPercent = (int) poorPercent;
            int fPercent = (int) fairPercent;
            int gPercent = (int) goodPercent;
            int hPercent = (int) noHealthPercent;

            JSONObject jRes = new JSONObject();
            jRes.put("jarrdevice_poor_percent", pPercent);
            jRes.put("jarrdevice_fair_percent", fPercent);
            jRes.put("jarrdevice_good_percent", gPercent);
            jRes.put("jarrdevice_nohealth_percent", hPercent);
            jRes.put("jarrdevice_poor_count", poor_count);
            jRes.put("jarrdevice_fair_count", fair_count);
            jRes.put("jarrdevice_good_count", good_count);
            jRes.put("jarrdevice_total_count", total_count);


            jarr.put(jRes);

        }
        System.out.println("jarr  ===" + jarr.toString());


        return jarr.toString();
    }



//Done --complete
    @RequestMapping(value = "/BullsEye/getalertsall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public String getAlertsall(@RequestParam(value = "sevtype", required = false) String sevtype,
                                               @RequestParam(value = "date", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date) {

        JSONObject severityFinal = new JSONObject();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, -10);
            Date endDDate = calendar.getTime();
            String endTime = dateFormat.format(date);
            String startTime = dateFormat.format(endDDate);
            List<ThousandEyeAlert> thousandEyeAlerts = thousandEyeAlertService.getAlertsByTimeRange(startTime,endTime);
try {
    JSONArray activeAlert = new JSONArray();
    List<JSONObject> infoList = new ArrayList<>();
    List<JSONObject> majorList = new ArrayList<>();
    List<JSONObject> minorList = new ArrayList<>();
    List<JSONObject> criticalList = new ArrayList<>();

    for (ThousandEyeAlert alert : thousandEyeAlerts) {
        JSONObject jsonObject = new JSONObject(alert.getAlert());
        if (!jsonObject.has("dateEndZoned")) {
            activeAlert.put(jsonObject);
        }
    }

    for (int i = 0; i < activeAlert.length(); i++) {
        JSONObject jsonObject = activeAlert.getJSONObject(i);
        String severity = jsonObject.getString("severity");
        if (severity.equalsIgnoreCase("Info")) {
            infoList.add(jsonObject);
        } else if (severity.equalsIgnoreCase("Major")) {
            majorList.add(jsonObject);
        } else if (severity.equalsIgnoreCase("Minor")) {
            minorList.add(jsonObject);
        } else if (severity.equalsIgnoreCase("Critical")) {
            criticalList.add(jsonObject);
        }

    }

    severityFinal.put("Info", infoList.size());
    severityFinal.put("Major", majorList.size());
    severityFinal.put("Minor", minorList.size());
    severityFinal.put("Critical", criticalList.size());
    severityFinal.put("All", infoList.size()+ majorList.size()+ minorList.size()+criticalList.size());
    if (sevtype.equals("0"))
        return criticalList.toString();

    if (sevtype.equals("1"))
        return majorList.toString();

    if (sevtype.equals("2"))
        return minorList.toString();

    if (sevtype.equals("3"))
        return infoList.toString();

      }catch (Exception e){
    e.printStackTrace();
    }
        return severityFinal.toString();


    }



    //Done--complete

    @RequestMapping(value = "/BullsEye/gettopalerts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public String topalerts(@RequestParam(value = "alertName", required = false) String name,
                            @RequestParam(value = "alertId", required = false) String Id,
                            @RequestParam(value = "date", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -10);
        Date endDDate = calendar.getTime();
        String endTime = dateFormat.format(date);
        String startTime = dateFormat.format(endDDate);
        List<ThousandEyeAlert> thousandEyeAlerts = thousandEyeAlertService.getAlertsByTimeRange(startTime,endTime);

        try {
            JSONArray activeAlert = new JSONArray();
            List<JSONObject> infoList = new ArrayList<>();
            List<JSONObject> majorList = new ArrayList<>();
            List<JSONObject> minorList = new ArrayList<>();
            List<JSONObject> criticalList = new ArrayList<>();

            for (ThousandEyeAlert alert : thousandEyeAlerts) {
                JSONObject jsonObject = new JSONObject(alert.getAlert());
                if (!jsonObject.has("dateEndZoned")) {
                    activeAlert.put(jsonObject);
                }
            }

            for (int i = 0; i < activeAlert.length(); i++) {
                JSONObject jsonObject = activeAlert.getJSONObject(i);
                String severity = jsonObject.getString("severity");
                if (severity.equalsIgnoreCase("Info")) {
                    infoList.add(jsonObject);
                } else if (severity.equalsIgnoreCase("Major")) {
                    majorList.add(jsonObject);
                } else if (severity.equalsIgnoreCase("Minor")) {
                    minorList.add(jsonObject);
                } else if (severity.equalsIgnoreCase("Critical")) {
                    criticalList.add(jsonObject);
                }
            }
            List<JSONObject> allalerts = new ArrayList<>();
            HashSet<String> appname = new HashSet<>();

            thousandEyeAlertService.addAlertName(criticalList, appname, allalerts);
            thousandEyeAlertService.addAlertName(majorList, appname, allalerts);
            thousandEyeAlertService.addAlertName(minorList, appname, allalerts);
            thousandEyeAlertService.addAlertName(infoList, appname, allalerts);

            if (Id != null && (!Id.isEmpty())) {
                JSONArray alertArray = new JSONArray();
                for (int i = 0; i < allalerts.size(); i++) {

                    JSONObject value = allalerts.get(i);
                    String alertValue=  value.getString("alertId");
                    if (alertValue.equals(Id)) {
                        alertArray.put(value);

                    }
                }
                return alertArray.toString();
            }
            if (name != null && (!name.isEmpty())) {
                JSONArray alertArray = new JSONArray();
                for (int i = 0; i < allalerts.size(); i++) {
                    JSONObject value = allalerts.get(i);
                 String alertValue = value.getString("alertId");
                 String fieldValue = value.getString("testName");

                    if (fieldValue.equals(name)) {
                        alertArray.put(value);
                    }
                }return alertArray.toString();
            }
            return thousandEyeAlertService.categorizeAlerts(allalerts, appname).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Error occurs";


    }


//Done ----but check when we have data in thousand eye alert
    @RequestMapping(value = "/BullsEye/getApplicationTrend", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public String getApplicationTrend(@RequestParam(value = "appName", required = true) String name) {
        JSONArray arr = new JSONArray();
        try {

            List<Application>applications = applicationService.findByName(name);
            int exists = applications.size();

            long currentTimestamp = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentTimestamp);
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.clear();
            calendar.set(currentYear, currentMonth, currentDay);
            Date currentDate = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentDateString = new Date(currentTimestamp);
            String enddate = dateFormat.format(currentDateString);
            String startdate = dateFormat.format(currentDate);
            Date aheaddate = currentDate;
            Date prevdate = currentDate;
            int result = aheaddate.compareTo(currentDateString);
            String previous = dateFormat.format(prevdate);
            List<JSONObject> alerts = new ArrayList<>();

            boolean breakthrough = false;

            while (result <= 0 && exists > 0) {

                calendar.add(Calendar.MINUTE, +15);
                aheaddate = calendar.getTime();
                JSONObject jobj = new JSONObject();
                String ahead = dateFormat.format(aheaddate);
                JSONObject intquery = new JSONObject();
     	List<ThousandEyeAlert> filterListByNameaAndTime = new ArrayList<>();
        List<ThousandEyeAlert> thousandEyeAlerts= thousandEyeAlertService.findByTimeGap(ahead, startdate);
                for (ThousandEyeAlert alert: thousandEyeAlerts) {
               JSONObject jsonObject  =  new JSONObject(alert.getAlert());
                    String testName = jsonObject.getString("testName");
                    if (testName.equalsIgnoreCase(name)){
                     if (jsonObject.has("dateEndZoned")){
                         String dateEndZoned = jsonObject.getString("dateEndZoned");

                         SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                         Date dateEndZonedChange = dateFormatter.parse(dateEndZoned);
                         if (aheaddate.before(dateEndZonedChange)){
                             filterListByNameaAndTime.add(alert);
                         }
                     }else{
                         filterListByNameaAndTime.add(alert);
                     }
                    }
                }

                long length = filterListByNameaAndTime.size();
                if (length > 0)
                    breakthrough = true;

                if (breakthrough) {
                    jobj.put("dateStart", previous.substring(11, 16));
                    jobj.put("dateEnd", ahead.substring(11, 16));
                    jobj.put("ActiveCount", length);
                    arr.put(jobj);
                }

                result = aheaddate.compareTo(currentDateString);
                previous = ahead;
            }

            return arr.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
