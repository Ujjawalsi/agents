package com.example.agents.reports.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import com.example.agents.constant.Constant;
import com.example.agents.endpointAgent.EndPointAgentsService;
import com.example.agents.endpointAgent.EndpointAgentModel;
import com.example.agents.reports.entities.DnacClient;
import com.example.agents.reports.entities.ThousandEyeAlert;
import com.example.agents.reports.service.DnacClientService;
import com.example.agents.reports.service.EndPointAgentService;
import com.example.agents.reports.service.ThousandEyeAlertService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.example.agents.customDate.DateTimeUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.example.agents.thousandeye.bean.ApplicationPerformance;
import com.example.agents.thousandeye.bean.EndpointPerformance;
import com.example.agents.thousandeye.bean.GatewayConnectivity;
import com.example.agents.thousandeye.bean.NetworkPathToApplication;
import com.example.agents.thousandeye.bean.ThousandEyeAlertBean;
import com.example.agents.drools.config.model.BullseyeDroolsModel;
import com.example.agents.ldap.controller.AuthenticateUserAPI;
import com.example.agents.ldap.GetUserDetailFromLDAPByEmail;


import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@RestController
//@PropertySource("classpath:com/example/agents/vel/common/connector/service/constants.properties")
public class FetchIssues {
//    @Value("${Enterprise_Agent}")
//    String enterprise_Agent;


//    @Autowired
//    private KieContainer kieContainer;
    @Autowired
    private ThousandEyeAlertService thousandEyeAlertService;

    @Autowired
    private EndPointAgentService endPointAgentService;

    @Autowired
    private DnacClientService dnacClientService;

    @Autowired
    private EndPointAgentsService endPointAgentsService;

    static Logger root = (Logger) LoggerFactory
            .getLogger(Logger.ROOT_LOGGER_NAME);

    static {
        root.setLevel(Level.INFO);
    }
    //	static String question = "My WebEx Was not working at 3 PM";
    public static void main(String[] args) {

        FetchIssues issue = new FetchIssues();
        issue.getDomainName("ranvijay");
//
//		issue.getDnacData("vishal.bansal", "2022-11-21 11:30:00", "2022-11-21 12:30:00", "");
        //		2022-08-31 03:30:00.000 AM UTC
        //				issue.getUTCTime(14,"LAPTOP-EU8R3HO0");
    }
    //http://10.18.1.53:8080/BullsEye/fetchIssues?l_time=15&host_name=LAPTOP-EU8R3HO0&application=&start_time=2022-09-19%2013:00:00&end_time=2022-09-19%2017:29:48&email=
    @RequestMapping(value="/fetchIssues", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public String  fetchIssuesFromTE(@RequestParam(value="application", required=false) String application,
                                     @RequestParam(value="host_name", required=true) String agent_name,
                                     @RequestParam(value="start_time", required=true) String start_time,
                                     @RequestParam(value="end_time", required=true) String end_time,
                                     @RequestParam(value="email", required=true) String email
    ) throws JsonProcessingException {
        System.out.println("enterprise_Agent: "+ Constant.Enterprise_Agent);
        TimeZone timeZone = TimeZone.getTimeZone("IST");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(timeZone);

        String domainName = getDomainName(agent_name);
        System.out.println(domainName);
        if (domainName==null)
            domainName=agent_name;
        ThousandEyeAlertBean bean = getThousandEyeAlerts(start_time, end_time, ""+agent_name, application, domainName);
        List<DnacClient> _jsonDnac = dnacClientService.getDnacData(agent_name, start_time, end_time, email);
        bean = processDnacJSONData(bean, _jsonDnac);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(bean);
        System.out.println("JSON: "+json);
        JSONObject rtnString = new JSONObject(json);
        List<String>issuesList = createIssuesListEU(rtnString);
        System.out.println("IssuesList: "+issuesList);
        String rca = droolsRulesEngine(issuesList);
        System.out.println("Root Cause: "+rca);
        rtnString.put("rca", new JSONObject().put("value", rca));
        System.out.println("final JSON Reponse: "+rtnString.toString());
        return new JSONArray().put(rtnString).toString();
    }


//    public String getDomainName(String agent_name) {
//        MongoDBConnection mongoDB = new MongoDBConnection();
//        DB db;
//        String _domainName = null;
//        try {
//            db = mongoDB.getMongoConnection();
//            DBCollection collection = db.getCollection("ms_endpoint_agents");
//            BasicDBObject orQuery = new BasicDBObject();
//            List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
//            BasicDBObject fields = new BasicDBObject();
//            fields.put("clients.userProfile.userName", 1);
//            fields.put("agentName", 1);
//            fields.put("_id", 0);
//
//            Document regexUserName = new Document();
//            regexUserName.append("$regex", ".*" + Pattern.quote(agent_name) + ".*").append("$options","i");
//            obj.add(new BasicDBObject("clients.userProfile.userName", regexUserName));
//
//            Document regexAgentName = new Document();
//            regexAgentName.append("$regex", ".*" + Pattern.quote(agent_name) + ".*").append("$options","i");
//            obj.add(new BasicDBObject("agentName", regexUserName));
//            orQuery.put("$or", obj);
//            DBCursor cursor =  collection.find(orQuery, fields);
//            while(cursor.hasNext()) {
//                DBObject doc = cursor.next();
//                JSONObject json = new JSONObject(doc.toString());
//                System.out.println("json: "+json.toString());
////				_domainName = json.getJSONArray("clients").getJSONObject(0).getJSONObject("userProfile").getString("userName");
//                _domainName = json.getString("agentName");
//                System.out.println(_domainName);
//            }
//
//        } catch (UnknownHostException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return _domainName;
//    }


    public String getDomainName(String agent_name) {
        String _domainName = null;
        try {
            List<EndpointAgentModel> agentList = endPointAgentService.findByAgentName(agent_name);
            if (!agentList.isEmpty()) {
                EndpointAgentModel endpointAgent = agentList.get(0);// Assuming there's only one matching entry
                String jsonDocument = endpointAgent.getAgentData();
                System.out.println(jsonDocument);

                JSONObject jsonObject = new JSONObject(jsonDocument);
                _domainName = jsonObject.getString("agentName");
                System.out.println(_domainName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _domainName;
    }


    public String droolsRulesEngine(List<String>issuesList) {
        System.out.println("Alerts:: "+ issuesList);
//        KieSession kieSession = kieContainer.newKieSession();
        BullseyeDroolsModel bullseyeDroolsModel = new BullseyeDroolsModel(issuesList);
//        kieSession.insert(bullseyeDroolsModel);
//        kieSession.fireAllRules();
//        kieSession.dispose();
        //		return new JSONObject().put("The RCA for this application is ", bullseyeDroolsModel.getRca()).toString();
        return bullseyeDroolsModel.getRca();
    }

    private List<String> createIssuesListEU(JSONObject rtnString) {
        List<String> issueList = new ArrayList<>();
        System.out.println("rtnString:: "+rtnString);
        try {
            JSONArray endpoint_Performance = rtnString.getJSONArray("Endpoint Performance");
            JSONArray application_Infrastructure = rtnString.getJSONArray("Application Infrastructure");
            JSONArray network_Path_Application = rtnString.getJSONArray("Network Path to Application");
            JSONArray gateway_Connectivity = rtnString.getJSONArray("Gateway Connectivity");

            endpoint_Performance.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                Object o = obj.get("issue_name");
                System.out.println("Issue in End Point: "+o.toString());
                if (!JSONObject.NULL.equals(o)) {
                    String issue_name = (String) o;
                    System.out.println("issue_name " + issue_name);
                    if( issue_name.contains("CPU")) {
                        issue_name= "TE_endpoint_cpu_utilization";
                    }
                    else if( issue_name.contains("Memory")) {
                        issue_name= "TE_endpoint_memory_utilization";
                    }
                    else if (issue_name.contains("health")) {
                        issue_name="DNAC_Client_health";
                    }
                    if(!issueList.contains(issue_name)) {
                        System.out.println("Adding to issueList: "+ issue_name);
                        issueList.add(issue_name);
                    }
                }
            });

            gateway_Connectivity.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                Object o = obj.get("issue_name");
                System.out.println("Issue in End Point: "+o.toString());
                if (!JSONObject.NULL.equals(o)) {
                    String issue_name = (String) o;
                    if( issue_name.contains("Low Throughput")) {
                        issue_name= "TE_endpoint_throughput";
                    }

                    if(!issueList.contains(issue_name)) {
                        System.out.println("Adding to issueList: "+ issue_name);
                        issueList.add(issue_name);
                    }
                }
            });
            application_Infrastructure.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                Object o = obj.get("issue_name");
                System.out.println("Issue in End Point: "+o.toString());
                if (!JSONObject.NULL.equals(o)) {
                    String issue_name = (String) o;
                    if( issue_name.contains("Page Load")) {
                        issue_name= "TE_endpoint_pageloadtime";
                    }

                    if(!issueList.contains(issue_name)) {
                        System.out.println("Adding to issueList: "+ issue_name);
                        issueList.add(issue_name);
                    }
                }
            });
            network_Path_Application.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                Object o = obj.get("issue_name");
                System.out.println("Issue in End Point: "+o.toString());
                if (!JSONObject.NULL.equals(o)) {
                    String issue_name = (String) o;
                    System.out.println("issue_name " + issue_name);
                    if( issue_name.contains("Packet Loss")) {
                        issue_name= "TE_endpoint_packet_loss";
                    }
                    else if( issue_name.contains("Jitter")) {
                        issue_name= "TE_endpoint_Jitter";
                    }
                    else if (issue_name.contains("Latency")) {
                        issue_name="TE_endpoint_Latency";
                    }
                    if(!issueList.contains(issue_name)) {
                        System.out.println("Adding to issueList: "+ issue_name);
                        issueList.add(issue_name);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return issueList;
    }
//    private ThousandEyeAlertBean getThousandEyeAlerts(String startTime, String endTime, String agentName, String application, String domainName) {
//        ThousandEyeAlertBean bean = new ThousandEyeAlertBean();
//        try {
//            MongoDBConnection mongoDB = new MongoDBConnection();
//            DB db = mongoDB.getMongoConnection();
//            DBCollection collection = db.getCollection("ms_thousandeye_alert");
//            BasicDBObject gtQuery = new BasicDBObject();
//            BasicDBObject andQuery = new BasicDBObject();
//            List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
//            List<BasicDBObject> orQuery = new ArrayList<BasicDBObject>();
//            List<BasicDBObject> orDateQuery = new ArrayList<BasicDBObject>();
//
//            obj.add(new BasicDBObject("alert.dateStart",  new BasicDBObject("$lte", endTime)));
//
//
//
//            BasicDBObject datEndDBobject = new BasicDBObject( "alert.dateEnd", new BasicDBObject("$exists", false) );
//            BasicDBObject subscriberIdIsNull = new BasicDBObject( "alert.dateEnd", new BasicDBObject("$gt", startTime) );
//            orDateQuery.add( datEndDBobject );
//            orDateQuery.add( subscriberIdIsNull );
//            BasicDBObject firstOr = new BasicDBObject( "$or", orDateQuery );
//            obj.add(firstOr);
//
//
//            Document regexQuery = new Document();
//            regexQuery.append("$regex", ".*" + Pattern.quote(domainName) + ".*").append("$options","i");
//            obj.add(new BasicDBObject("alert.agents.agentName", regexQuery));
//
//            Document regexQuery1 = new Document();
//            regexQuery1.append("$regex", ".*" + Pattern.quote(application) + ".*").append("$options","i");
//            obj.add(new BasicDBObject("alert.testName", regexQuery1));
//            andQuery.put("$and", obj);
//            DBCursor cursor =  collection.find(andQuery);
//            JSONArray jsonarray = new JSONArray();
//            System.out.println(cursor.count());
//            while(cursor.hasNext()) {
//                DBObject doc = cursor.next();
//                jsonarray.put((new JSONObject(doc.toString())));
//            }
//            System.out.println(jsonarray.toString());
//            JSONArray appIssuesArr = new JSONArray();
//            if(application !=null && !application.isEmpty()) {
//                appIssuesArr = getApplicationIssues(startTime, endTime, agentName, application);
//            }
//
//            return processJSONData(jsonarray, agentName, application, appIssuesArr, domainName);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bean;
//    }




    public ThousandEyeAlertBean getThousandEyeAlerts(String startTime, String endTime, String agentName, String application, String domainName) {
        ThousandEyeAlertBean bean = new ThousandEyeAlertBean();
        try {
            List<ThousandEyeAlert> alertList = thousandEyeAlertService.findAlertsByCriteria(startTime, endTime, agentName, application, domainName);
            JSONArray jsonarray = new JSONArray();
            for (ThousandEyeAlert alert : alertList) {
//                jsonarray.put(new JSONObject(alert.getJsonData()));
                jsonarray.put(new JSONObject(alert.getAlert()));
                jsonarray.put(new JSONObject(alert.getId()));
                jsonarray.put(new JSONObject(alert.getNewEventId()));
                jsonarray.put(new JSONObject(alert.getEventId()));
                jsonarray.put(new JSONObject(alert.getEventType()));
            }

            JSONArray appIssuesArr = new JSONArray();
            if (application != null && !application.isEmpty()) {
                appIssuesArr = getApplicationIssues(startTime, endTime, agentName, application);
            }

            return processJSONData(jsonarray, agentName, application, appIssuesArr, domainName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }



//    private ThousandEyeAlertBean getThousandEyeAlerts1(String startTime, String endTime, String agentName, String application, String domainName) {
//        ThousandEyeAlertBean bean = new ThousandEyeAlertBean();
//        try {
//            MongoDBConnection mongoDB = new MongoDBConnection();
//            DB db = mongoDB.getMongoConnection();
//            DBCollection collection = db.getCollection("ms_thousandeye_alert");
//            BasicDBObject gtQuery = new BasicDBObject();
//            BasicDBObject andQuery = new BasicDBObject();
//            List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
//            List<BasicDBObject> orQuery = new ArrayList<BasicDBObject>();
//
//            gtQuery.put("alert.dateStart", new BasicDBObject("$gt", startTime).append("$lt", endTime));
//            obj.add(gtQuery);
//
////			Document regexQuery = new Document();
////			regexQuery.append("$regex", ".*" + Pattern.quote(agentName) + ".*").append("$options","i");
////			obj.add(new BasicDBObject("alert.agents.agentName", regexQuery));
//
//
//
//
//            Document regexAgentName = new Document();
//            regexAgentName.append("$regex", ".*" + Pattern.quote(agentName) + ".*").append("$options","i");
//
//            Document regexAgentName1 = new Document();
//            regexAgentName1.append("$regex", ".*" + Pattern.quote(domainName) + ".*").append("$options","i");
//
//
//
//            BasicDBObject subscriberId = new BasicDBObject( "alert.agents.agentName", regexAgentName );
//            BasicDBObject subscriberIdIsNull = new BasicDBObject( "alert.agents.agentName", regexAgentName1 );
//            orQuery.add( subscriberId );
//            orQuery.add( subscriberIdIsNull );
//            BasicDBObject firstOr = new BasicDBObject( "$or", orQuery );
//            obj.add(firstOr);
//
//
//
//            Document regexQuery1 = new Document();
//            regexQuery1.append("$regex", ".*" + Pattern.quote(application) + ".*").append("$options","i");
//            obj.add(new BasicDBObject("alert.testName", regexQuery1));
//
//            andQuery.put("$and", obj);
//            DBCursor cursor =  collection.find(andQuery);
//            JSONArray jsonarray = new JSONArray();
//            System.out.println(cursor.count());
//            while(cursor.hasNext()) {
//                DBObject doc = cursor.next();
//                jsonarray.put((new JSONObject(doc.toString())));
//            }
//            System.out.println(jsonarray.toString());
//            JSONArray appIssuesArr = new JSONArray();
//            if(application !=null && !application.isEmpty()) {
//                appIssuesArr = getApplicationIssues(startTime, endTime, agentName, application);
//            }
//
//            return processJSONData(jsonarray, agentName, application, appIssuesArr, domainName);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bean;
//    }




    public ThousandEyeAlertBean getThousandEyeAlerts1(String startTime, String endTime, String agentName, String application, String domainName) {
        ThousandEyeAlertBean bean = new ThousandEyeAlertBean();
        try {
            List<ThousandEyeAlert> alertList = thousandEyeAlertService.findAlertsByCriteria1(startTime, endTime, agentName, application, domainName);
            JSONArray jsonarray = new JSONArray();
            for (ThousandEyeAlert alert : alertList) {
//                jsonarray.put(new JSONObject(alert.getJsonData()));
                jsonarray.put(new JSONObject(alert.getId()));
                jsonarray.put(new JSONObject(alert.getAlert()));
                jsonarray.put(new JSONObject(alert.getEventType()));
                jsonarray.put(new JSONObject(alert.getNewEventId()));
                jsonarray.put(new JSONObject(alert.getEventId()));
            }

            JSONArray appIssuesArr = new JSONArray();
            if (application != null && !application.isEmpty()) {
                appIssuesArr = getApplicationIssues(startTime, endTime, agentName, application);
            }

            return processJSONData(jsonarray, agentName, application, appIssuesArr, domainName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }



//    private JSONArray getApplicationIssues(String startTime, String endTime, String enterpriseAgentName, String application) {
//        JSONArray jsonarray = new JSONArray();
//        try {
//            MongoDBConnection mongoDB = new MongoDBConnection();
//            DB db = mongoDB.getMongoConnection();
//            DBCollection collection = db.getCollection("ms_thousandeye_alert");
//            BasicDBObject gtQuery = new BasicDBObject();
//            BasicDBObject andQuery = new BasicDBObject();
//            List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
//            gtQuery.put("alert.dateStart", new BasicDBObject("$gt", startTime).append("$lt", endTime));
//            obj.add(gtQuery);
//            obj.add(new BasicDBObject("alert.agents.agentName", enterprise_Agent));
//
////			obj.add(new BasicDBObject("alert.ruleName", pattern));
////			Pattern p = Pattern.compile("(.)" + application + "(.)", Pattern.CASE_INSENSITIVE);
////			obj.add(new BasicDBObject("alert.testName",new BasicDBObject("$regex", p)));
//
//            Document regexQuery = new Document();
//            regexQuery.append("$regex", ".*" + Pattern.quote(application.toLowerCase()) + ".*");
//            obj.add(new BasicDBObject("alert.testName", regexQuery));
//
//            andQuery.put("$and", obj);
//            DBCursor cursor =  collection.find(andQuery);
//            System.out.println(cursor.count());
//            while(cursor.hasNext()) {
//                DBObject doc = cursor.next();
//                jsonarray.put((new JSONObject(doc.toString())));
//            }
//            System.out.println("getApplicationIssues: "+jsonarray.toString());
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return jsonarray;
//    }



    public JSONArray getApplicationIssues(String startTime, String endTime, String enterpriseAgentName, String application) {
        JSONArray jsonarray = new JSONArray();
        try {
            List<ThousandEyeAlert> alertList = thousandEyeAlertService.findAlertsForApplicationIssues(startTime, endTime, enterpriseAgentName, application);
            for (ThousandEyeAlert alert : alertList) {
//                jsonarray.put(new JSONObject(alert.getJsonData()));
                jsonarray.put(new JSONObject(alert.getId()));
                jsonarray.put(new JSONObject(alert.getAlert()));
                jsonarray.put(new JSONObject(alert.getEventId()));
                jsonarray.put(new JSONObject(alert.getEventType()));
                jsonarray.put(new JSONObject(alert.getNewEventId()));
            }
            System.out.println("getApplicationIssues: " + jsonarray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonarray;
    }






    //	private JSONObject processJSONData(JSONArray arrData, String agentName, String application, JSONArray appIssuesArr) {
    private ThousandEyeAlertBean processJSONData(JSONArray arrData, String agentName, String application, JSONArray appIssuesArr, String domainName) {
//		JSONObject rtnString = new JSONObject();
        int totCount = 0;
        ThousandEyeAlertBean bean = new ThousandEyeAlertBean();
        try {
            JSONArray _jsonResponse = arrData;
            List<EndpointPerformance> endpointPrfList = new ArrayList<EndpointPerformance>();
            List<ApplicationPerformance> appPrfList = new ArrayList<ApplicationPerformance>();
            List<NetworkPathToApplication> networkPathList = new ArrayList<NetworkPathToApplication>();
            List<GatewayConnectivity> gatewayConnectList = new ArrayList<GatewayConnectivity>();
            for (int i=0; i<_jsonResponse.length(); i++) {
                JSONObject _jObj = _jsonResponse.getJSONObject(i);
                JSONObject _alertJson =_jObj.getJSONObject("alert");
                JSONArray _agentsArray = _alertJson.getJSONArray("agents");
                System.out.println("_agentsArray: "+_agentsArray);
                for (int j=0; j<_agentsArray.length(); j++) {
                    EndpointPerformance endPoint = new EndpointPerformance();
                    ApplicationPerformance appPerf = new ApplicationPerformance();
                    NetworkPathToApplication networkApp = new NetworkPathToApplication();
                    GatewayConnectivity gatewayApp = new GatewayConnectivity();
                    JSONObject _json = new JSONObject();
                    String _agentName = _agentsArray.getJSONObject(j).getString("agentName");
//					if(_agentName.contains("\\")) {
//						_agentName = _agentName.split("\\\\")[1];
//					}
                    if(_agentName.equalsIgnoreCase(domainName)) {
                        //CPU/Memory/Client health/(Throughput)
                        String issueName = "";
                        String _startTime = "";
                        String _dateEnd = "";
                        if(_agentsArray.getJSONObject(j).has("metricsAtStart")) {
                            issueName = _agentsArray.getJSONObject(j).getString("metricsAtStart");
                            _startTime = _agentsArray.getJSONObject(j).getString("dateStart");

                            if(_jObj.has("dateEnd")) {
                                _dateEnd = _agentsArray.getJSONObject(j).getString("dateEnd");
                                _json.put("dateEnd", _dateEnd);
                            }else {
                                _json.put("dateEnd", "");
                            }
                        }else {
                            issueName = _alertJson.getString("ruleName");
                            _startTime =_alertJson.getString("dateStart");
                        }

                        if( issueName.contains("CPU")|| issueName.contains("Memory") ||
                                issueName.contains("health")) {
                            endPoint.setStartTime(_startTime);
                            endPoint.setDateEnd(_dateEnd);
                            endPoint.setIssueName("High "+issueName.split(":")[0]);
                            endPoint.setMessage("Endpoint for the given time is not performing optimally");
                            endpointPrfList.add(endPoint);

                        }else if(issueName.contains("Packet Loss")) {
                            networkApp.setStartTime(_startTime);
                            networkApp.setDateEnd(_dateEnd);
                            networkApp.setIssueName("High Packet Loss");
                            networkApp.setMessage("Path for the given time is not performing optimally");
                            networkPathList.add(networkApp);
                        }else if(issueName.contains("Jitter")) {
                            networkApp.setStartTime(_startTime);
                            networkApp.setDateEnd(_dateEnd);
                            networkApp.setIssueName("High Jitter");
                            networkApp.setMessage("Path for the given time is not performing optimally");
                            networkPathList.add(networkApp);
                        }else if(issueName.contains("Latency")) {
                            networkApp.setStartTime(_startTime);
                            networkApp.setDateEnd(_dateEnd);
                            networkApp.setIssueName("High Latency");
                            networkApp.setMessage("Path for the given time is not performing optimally");
                            networkPathList.add(networkApp);
                        }
                        else if(issueName.contains("Throughput")) {
                            gatewayApp.setStartTime(_startTime);
                            gatewayApp.setDateEnd(_dateEnd);
                            gatewayApp.setIssueName("Low Throughput");
                            gatewayApp.setMessage("Gateway Connectivity for the given time is not performing optimally");
                            gatewayConnectList.add(gatewayApp);
                        }else if(issueName.contains("Page Load")) {
                            appPerf.setStartTime(_startTime);
                            appPerf.setDateEnd(_dateEnd);
                            appPerf.setIssueName("High Page Load Time");
                            appPerf.setMessage("Application for the given time is not performing optimally");
                            appPrfList.add(appPerf);
                        }
                    }
                }
            }
            for (int i=0; i<appIssuesArr.length(); i++) {
                JSONObject _jObj = appIssuesArr.getJSONObject(i);
                JSONObject _alertJson =_jObj.getJSONObject("alert");
                JSONArray _agentsArray = _alertJson.getJSONArray("agents");
                for (int j=0; j<_agentsArray.length(); j++) {
                    JSONObject _json = new JSONObject();
                    ApplicationPerformance appPerf = new ApplicationPerformance();
                    String issueName = "";
                    String _startTime = "";
                    String _dateEnd = "";
                    if(_agentsArray.getJSONObject(j).has("metricsAtStart")) {
                        issueName = _agentsArray.getJSONObject(j).getString("metricsAtStart");
                        _startTime = _agentsArray.getJSONObject(j).getString("dateStart");

                        if(_jObj.has("dateEnd")) {
                            _dateEnd = _agentsArray.getJSONObject(j).getString("dateEnd");
                            _json.put("dateEnd", _dateEnd);
                        }else {
                            _json.put("dateEnd", "");
                        }
                    }else {
                        issueName = _alertJson.getString("ruleName");
                        _startTime =_alertJson.getString("dateStart");
                    }

                    if(issueName.contains("Page Load")) {
                        appPerf.setStartTime(_startTime);
                        appPerf.setDateEnd(_dateEnd);
                        appPerf.setIssueName("High Page Load Time");
                        appPerf.setMessage("Application for the given time is not performing optimally");
                        appPrfList.add(appPerf);
                    }
                }
            }
            if(appPrfList.size()>0) {
                bean.setApplicationPerformance(appPrfList);
                totCount = totCount+appPrfList.size();
            }
            else {
                ApplicationPerformance appPerf1 = new ApplicationPerformance();
                appPerf1.setMessage("Application for the given time is Good.");
                appPrfList.add(appPerf1);
                bean.setApplicationPerformance(appPrfList);
            }
            if(endpointPrfList.size()>0) {
                bean.setEndpointPerformance(endpointPrfList);
                totCount = totCount+endpointPrfList.size();
            }
            else{
                EndpointPerformance endPoint1 = new EndpointPerformance();
                endPoint1.setMessage("EndPoint for the given time is Good.");
                endpointPrfList.add(endPoint1);
                bean.setEndpointPerformance(endpointPrfList);
            }
            if(gatewayConnectList.size()>0) {
                bean.setGatewayConnectivity(gatewayConnectList);
                totCount = totCount+gatewayConnectList.size();
            }
            else{
                GatewayConnectivity gateway = new GatewayConnectivity();
                gateway.setMessage("Gateway for the given time is Good");
                gatewayConnectList.add(gateway);
                bean.setGatewayConnectivity(gatewayConnectList);
            }
            if(networkPathList.size()>0) {
                bean.setNetworkPathToApplication(networkPathList);
                totCount = totCount+gatewayConnectList.size();
            }
            else{
                NetworkPathToApplication nwpath = new NetworkPathToApplication();
                nwpath.setMessage("Path for the given time is Good");
                networkPathList.add(nwpath);
                bean.setNetworkPathToApplication(networkPathList);
            }
            //			bean.setCount(totCount);
//			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//			String json = ow.writeValueAsString(bean);
//			System.out.println("JSON: "+json);
//			rtnString = new JSONObject(json);
//			System.out.println("final JSON Reponse: "+rtnString.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
//		return rtnString;
        return bean;
    }






    @RequestMapping(value="/fetchDnacIssues", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public String  fetchIssuesFromDnac(@RequestParam(value="application", required=false) String application,
                                       @RequestParam(value="user_name", required=true) String user_name,
                                       @RequestParam(value="start_time", required=true) String start_time,
                                       @RequestParam(value="end_time", required=true) String end_time,
                                       @RequestParam(value="email", required=true) String email
    ) {
        DateTimeUtil _util = new DateTimeUtil();

        start_time = _util.convertIST2UTC(start_time);
        end_time = _util.convertIST2UTC(end_time);
        dnacClientService.getDnacData(user_name, start_time, end_time, email);
        return email;

    }


    @RequestMapping(value="/fetchUserInfo", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public String  fetchUserInfo(
            @RequestParam(value="user_name", required=true) String user_name
    ) {
//        MongoDBConnection mongoDB = new MongoDBConnection();
        JSONArray jsonarray = new JSONArray();
        try {
            String user = user_name;
            Properties prop = new Properties();
            InputStream input=AuthenticateUserAPI.class.getClassLoader().getResourceAsStream("com/example/agents/vel/ldap/ldap.properties");
            try {
                prop.load(input);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String flag = prop.getProperty("isLdap");
            String domain = prop.getProperty("DOMAIN");
            if (flag.equals("true")) {
                GetUserDetailFromLDAPByEmail l = new GetUserDetailFromLDAPByEmail();
                user_name = l.getEmailDetailFromLdap(user_name+"@"+domain);

            }

            if(user_name == "" || user_name == null) {
                user_name= user;
            }
            System.out.println("user_name : "+user_name);
            String domainName = getDomainName(user_name);
            if (domainName==null)
                domainName=user_name;
            System.out.println("domainName : "+domainName);
//            DB db = mongoDB.getMongoConnection();
//            DBCollection collection = db.getCollection("ms_endpoint_agents");
//            Document regexQuery = new Document();
//            regexQuery.append("$regex", ".*" + Pattern.quote(domainName) + ".*").append("$options","i");
//            BasicDBObject criteria = new BasicDBObject("agentName", regexQuery);
//            DBCursor cursor = collection.find(criteria);
//            while(cursor.hasNext()) {
//                BasicDBObject obj1 = (BasicDBObject) cursor.next();
//                jsonarray.put(obj1);
////				JSONObject o = new JSONObject(obj1.toJson());
////				String name = o.getString("agentName");
////				String os = o.getString("osVersion");
////				JSONArray networkArr = o.getJSONArray("networkInterfaceProfiles");
////				for (int i = 0; i < networkArr.length(); i++) {
////					if(networkArr.getJSONObject(i).getString("hardwareType").equalsIgnoreCase("WIRELESS")) {
////
////					}
////				}
//            }

            List<EndpointAgentModel> endPointAgentList = endPointAgentService.findByAgentNameContainingIgnoreCase(domainName);
            for (EndpointAgentModel agent: endPointAgentList) {
                jsonarray.put(agent);

            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return jsonarray.toString();

    }

    private ThousandEyeAlertBean processDnacJSONData(ThousandEyeAlertBean bean, List<DnacClient> _jsonDnac) {
        // TODO Auto-generated method stub
        List<EndpointPerformance> endpointPrfList = bean.getEndpointPerformance();
        try {
            for (DnacClient dnacClient : _jsonDnac) {
                if(endpointPrfList.size()>0)
                    endpointPrfList.remove(0);
                EndpointPerformance endPoint = new EndpointPerformance();
//                JSONObject _json = _jsonDnac.getJSONObject(i);
                JSONArray _json = new JSONArray(dnacClient.getJsonDocument());
                int averageHealthScore_min = Integer.parseInt(_json.getString(Integer.parseInt("averageHealthScore_min")));
//				if(averageHealthScore_min>5 && averageHealthScore_min < 8) {
//					endPoint.setStartTime(_json.getString("start_Time"));
//					endPoint.setDateEnd(_json.getString("end_Time"));
//					endPoint.setIssueName("Fair Health: "+ averageHealthScore_min);
//					endPoint.setMessage("Endpoint for the given time is not performing optimally");
//				}else
                if(averageHealthScore_min>7) {
                    endPoint.setStartTime(_json.getString(Integer.parseInt("start_Time")));
                    endPoint.setDateEnd(_json.getString(Integer.parseInt("end_Time")));
                    endPoint.setIssueName("Fair Health: "+ averageHealthScore_min);
                    endPoint.setMessage("Endpoint for the given time is performing optimally");
                }
                else {
                    endPoint.setStartTime(_json.getString(Integer.parseInt("start_Time")));
                    endPoint.setDateEnd(_json.getString(Integer.parseInt("end_Time")));
                    endPoint.setIssueName("Poor Health: "+ averageHealthScore_min);
                    endPoint.setMessage("Endpoint for the given time is not performing optimally");

                }
                endpointPrfList.add(endPoint);
//				String rssi_median = _json.getString("rssi_median");
//				String snr_median = _json.getString("snr_median");

            }

            bean.setEndpointPerformance(endpointPrfList);
        }catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return bean;
    }


}




