package com.example.agents.reports.implementation;

import ch.qos.logback.core.net.ObjectWriter;
import com.example.agents.customDate.DateTimeUtil;
import com.example.agents.drools.config.model.BullseyeDroolsModel;
import com.example.agents.inventoryData.InventoryDataModel;
import com.example.agents.inventoryData.InventoryDataRepository;
import com.example.agents.reports.controller.CreateReports;
import com.example.agents.reports.controller.FetchIssues;
import com.example.agents.reports.controller.FetchIssuesForITAdmin;
import com.example.agents.reports.entities.Constant;
import com.example.agents.reports.entities.ThousandEyeAlert;
import com.example.agents.reports.repository.ThousandEyeAlertRepo;
import com.example.agents.reports.service.ThousandEyeAlertService;
import com.example.agents.thousandeye.itadmin.bean.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vel.common.connector.service.impl.BUSAPIConnectorImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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




    @Autowired
    public ThousandEyeAlertRepo thousandEyeAlertRepo;


    BUSAPIConnectorImpl service = new BUSAPIConnectorImpl();


    DateTimeUtil timeutil = new DateTimeUtil();




//    @Autowired
//    private KieContainer kieContainer;


    @Autowired
    private InventoryDataRepository inventoryDataRepository;


    @Autowired
     private CreateReports reports;





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
        System.out.println(thousandEyeAlert);
        thousandEyeAlertRepo.save(thousandEyeAlert);
        System.out.println("Inserted into db");

    }

    @Override
    public List<ThousandEyeAlert> findAlertsByCriteria(String startTime, String endTime, String agentName, String application, String domainName) {
     return thousandEyeAlertRepo.findAlertsByCriteria(startTime, endTime, agentName, application, domainName);

    }

    @Override
    public List<ThousandEyeAlert> findAlertsByCriteria1(String startTime, String endTime, String agentName, String application, String domainName) {
       return thousandEyeAlertRepo.findAlertsByCriteria1(startTime, endTime, agentName, application, domainName);
    }

    @Override
    public List<ThousandEyeAlert> findAlertsForApplicationIssues(String startTime, String endTime, String enterpriseAgentName, String application) {
        return thousandEyeAlertRepo.findAlertsForApplicationIssues(startTime, endTime, enterpriseAgentName, application);
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
            System.out.println("user/pass:: " + _decodePass);
            String[] _userPassword = _decodePass.split(":");
            String _username = _userPassword[0];
            String _password = _userPassword[1];
            System.out.println("Username ---->>>" + _username);
            System.out.println("password ---->" + _password);

//            Constant constant = new Constant();
//            System.out.println("Constant Username ---->>>" + constant.USERNAME);
//            System.out.println("COnstant password ------>>" + constant.PASSWORD);
//
//            System.out.println("Constant p Username ---->>>" + constant.getDnacUserName());
//            System.out.println("COnstant p  password ------>>" + constant.getDnacPassword());
//
//            System.out.println("Constant p Username ---->>>" + constant.getName());
//            System.out.println("COnstant p  password ------>>" + constant.getPass());


           if (_username.equalsIgnoreCase(Constant.USERNAME) && _password.equalsIgnoreCase(Constant.PASSWORD)) {
            _body = getBody(l_request);
            JSONObject _json = new JSONObject(_body);
            String eventType = _json.getString("eventType");
            String eventId = _json.getString("eventId");
            System.out.println("eventId: " + eventId);
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
                System.out.println("Final Json Object: " + _json.toString());
                if (!_json.has("browserSessionAlert")) {
                    String jsonString = _json.toString();
                    System.out.println("AWAWW-->>>00" + jsonString);
                    ThousandEyeAlert thousandEyeAlert = new ThousandEyeAlert();
//                       thousandEyeAlert .setData(jsonString);
//                      thousandEyeAlert.setId(1L);
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
    public List<ThousandEyeAlert> getAlertsBySeverityAndDate(String sevtype, Date date) {

        if ("0".equals(sevtype)) {
            return thousandEyeAlertRepo.findBySeverityAndDateLessThanEqual("Critical", date);
        } else if ("1".equals(sevtype)) {
            return thousandEyeAlertRepo.findBySeverityAndDateLessThanEqual("Major", date);
        } else if ("2".equals(sevtype)) {
            return thousandEyeAlertRepo.findBySeverityAndDateLessThanEqual("Minor", date);
        } else if ("3".equals(sevtype)) {
            return thousandEyeAlertRepo.findBySeverityAndDateLessThanEqual("Info", date);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<ThousandEyeAlert> findBySeverityAndDateStartZonedBetween(String critical, String seconddate, String firstdate) {
        return thousandEyeAlertRepo.findBySeverityAndDateStartZonedBetween(critical, seconddate, firstdate);
    }

    @Override
    public List<ThousandEyeAlert> getCountBySeverity(String critical, int i, Date date) {

        return thousandEyeAlertRepo.getCoudnByServerity(critical,i,date);
    }

    @Override
    public List<ThousandEyeAlert> combineAlertLists(List<ThousandEyeAlert> critlist, List<ThousandEyeAlert> majlist, List<ThousandEyeAlert> minlist, List<ThousandEyeAlert> infolist) {
        critlist.addAll(majlist);
        critlist.addAll(minlist);
        critlist.addAll(infolist);
        return critlist;
    }

    @Override
    public void addAlertName(List<ThousandEyeAlert> sevalert, HashSet<String> appname, List<ThousandEyeAlert> allalerts) {
        for (int i = 0; i < sevalert.size(); i++) {
            allalerts.add(sevalert.get(i));
            ThousandEyeAlert dbobj= sevalert.get(i);
            String value = dbobj.getAlert();
            JSONObject jsonObject =new JSONObject(value);
            String fieldValue = jsonObject.getString("testName");
            System.out.println(fieldValue);
            appname.add(fieldValue);
        }

    }

    @Override
    public Set<String> extractAppNames(List<ThousandEyeAlert> alerts, HashSet<String> appname) {
        return null;
    }

    @Override
    public List<ThousandEyeAlert> getAlertsById(List<ThousandEyeAlert> alerts, String id) {
        return null;
    }

    @Override
    public List<ThousandEyeAlert> filterAlertsByName(List<ThousandEyeAlert> alerts, String name) {
        return null;
    }

    @Override
    public JSONArray categorizeAlerts(List<ThousandEyeAlert> allalerts, HashSet<String> appname) {
        JSONArray categorize = new JSONArray();
        for (String element : appname) {
            JSONObject newapp = new JSONObject();
            int critical = 0;
            int major = 0;
            int minor = 0;
            int info = 0;
            for (ThousandEyeAlert alert : allalerts) {
//                String fieldValue = alert.getTestName();
//                String severity = alert.getSeverity();

                JSONObject jsonObject = new JSONObject(alert);
                String fieldValue = jsonObject.getString("testName");
                String severity = jsonObject.getString("severity");
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
        System.out.println("Agent_name in getalerts: " + agentName);

        JSONArray _jsonDnac = new FetchIssues().getDnacData(agentName, startTime, endTime, application);
        System.out.println("Data from Dnac: " + _jsonDnac);

        try {
            List<ThousandEyeAlert> alerts = thousandEyeAlertRepo.findAlertsByFilters(startTime, endTime, agentName, application, domainName);
            JSONArray jsonarray = new JSONArray();
            for (ThousandEyeAlert alert : alerts) {
                jsonarray.put(new JSONObject(alert.toString()));
            }
            System.out.println(jsonarray.toString());

            JSONArray appIssuesArr = new JSONArray();
            checkAlerts(startTime, endTime, application);
            if (application != null && !application.isEmpty()) {
                appIssuesArr = getApplicationIssues(startTime, endTime, agentName, application);
            }

            ITAdminBean bean = processJSONData(jsonarray, agentName, application, appIssuesArr, domainName, startTime,
                    endTime);
            bean = processDnacJSONDataAdmin(bean, _jsonDnac);
            System.out.println("Final Bean:: " + bean.toString());

            ObjectWriter ow = (ObjectWriter) new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ((com.fasterxml.jackson.databind.ObjectWriter) ow).writeValueAsString(bean);
            rtnString = new JSONObject(json);

            List<String> issuesList = createIssuesList(rtnString);
            String rca = droolsRulesEngine(issuesList);
            System.out.println("issuesList: " + issuesList);
            System.out.println("Root Cause: " + rca);
            rtnString.put("rca", new JSONObject().put("value", rca));
            System.out.println("final JSON Reponse: " + rtnString.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnString;
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
            Properties prop = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("classpath:com/vel/common/connector/service/constants.properties");
            prop.load(input);
            String Enterprise_Agent = prop.getProperty("Enterprise_Agent");


            Properties props = new Properties();
            InputStream inputs = getClass().getClassLoader().getResourceAsStream("classpath:com/vel/common/connector/service/constants.properties");
            prop.load(inputs);
            te_api_key = prop.getProperty("te_api_key");
            te_alert_api = prop.getProperty("te_alert_api");
            try {
                List<ThousandEyeAlert> alerts = thousandEyeAlertRepo.findAlertsByTimeRangeAndApplication(startTime, endTime, application);
                System.out.println("getApplicationIssues: " + alerts);

                FetchIssuesForITAdmin fs = new FetchIssuesForITAdmin();
                for (ThousandEyeAlert alert : alerts) {
                    String newEventId = alert.getNewEventId();
                    System.out.println("Alert Id to update : " + newEventId);
                    checkUpdate(newEventId, te_alert_api, te_api_key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    public void checkUpdate(String newEventId, String te_alert_api, String te_api_key) throws Exception {
        FetchIssuesForITAdmin fs = new FetchIssuesForITAdmin();
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
                    System.out.println("Date in UTC is :" + _dateEndUTC);
                    String dateEnd = timeutil.convertUTC2IST(_dateEndUTC);
                    System.out.println("Date in IST is: " + dateEnd);
                    alertobj.remove("dateEnd");
                    alertobj.put("dateEnd", dateEnd);
                    updateRecord(newEventId, alertobj);
                    System.out.println(newEventId + " :: Record updated :" + alertobj);

                }

            }

        }

    }



    public JSONArray getApplicationIssues(String startTime, String endTime, String enterpriseAgentName,
                                          String application) throws IOException {
        JSONArray jsonarray = new JSONArray();

        Properties prop = new Properties();
        InputStream input=getClass().getClassLoader().getResourceAsStream("classpath:com/vel/common/connector/service/constants.properties");
        prop.load(input);
        String Enterprise_Agent = prop.getProperty("Enterprise_Agent");
        try {
            List<ThousandEyeAlert> alerts = thousandEyeAlertRepo.findApplicationIssues(startTime, endTime,
                    Enterprise_Agent, application);

            for (ThousandEyeAlert alert : alerts) {
                jsonarray.put(new JSONObject(alert.getAlert()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonarray;
    }




    private synchronized ITAdminBean processJSONData(JSONArray arrData, String agentName, String application,
                                                     JSONArray appIssuesArr, String domainName, String startTime, String endTime) {
        ITAdminBean bean = new ITAdminBean();
        try {
            JSONArray _jsonResponse = arrData;
            CloudWatch cloudWatch = new CloudWatch();
            List<ThousandEndpoint> thousandEndpointList = new ArrayList<ThousandEndpoint>();
            List<ThousandEnterprise> thousandEnterpriseList = new ArrayList<ThousandEnterprise>();

            Vm vm = new Vm();
            UserInfo userInfo = new UserInfo();
            List<DnacEndpoint> endPoint = new ArrayList<DnacEndpoint>();

//System.out.println("Json type")
            for (int i = 0; i < _jsonResponse.length(); i++) {
                JSONObject _jObj = _jsonResponse.getJSONObject(i);
                JSONObject _alertJson = _jObj.getJSONObject("alert");
                JSONArray _agentsArray = _alertJson.getJSONArray("agents");
                System.out.println("_agentsArray: " + _agentsArray);
//	if((_agentsArray.length())==0) {
//
//
//
//	thousandEndpointList.clear();
//	}
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
                            } else if (_alertJson.has("dateEnd")) {
                                _dateEnd = _alertJson.getString("dateEnd");
                            } else {
                                _dateEnd = "NA";
                            }
                        } /*
                         * else { issueName = _alertJson.getString("ruleName"); _startTime
                         * =_alertJson.getString("dateStart"); }
                         */
                        thousandEndpoint.setIssueTime(_startTime + " | " + _dateEnd);
                        if (issueName.contains("CPU") || issueName.contains("Memory") || issueName.contains("health")) {
                            String[] issues = issueName.split(":");
                            if (issues[0].contains("CPU")) {
                                thousandEndpoint.setCpuUtilization(issues[1].trim());
                            }
                            // else {
                            // thousandEndpoint.setCpuUtilization("True");
                            // }

                            if (issues[0].contains("Memory")) {
                                thousandEndpoint.setMemoryUtilization(issues[1].trim());
                            }
                            // else {
                            //
                            // thousandEndpoint.setMemoryUtilization("True");
                            // }

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
                            // thousandEndpoint.setPageloadtime("True");
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
                            // _dateEnd = _agentsArray.getJSONObject(j).getString("dateEnd");
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
            if (Constant.awsFlag)
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




    private ITAdminBean processDnacJSONDataAdmin(ITAdminBean bean, JSONArray _jsonDnac) {
        // TODO Auto-generated method stub
        // DnacEndpoint endpoint_dnac = bean.getDnacEndpoint();
        List<DnacEndpoint> dnacDataList = new ArrayList<DnacEndpoint>();
        DateTimeUtil timeUtil = new DateTimeUtil();
        System.out.println("Length of data recieved from DNAC: " + _jsonDnac.length());
        if ((_jsonDnac.length()) == 0) {
            dnacDataList.clear();
            ;
            bean.setDnacEndpoint(dnacDataList);
            return bean;
        }
        try {
            for (int i = 0; i < _jsonDnac.length(); i++) {
                DnacEndpoint endpoint_dnac = new DnacEndpoint();
                JSONObject _json = _jsonDnac.getJSONObject(i);
                int averageHealthScore_min = Integer.parseInt(_json.getString("averageHealthScore_min"));
//	String apMac = _json.getString("apMac");
                String connectedDeviceName = _json.getString("connectedDeviceName");
                JSONObject _jsonConnected = getMacFromPostgres(connectedDeviceName);
                String apMac = _jsonConnected.getString("macAddress");
//	String siteId = _json.getString("siteId");
                String start_Time = _json.getString("start_Time");
                String end_Time = _json.getString("end_Time");
                long startTime = timeUtil.convertLongDate(start_Time);
                long endTime = timeUtil.convertLongDate(end_Time);
                endpoint_dnac.setIssueTime(start_Time + "|" + end_Time);
//	int overallHealth =processDNACClientHealth(apMac, startTime, endTime, siteId);
                int overallHealth = processDNACClientHealth(apMac, startTime, endTime);
                if (overallHealth == -1)
                    endpoint_dnac.setAp_health("Device Not Found");
                else if (overallHealth > 7) {
                    endpoint_dnac.setAp_health("True");
//	}else if(overallHealth>7) {
//	endpoint_dnac.setAp_health("True");
                } else
                    endpoint_dnac.setAp_health(overallHealth + "");
                if (averageHealthScore_min > 7) {
                    endpoint_dnac.setClient_health("True");
//	}else if(averageHealthScore_min>4 ) {
//	endpoint_dnac.setClient_health("True");
                } else {
                    endpoint_dnac.setClient_health(averageHealthScore_min + "");
                }
                dnacDataList.add(endpoint_dnac);
                // String rssi_median = _json.getString("rssi_median");
                // String snr_median = _json.getString("snr_median");

            }
            bean.setDnacEndpoint(dnacDataList);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return bean;
    }



    public JSONObject getMacFromPostgres(String connectedDeviceName) {
        JSONObject _json = null;
        System.out.println("ConnectedDeviceName: " + connectedDeviceName);
        if (connectedDeviceName == null) {
            connectedDeviceName = "";
        }
        System.out.println("ConnectedDeviceName: " + connectedDeviceName);
        try {
            InventoryDataModel inventory = inventoryDataRepository.findByHostname(connectedDeviceName);
            if (inventory != null) {
                _json = new JSONObject(inventory.getData());
            } else {
                System.out.println("Device not found in inventory!");
            }
            System.out.println("getMacFromPostgres: " + _json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _json;
    }




    public int processDNACClientHealth(String apMac, long startTime, long endTime) {
        // CreateReports reports = new CreateReports();

        int overallHealth = -1;
        try {
            String _token = reports.getAuthTokenFromDNAC();
            HttpHeaders _headers = reports.createHeaders(_token);
            // String _url =
            // "https://10.6.1.25/dna/intent/api/v1/device-health?deviceRole=AP&startTime="+startTime+"&endTime="+endTime+"&siteId="+siteId;
//	String _url = dnac_health_api+startTime+"&endTime="+endTime+"&siteId="+siteId;
            String _url = Constant.dnac_health_api + startTime + "&endTime=" + endTime;
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
            // boolean awsFlag = false;
            JSONArray teEndPointJSONArray = rtnString.getJSONArray("thousand_endpoint");
            // JSONObject teEndPointJSON = rtnString.getJSONObject("thousand_endpoint");
            // JSONObject teEnterpriseJSON = rtnString.getJSONObject("thousand_enterprise");
            JSONArray teEnterpriseJSONArray = rtnString.getJSONArray("thousand_enterprise");
            JSONArray dnacJSONArray = rtnString.getJSONArray("dnac_endpoint");
            // JSONObject dnacJSON = rtnString.getJSONObject("dnac_endpoint");

            if (Constant.awsFlag) {
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
                    System.out.println(value + "Key:: " + key);
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
//        KieSession kieSession = kieContainer.newKieSession();
       BullseyeDroolsModel bullseyeDroolsModel = new BullseyeDroolsModel(issuesList);
//        kieSession.insert(bullseyeDroolsModel);
//        kieSession.fireAllRules();
//        kieSession.dispose();
        // return new JSONObject().put("The RCA for this application is ",
        // bullseyeDroolsModel.getRca()).toString();
        return bullseyeDroolsModel.getRca();
    }

}












