package com.example.agents.reports.implementation;
import com.example.agents.drools.config.configuration.*;

import com.example.agents.customDate.DateTimeUtil;
import com.example.agents.drools.config.model.BullseyeDroolsModel;
import com.example.agents.inventoryData.InventoryDataModel;
import com.example.agents.inventoryData.InventoryDataRepository;
import com.example.agents.reports.controller.CreateReports;
import com.example.agents.reports.entities.DnacClient;
import com.example.agents.reports.entities.ThousandEyeAlert;
import com.example.agents.reports.repository.ThousandEyeAlertRepo;
import com.example.agents.reports.service.DnacClientService;
import com.example.agents.reports.service.ThousandEyeAlertService;
import com.example.agents.thousandeye.itadmin.bean.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.agents.vel.common.connector.service.impl.BUSAPIConnectorImpl;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ThousandEyeAlertImpl implements ThousandEyeAlertService {


    @Value("${USER.NAME}")
    private String userName;

    @Value("${USER.PASSWORD}")
    private String userPassword;

    @Value("${aws.Flag}")
    private boolean awsFlag;

    @Value("${dnac.health.api}")
    private String dnacHealthApi;

    @Value("${Enterprise.Agent}")
    private String enterpriseAgent;

    @Value("${te.api.key}")
    private String teApiKey;

   @Value("${te.alert.api}")
   private String teAlertApi;




    @Autowired
    public ThousandEyeAlertRepo thousandEyeAlertRepo;


    BUSAPIConnectorImpl service = new BUSAPIConnectorImpl();


    DateTimeUtil timeutil = new DateTimeUtil();




    @Autowired
    private KieContainer kieContainer;
    @Autowired
    private InventoryDataRepository inventoryDataRepository;


    @Autowired
     private CreateReports reports;


    @Autowired
    private DnacClientService dnacClientService;

    @Autowired
    private DroolsConfiguration droolsConfiguration;



    @Override
    public void updateRecord(String newEventId, JSONObject dateEnd) {

        ThousandEyeAlert thousandEyeAlert = thousandEyeAlertRepo.findByNewEventId(newEventId);
        if (thousandEyeAlert != null) {
            thousandEyeAlert.setAlert(dateEnd.toString());
            thousandEyeAlert.setId(Long.valueOf(dateEnd.getString("_id")));
            thousandEyeAlert.setAlert(dateEnd.getString("alert"));
            thousandEyeAlert.setEventType(dateEnd.getString("eventType"));
            thousandEyeAlert.setEventId(dateEnd.getString("eventId"));
            thousandEyeAlert.setNewEventId(dateEnd.getString("newEventId"));
            thousandEyeAlertRepo.save(thousandEyeAlert);

        }
    }

    @Override
    public void save(ThousandEyeAlert thousandEyeAlert) {
        thousandEyeAlertRepo.save(thousandEyeAlert);
        System.out.println("Inserted into db");

    }

    @Override
    public List<ThousandEyeAlert> findAlertsByCriteria(String endTime, String application) {
        return thousandEyeAlertRepo.findAlertsByCriteria(endTime, application);

    }

    @Override
    public List<ThousandEyeAlert> findAlertsByCriteria1(String startTime, String endTime,String application) {
       return thousandEyeAlertRepo.findByTimeGapAndTestName(startTime, endTime,application);
    }

    @Override
    public List<ThousandEyeAlert> findAlertsForApplicationIssues(String startTime, String endTime,String application) {
        return thousandEyeAlertRepo.findByTimeGapAndTestName(startTime, endTime,application);
    }

    @Override
    public void process(HttpServletRequest l_request) {

        Map<String, String> map = new HashMap<String, String>();
        String _body = null;
        try {
            Enumeration<String> headerNames = l_request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = l_request.getHeader(key);
                map.put(key, value);
            }
            String _auth = map.get("authorization");
            String _arr[] = _auth.split(" ");
            Base64.Decoder decoder = Base64.getMimeDecoder();
            String _decodePass = new String(decoder.decode(_arr[1]));
            String[] _userPassword = _decodePass.split(":");
            String _username = _userPassword[0];
            String _password = _userPassword[1];
           if (_username.equalsIgnoreCase(userName) && _password.equalsIgnoreCase(userPassword)) {
            _body = getBody(l_request);
            JSONObject _json = new JSONObject(_body);
            String eventType = _json.getString("eventType");
            String eventId = _json.getString("eventId");
            String newEventId = eventId.split("-")[1];
            JSONObject _alerts = _json.getJSONObject("alert");
            if (_alerts.has("dateEndZoned")) {
                String dateEndZoned = _alerts.getString("dateEndZoned");
                dateEndZoned = dateEndZoned.replace("IST", "").trim();
                _alerts.put("dateEnd", dateEndZoned);
                _json.remove("alert");
                _json.put("alert", _alerts);
            }
            if (eventType.equalsIgnoreCase("ALERT_NOTIFICATION_CLEAR")) {
                updateRecord(newEventId, _alerts);
            } else {
                _json.put("newEventId", newEventId);
                if (!_json.has("browserSessionAlert")) {
                    String jsonString = _json.toString();
                    ThousandEyeAlert thousandEyeAlert = new ThousandEyeAlert();
                    thousandEyeAlert.setAlert(String.valueOf(_json.getJSONObject("alert")));
                    thousandEyeAlert.setEventId(_json.getString("eventId"));
                    thousandEyeAlert.setEventType(_json.getString("eventType"));
                    thousandEyeAlert.setNewEventId(_json.getString("newEventId"));

                    save(thousandEyeAlert);

                }
            }


            }else {
                System.out.println("Dnac callback :: Password does not matched: ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }



    }

    @Override
    public void addAlertName(List<JSONObject> sevalert, HashSet<String> appname, List<JSONObject> allalerts) {
        for (int i = 0; i < sevalert.size(); i++) {
            allalerts.add(sevalert.get(i));
            JSONObject dbobj = sevalert.get(i);
            String fieldValue = dbobj.getString("testName");
            appname.add(fieldValue);
        }

    }
    @Override
    public JSONArray categorizeAlerts(List<JSONObject> allalerts, HashSet<String> appname) {
        JSONArray categorize = new JSONArray();
        for (String element : appname) {
            JSONObject newapp = new JSONObject();
            int critical = 0;
            int major = 0;
            int minor = 0;
            int info = 0;
            for (int i = 0; i < allalerts.size(); i++) {
                JSONObject dbobj = allalerts.get(i);
                String fieldValue = dbobj.getString("testName");
                String severity = dbobj.getString("severity");
                if (element.equals(fieldValue)) {
                    if (severity.equals("Critical"))
                        critical++;
                    else if (severity.equals("Major"))
                        major++;
                    else if (severity.equals("Minor"))
                        minor++;
                    else if (severity.equals("Info"))
                        info++;
                }
            }
            newapp.put("testName", element);
            newapp.put("Critical", critical);
            newapp.put("Major", major);
            newapp.put("Minor", minor);
            newapp.put("Info", info);
            newapp.put("GrandTotal", critical + major + minor + info);

            categorize.put(newapp);
        }
        return categorize;
    }

    @Override
    public JSONObject getThousandEyeAlerts(String startTime, String endTime, String agentName,
                                           String application, String domainName) {
        JSONObject rtnString = new JSONObject();
        List<DnacClient>_jsonDnac = dnacClientService.getDnacData(agentName,startTime,endTime,application);
        try {
            List<ThousandEyeAlert> thousandEyeAlertList = new ArrayList<>();
            List<ThousandEyeAlert>thousandEyeAlerts = thousandEyeAlertRepo.findByTestName(application);
            for (ThousandEyeAlert alert : thousandEyeAlerts){
                ObjectMapper objectMapper = new ObjectMapper();
                JSONObject jsonObject = new JSONObject(alert.getAlert());
                JsonNode rootNode = objectMapper.readTree(alert.getAlert());
                JsonNode agentsNode = rootNode.get("agents");
                if (agentsNode != null && agentsNode.isArray()) {
                    for (JsonNode agentNode : agentsNode) {
                        String agentNameFinal = agentNode.get("agentName").asText();
                        if (agentNameFinal.equalsIgnoreCase(domainName) && !jsonObject.has("dateEnd")){
                            thousandEyeAlertList.add(alert);
                        } else if (agentNameFinal.equalsIgnoreCase(domainName) && jsonObject.has("dateEnd")) {
                            String dateEnd = rootNode.get("dateEnd").asText();
                            Date dateEndFinal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateEnd);
                            Date startTimeFinal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
                            //need to change from before to after
                            if (dateEndFinal.after(startTimeFinal)){
                                thousandEyeAlertList.add(alert);
                            }


                        }
                    }
                }
            }
            JSONArray appIssuesArr = new JSONArray();
            checkAlerts(startTime, endTime, application);
            if (application != null && !application.isEmpty()) {
                appIssuesArr = getApplicationIssues(startTime, endTime, agentName, application);

            }
            ITAdminBean bean = processJSONData(thousandEyeAlertList, agentName, application, appIssuesArr, domainName, startTime,
                    endTime);
            bean = processDnacJSONDataAdmin(bean, _jsonDnac);
            com.fasterxml.jackson.databind.ObjectWriter ow= new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(bean);
            rtnString = new JSONObject(json);
            List<String> issuesList = createIssuesList(rtnString);
            String rca = droolsRulesEngine(issuesList);
            rtnString.put("rca", new JSONObject().put("value", rca));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnString;
    }

    @Override
    public List<ThousandEyeAlert> getAlertsByTimeRange(String startTime, String endTime) {
        return thousandEyeAlertRepo.findByTimeRange(startTime,endTime);
    }

    @Override
    public List<ThousandEyeAlert> findByTimeGap(String ahead, String startdate) {
      return thousandEyeAlertRepo.findByTimingGap(ahead, startdate);
    }

    public static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }

        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    inputStream.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }




    public void checkAlerts(String startTime, String endTime, String application) throws Exception {

        JSONArray jsonarray = new JSONArray();
        String te_api_key = "";
        String te_alert_api = "";
        try {
            String Enterprise_Agent = enterpriseAgent;
            te_api_key=teApiKey;
            te_alert_api=teAlertApi;
            try {
                List<ThousandEyeAlert> thousandEyeAlerts = thousandEyeAlertRepo.findByTimeGapAndTestName(startTime,endTime,application);
                for (ThousandEyeAlert alert : thousandEyeAlerts){
                    ObjectMapper objectMapper = new ObjectMapper();
                    JSONObject jsonObject = new JSONObject(alert.getAlert());
                    JsonNode rootNode = objectMapper.readTree(alert.getAlert());
                    JsonNode agentsNode = rootNode.get("agents");
                    if (agentsNode != null && agentsNode.isArray()) {
                        for (JsonNode agentNode : agentsNode) {
                            String agentNameFinal = agentNode.get("agentName").asText();
                            if (agentNameFinal.equalsIgnoreCase(Enterprise_Agent) && !jsonObject.has("dateEnd")){
                                jsonarray.put(alert);
                            } else if (agentNameFinal.equalsIgnoreCase(Enterprise_Agent) && jsonObject.has("dateEnd")) {
                                String dateEnd = rootNode.get("dateEnd").asText();
                                Date dateEndFinal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateEnd);
                                Date startTimeFinal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
                                //need to change from before to after
                                if (dateEndFinal.after(startTimeFinal)){
                                    jsonarray.put(alert);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject _jObj = jsonarray.getJSONObject(i);
                String newEventId = _jObj.getString("newEventId");
                checkUpdate(newEventId, te_alert_api, te_api_key);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void checkUpdate(String newEventId, String te_alert_api, String te_api_key) throws Exception {
        HttpHeaders _headerSet = new HttpHeaders();
        _headerSet.setContentType(MediaType.APPLICATION_JSON);
        _headerSet.set("Authorization", te_api_key);
        String _url = te_alert_api + newEventId + ".json";
        ResponseEntity<String> response = service.CallGetRequest(_headerSet, "", _url);
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject _json = new JSONObject(response.getBody());
            JSONArray alertsArr = _json.getJSONArray("alert");
            for (int i = 0; i < alertsArr.length(); i++) {
                JSONObject alertobj = alertsArr.getJSONObject(i);
                if (alertobj.has("dateEnd")) {
                    String _dateEndUTC = alertobj.getString("dateEnd");
                    String dateEnd = timeutil.convertUTC2IST(_dateEndUTC);
                    alertobj.remove("dateEnd");
                    alertobj.put("dateEnd", dateEnd);
                    updateRecord(newEventId, alertobj);

                }

            }

        }

    }



    public JSONArray getApplicationIssues(String startTime, String endTime, String enterpriseAgentName,
                                          String application) throws IOException {
        JSONArray jsonarray = new JSONArray();
        String Enterprise_Agent = enterpriseAgent;
        try {
            List<ThousandEyeAlert> thousandEyeAlerts = thousandEyeAlertRepo.findByTimeGapAndTestName(startTime, endTime, application);
            for (ThousandEyeAlert alert: thousandEyeAlerts) {
                ObjectMapper objectMapper = new ObjectMapper();
               JSONObject jsonObject = new JSONObject(alert.getAlert());
                JsonNode rootNode = objectMapper.readTree(alert.getAlert());
                JsonNode agentsNode = rootNode.get("agents");
                if (agentsNode != null && agentsNode.isArray()) {
                    for (JsonNode agentNode : agentsNode) {
                        String agentNameFinal = agentNode.get("agentName").asText();
                        if (agentNameFinal.equalsIgnoreCase(Enterprise_Agent)) {
                            if (agentNameFinal.equalsIgnoreCase(Enterprise_Agent) && !jsonObject.has("dateEnd")){
                                jsonarray.put(alert.getAlert());
                            } else if (agentNameFinal.equalsIgnoreCase(Enterprise_Agent) && jsonObject.has("dateEnd")) {
                                String dateEnd = rootNode.get("dateEnd").asText();
                                Date dateEndFinal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateEnd);
                                Date startTimeFinal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
                                //need to change from before to after
                                if (dateEndFinal.after(startTimeFinal)){
                                    jsonarray.put(alert.getAlert());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonarray;
    }




    private synchronized ITAdminBean processJSONData(List<ThousandEyeAlert> arrData, String agentName, String application,
                                                     JSONArray appIssuesArr, String domainName, String startTime, String endTime) {
        ITAdminBean bean = new ITAdminBean();
        try {
            List<ThousandEyeAlert> _jsonResponse = arrData;
            CloudWatch cloudWatch = new CloudWatch();
            List<ThousandEndpoint> thousandEndpointList = new ArrayList<ThousandEndpoint>();
            List<ThousandEnterprise> thousandEnterpriseList = new ArrayList<ThousandEnterprise>();

            Vm vm = new Vm();
            List<DnacEndpoint> endPoint = new ArrayList<DnacEndpoint>();
            for (ThousandEyeAlert alert : _jsonResponse) {
              JSONObject _jObj =new JSONObject(alert.getAlert());
                JSONArray _agentsArray = _jObj.getJSONArray("agents");
                for (int j = 0; j < _agentsArray.length(); j++) {
                    ThousandEndpoint thousandEndpoint = new ThousandEndpoint();
                    String _agentName = _agentsArray.getJSONObject(j).getString("agentName");
                    if (_agentName.equalsIgnoreCase(domainName)) {
                        String issueName = "";
                        String _startTime = "";
                        String _dateEnd = "";
                        if (_agentsArray.getJSONObject(j).has("metricsAtStart")) {
                            issueName = _agentsArray.getJSONObject(j).getString("metricsAtStart");
                            _startTime = _agentsArray.getJSONObject(j).getString("dateStart");
                            if (dateCompare(_startTime, endTime)) {
                                break;
                            }
                            if (_agentsArray.getJSONObject(j).has("dateEnd")) {
                                _dateEnd = _agentsArray.getJSONObject(j).getString("dateEnd");
                                if (dateCompare(startTime, _dateEnd)) {
                                    break;
                                }
                            } else if (_jObj.has("dateEnd")) {
                                _dateEnd = _jObj.getString("dateEnd");
                            } else {
                                _dateEnd = "NA";
                            }
                        }
                        thousandEndpoint.setIssueTime(_startTime + " | " + _dateEnd);
                        if (issueName.contains("CPU") || issueName.contains("Memory") || issueName.contains("health")) {
                            String[] issues = issueName.split(":");
                            if (issues[0].contains("CPU")) {
                                thousandEndpoint.setCpuUtilization(issues[1].trim());
                            }
                            if (issues[0].contains("Memory")) {
                                thousandEndpoint.setMemoryUtilization(issues[1].trim());
                            }

                        } else if (issueName.contains("Throughput")) {
                            String[] issues = issueName.split(":");
                            thousandEndpoint.setThroughput(issues[1].trim());
                        } else if (issueName.contains("Page Load")) {
                            String[] issues = issueName.split(":");
                            thousandEndpoint.setPageloadtime(issues[1].trim());
                        } else if (issueName.toLowerCase().contains("jitter")) {
                            String[] issues = issueName.split(":");
                            thousandEndpoint.setJitter(issues[1].trim());
                        } else if (issueName.toLowerCase().contains("packet loss")) {
                            String[] issues = issueName.split(":");
                            thousandEndpoint.setPacketLoss(issues[1].trim());
                        } else if (issueName.toLowerCase().contains("latency")) {
                            String[] issues = issueName.split(":");
                            thousandEndpoint.setLatency(issues[1].trim());
                        } else if (issueName.contains("Error")) {
                            String[] issues = issueName.split(":");
                            thousandEndpoint.setError(issues[1].trim());
                        } else if (issueName.contains("Response Code")) {
                            thousandEndpoint.setError(issueName);
                        }

                        else {
                        }
                        vm.setTime(_startTime + " - " + _dateEnd);
                    }
                    thousandEndpointList.add(thousandEndpoint);
                }
            }
            bean.setThousandEndpoint(thousandEndpointList);

            System.err.println("appIssuesArr Enterprise: " + appIssuesArr);
            for (int i = 0; i < appIssuesArr.length(); i++) {
                JSONObject _jObj = appIssuesArr.getJSONObject(i);
                JSONObject _alertJson = _jObj.getJSONObject("alert");
                JSONArray _agentsArray = _alertJson.getJSONArray("agents");
                for (int j = 0; j < _agentsArray.length(); j++) {
                    ThousandEnterprise thousandEnterprise = new ThousandEnterprise();
                    JSONObject _json = new JSONObject();
                    String issueName = "";
                    String _startTime = "";
                    String _dateEnd = "";
                    if (_agentsArray.getJSONObject(j).has("metricsAtStart")) {
                        issueName = _agentsArray.getJSONObject(j).getString("metricsAtStart");
                        _startTime = _agentsArray.getJSONObject(j).getString("dateStart");

                        if (_alertJson.has("dateEnd")) {
                            _dateEnd = _alertJson.getString("dateEnd");
                            _json.put("dateEnd", _dateEnd);
                        } else {
                            _dateEnd = "NA";
                            _json.put("dateEnd", "NA");
                        }
                    } else {
                        issueName = _alertJson.getString("ruleName");
                        _startTime = _alertJson.getString("dateStart");
                        if (_alertJson.has("dateEnd")) {
                            _dateEnd = _alertJson.getString("dateEnd");
                        }
                    }
                    thousandEnterprise.setIssueTime(_startTime + " | " + _dateEnd);
                    if (issueName.contains("Page Load")) {
                        String[] issues = issueName.split(":");
                        thousandEnterprise.setPageloadtime(issues[1].trim());
                    } else if (issueName.contains("Packet Loss")) {
                        String[] issues = issueName.split(":");
                        thousandEnterprise.setLoss(issues[1].trim());
                    } else if (issueName.contains("Jitter")) {
                        String[] issues = issueName.split(":");
                        thousandEnterprise.setJitter(issues[1].trim());
                    } else if (issueName.contains("Latency")) {
                        String[] issues = issueName.split(":");
                        thousandEnterprise.setLatency(issues[1].trim());
                    } else if (issueName.contains("Throughput")) {
                        String[] issues = issueName.split(":");
                        thousandEnterprise.setThroughput(issues[1].trim());
                    } else if (issueName.contains("Response")) {
                        String[] issues = issueName.split(":");
                        thousandEnterprise.setResponseTime(issues[1].trim());
                    } // Response Time:
                    thousandEnterpriseList.add(thousandEnterprise);
                }
            }
            bean.setThousandEnterprise(thousandEnterpriseList);
            if (awsFlag)
                bean.setCloudWatch(cloudWatch);
            else
                bean.setCloudWatch(new CloudWatch("", ""));
            bean.setVm(vm);
            bean.setDnacEndpoint(endPoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }



    public boolean dateCompare(String date1, String date2) {
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");// 2023-01-19 11:15:00
            Date dt1 = sdf1.parse(date1);
            Date dt2 = sdf1.parse(date2);
            int i = dt1.compareTo(dt2);
            if (i > 0) {
                return true;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }




    private ITAdminBean processDnacJSONDataAdmin(ITAdminBean bean, List<DnacClient> _jsonDnac) {
        // TODO Auto-generated method stub
        List<DnacEndpoint> dnacDataList = new ArrayList<DnacEndpoint>();
        DateTimeUtil timeUtil = new DateTimeUtil();
        if ((_jsonDnac.size()) == 0) {
            dnacDataList.clear();
            ;
            bean.setDnacEndpoint(dnacDataList);
            return bean;
        }
        try {
            for (DnacClient dnacClient : _jsonDnac) {
                DnacEndpoint endpoint_dnac = new DnacEndpoint();
                JSONObject _json=  new JSONObject(dnacClient.getJsonDocument());
                int averageHealthScore_min = Integer.parseInt(_json.getString("averageHealthScore_min"));
                String connectedDeviceName = _json.getString("connectedDeviceName");
                JSONObject _jsonConnected = getMacFromPostgres(connectedDeviceName);
                String apMac = _jsonConnected.getString("macAddress");
                Date start_Time = dnacClient.getStartTime();
                Date end_Time = dnacClient.getEndTime();
                long startTime = timeUtil.convertLongDate(String.valueOf(start_Time));
                long endTime = timeUtil.convertLongDate(String.valueOf(end_Time));
                endpoint_dnac.setIssueTime(start_Time + "|" + end_Time);
                int overallHealth = processDNACClientHealth(apMac, startTime, endTime);
                if (overallHealth == -1)
                    endpoint_dnac.setAp_health("Device Not Found");
                else if (overallHealth > 7) {
                    endpoint_dnac.setAp_health("True");
                } else
                    endpoint_dnac.setAp_health(overallHealth + "");
                if (averageHealthScore_min > 7) {
                    endpoint_dnac.setClient_health("True");
                } else {
                    endpoint_dnac.setClient_health(averageHealthScore_min + "");
                }
                dnacDataList.add(endpoint_dnac);
            }
            bean.setDnacEndpoint(dnacDataList);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return bean;
    }



    public JSONObject getMacFromPostgres(String connectedDeviceName) {
        JSONObject _json = new JSONObject();
        if (connectedDeviceName == null) {
            connectedDeviceName = "";
        }
        try {
            List<InventoryDataModel> inventory = inventoryDataRepository.findByHostname(connectedDeviceName);
            if (!inventory.isEmpty()){
                InventoryDataModel inventoryDataModel = inventory.get(inventory.size() - 1);
                _json= new JSONObject(inventoryDataModel.getData());
            }else {
                System.out.println("Device not found in inventory!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _json;
    }




    public int processDNACClientHealth(String apMac, long startTime, long endTime) {
        int overallHealth = -1;
        try {
            String _token = reports.getAuthTokenFromDNAC();
            HttpHeaders _headers = reports.createHeaders(_token);
            String _url = dnacHealthApi+ startTime + "&endTime=" + endTime;
            ResponseEntity<String> response = service.CallGetRequest(_headers, "", _url);
            if (response.getStatusCode() == HttpStatus.OK) {
                String _body = response.getBody();
                JSONObject jsonData = new JSONObject(_body);
                JSONArray respArr = jsonData.getJSONArray("response");
                for (int i = 0; i < respArr.length(); i++) {
                    if (respArr.getJSONObject(i).getString("macAddress").equalsIgnoreCase(apMac)) {
                        overallHealth = respArr.getJSONObject(i).getInt("overallHealth");
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return overallHealth;
    }



    private List<String> createIssuesList(JSONObject rtnString) {
        List<String> issueList = new ArrayList<>();
        try {
            JSONArray teEndPointJSONArray = rtnString.getJSONArray("thousand_endpoint");
            JSONArray teEnterpriseJSONArray = rtnString.getJSONArray("thousand_enterprise");
            JSONArray dnacJSONArray = rtnString.getJSONArray("dnac_endpoint");

            if (awsFlag) {
                JSONObject awsJSON = rtnString.getJSONObject("cloudWatch");
                awsJSON.keys().forEachRemaining(key -> {
                    Object value = awsJSON.get(key);
                    String issueName = null;
                    if (!value.equals("True") && key.toString().equalsIgnoreCase("cpu_utilization")) {
                        issueName = "AWS_CPU_resources";
                    } else if (!value.equals("True") && key.toString().equalsIgnoreCase("memory_utilization")) {
                        issueName = "AWS_Memory_resources";
                    }
                    if (issueName != null && !issueList.contains(key)) {
                        // issueList.add("TE_endpoint_signal_quality");
                        issueList.add(issueName);
                    }
                });

            }
            for (int i = 0; i < teEndPointJSONArray.length(); i++) {
                JSONObject teEndPointJSON = teEndPointJSONArray.getJSONObject(i);
                teEndPointJSON.keys().forEachRemaining(key -> {
                    Object value = teEndPointJSON.get(key);
                    String issueName = null;
                    if (!value.equals("True") && key.toString().equalsIgnoreCase("cpu_utilization")) {
                        issueName = "TE_endpoint_cpu_utilization";
                    } else if (!value.equals("True") && key.toString().equalsIgnoreCase("memory_utilization")) {
                        issueName = "TE_endpoint_memory_utilization";
                    } else if (!value.equals("True") && key.toString().equalsIgnoreCase("signal_quality")) {
                        issueName = "TE_endpoint_signal_quality";
                    } else if (!value.equals("True") && key.toString().equalsIgnoreCase("throughput")) {
                        issueName = "TE_endpoint_throughput";
                    } else if (!value.equals("True") && key.toString().equalsIgnoreCase("pageloadtime")) {
                        issueName = "TE_endpoint_pageloadtime";
                    } else if (!value.equals("True") && key.toString().equalsIgnoreCase("jitter")) {
                        issueName = "TE_endpoint_Jitter";
                    } else if (!value.equals("True") && (key.toString().equalsIgnoreCase("error"))) {
                        issueName = "TE_endpoint_SSL_Error";
                    } else if (!value.equals("True") && key.toString().equalsIgnoreCase("packetLoss")) {
                        issueName = "TE_endpoint_packet_loss";
                    } else if (!value.equals("True") && key.toString().equalsIgnoreCase("latency")) {
                        issueName = "TE_endpoint_Latency";
                    } else if (!value.equals("True") && key.toString().equalsIgnoreCase("client")) {
                        issueName = "TE_endpoint_Error_4xx";
                    } else if (!value.equals("True") && key.toString().equalsIgnoreCase("server")) {
                        issueName = "TE_endpoint_Error_5xx";
                    }

                    // "TE_endpoint_cpu_utilization" "TE_endpoint_packet_loss" "TE_endpoint_Latency"

                    if (issueName != null && !issueList.contains(issueName)) {
                        // issueList.add("TE_endpoint_signal_quality");
                        issueList.add(issueName);
                    }
                });
            }

            for (int i = 0; i < teEnterpriseJSONArray.length(); i++) {
                JSONObject teEnterpriseJSON = teEnterpriseJSONArray.getJSONObject(i);
                teEnterpriseJSON.keys().forEachRemaining(key -> {
                    Object value = teEnterpriseJSON.get(key);
                    String issueName = null;
                    if (!value.equals("True") && key.toString().equalsIgnoreCase("pageloadtime")) {
                        issueName = "TE_Ent_agent_pageloadtime";
                    } else if (!value.equals("True") && key.toString().equalsIgnoreCase("latency")) {
                        issueName = "TE_Ent_agent_Latency";
                    } else if (!value.equals("True") && key.toString().equalsIgnoreCase("loss")) {
                        issueName = "TE_Ent_agent_packet_loss";
                    } else if (!value.equals("True") && key.toString().equalsIgnoreCase("jitter")) {
                        issueName = "TE_Ent_agent_jitter";
                    } else if (!value.equals("True") && key.toString().equalsIgnoreCase("ssl")) {
                        issueName = "TE_Ent_agent_ssl_error";
                    }
                    if (issueName != null && !issueList.contains(issueName)) {
                        issueList.add(issueName);
                    }
                });
            }
            for (int i = 0; i < dnacJSONArray.length(); i++) {
                JSONObject dnacJSON = dnacJSONArray.getJSONObject(i);
                dnacJSON.keys().forEachRemaining(key -> {
                    Object value = dnacJSON.get(key);
                    String issueName = null;
                    if (!value.equals("True") && key.toString().equalsIgnoreCase("ap_health")) {
                        issueName = "DNAC_AP_Health";
                    } else if (!value.equals("True") && key.toString().equalsIgnoreCase("client_health")) {
                        issueName = "DNAC_Client_health";
                    }
                    if (issueName != null && !issueList.contains(issueName)) {
                        issueList.add(issueName);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return issueList;
    }

    public String droolsRulesEngine(List<String> issuesList) {
        System.out.println("Alerts:: " + issuesList);
        KieSession kieSession = kieContainer.newKieSession();
        BullseyeDroolsModel bullseyeDroolsModel = new BullseyeDroolsModel(issuesList);
        kieSession.insert(bullseyeDroolsModel);
        kieSession.fireAllRules();
        kieSession.dispose();
        return bullseyeDroolsModel.getRca();
    }

}













