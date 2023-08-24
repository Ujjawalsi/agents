package com.example.agents.reports.controller;

import com.example.agents.reports.entities.DnacClient;
import com.example.agents.reports.service.DnacClientService;
import com.example.agents.customDate.DateTimeUtil;
import com.vel.common.connector.service.IBUSAPIConnectorService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@EnableScheduling
@PropertySource("classpath:com/vel/common/connector/service/dnacconstants.properties")
public class CreateReports {

    @Value("${dnac_auth_api}")
    String dnac_auth_api;

    @Value("${dnac_user_name}")
    String dnac_user_name;

    @Value("${dnac_password}")
    String dnac_password;

    @Value("${dnac_report_api}")
    String dnac_report_api;

//	@Value("${user_ssid}")
//	String user_ssid;

    @Autowired
    IBUSAPIConnectorService service;
    @Autowired
    private DnacClientService dnacClientService;

//    @Scheduled(cron = "0 */15 * ? * *")
//@Scheduled(cron = "0 */1 * * * ?")
public void create() {
        System.out.println("Auto Scheduler Start :: ::  ");
        createReportInDNAC("Scheduler BullsEye");
    }



    //	@RequestMapping(value="/reports", method=RequestMethod.GET)
//	@ResponseBody
//	public void createReportInDNAC(@RequestParam(value="l_name", required=true) String l_name) {
    public void createReportInDNAC(String l_name) {
        String jsonPayLoad ="{\"tags\": []," +
                "    \"deliveries\": [{"
                + "\"type\": \"WEBHOOK\","
                + "\"webhookId\": \"f6c09c52-c747-4ea3-a9ac-abe1f65222\","
                + "\"default\": false"
                + "}]," +
                "    \"name\": \""+l_name+"\"," +
                "\"dataCategory\": \"Clients\"," +

                "    \"schedule\": {" +
                "        \"type\": \"SCHEDULE_NOW\"" +
                "    }," +
                "    \"view\": {" +
                "        \"name\": \"Client Detail\"," +
                "        \"description\": \"This client report view provides detailed information about the list of clients that are seen in the network\"," +
                "        \"fieldGroups\": [" +
                "            {" +
                "                \"fieldGroupName\": \"client_details\"," +
                "                \"fields\": [" +
                "                    {" +
                "                        \"name\": \"hostName\"," +
                "                        \"displayName\": \"Host Name\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"username\"," +
                "                        \"displayName\": \"User ID\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"macAddress\"," +
                "                        \"displayName\": \"MAC Address\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"ipv4\"," +
                "                        \"displayName\": \"IPv4 Address\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"ipv6\"," +
                "                        \"displayName\": \"IPv6 Address\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"deviceType\"," +
                "                        \"displayName\": \"Device Type\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"connectionStatus\"," +
                "                        \"displayName\": \"Current Status\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"averageHealthScore_min\"," +
                "                        \"displayName\": \"Min Health Score\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"averageHealthScore_max\"," +
                "                        \"displayName\": \"Max Health Score\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"averageHealthScore_median\"," +
                "                        \"displayName\": \"Median Health Score\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"usage_sum\"," +
                "                        \"displayName\": \"Usage (MB)\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"connectedDeviceName\"," +
                "                        \"displayName\": \"Connected Device Name\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"frequency\"," +
                "                        \"displayName\": \"Band\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"rssi_median\"," +
                "                        \"displayName\": \"RSSI (dBm)\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"snr_median\"," +
                "                        \"displayName\": \"SNR (dB)\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"site\"," +
                "                        \"displayName\": \"Last Location\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"lastUpdated\"," +
                "                        \"displayName\": \"Last Seen\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"apGroup\"," +
                "                        \"displayName\": \"AP Group\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"ssid\"," +
                "                        \"displayName\": \"SSID\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"vlan\"," +
                "                        \"displayName\": \"VLAN ID\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"vnid\"," +
                "                        \"displayName\": \"VNID\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"onboardingEventTime\"," +
                "                        \"displayName\": \"Onboarding Time\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"assocDoneTimestamp\"," +
                "                        \"displayName\": \"Association Time\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"authDoneTimestamp\"," +
                "                        \"displayName\": \"Authentication Time\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"aaaServerIp\"," +
                "                        \"displayName\": \"Authentication Server\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"dhcpDoneTimestamp\"," +
                "                        \"displayName\": \"Last DHCP Request\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"maxDhcpDuration_max\"," +
                "                        \"displayName\": \"DHCP Response Time (ms)\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"dhcpServerIp\"," +
                "                        \"displayName\": \"DHCP Server\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"linkSpeed\"," +
                "                        \"displayName\": \"Link Speed (Mbps)\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"txRate_min\"," +
                "                        \"displayName\": \"Min Tx Rate (bps)\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"txRate_max\"," +
                "                        \"displayName\": \"Max Tx Rate (bps)\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"txRate_avg\"," +
                "                        \"displayName\": \"Average Tx Rate (bps)\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"rxRate_min\"," +
                "                        \"displayName\": \"Min Rx Rate (bps)\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"rxRate_max\"," +
                "                        \"displayName\": \"Max Rx Rate (bps)\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"rxRate_avg\"," +
                "                        \"displayName\": \"Average Rx Rate (bps)\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"txBytes_sum\"," +
                "                        \"displayName\": \"Tx (MB)\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"rxBytes_sum\"," +
                "                        \"displayName\": \"Rx (MB)\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"dataRate_median\"," +
                "                        \"displayName\": \"Data Rate (Mbps)\"" +
                "                    }," +
                "                    {" +
                "                        \"name\": \"dot11Protocol\"," +
                "                        \"displayName\": \"Client Protocol\"" +
                "                    }" +
                "                ]" +
                "            }" +
                "        ]," +
                "        \"filters\": [" +
                "            {" +
                "                \"type\": \"MULTI_SELECT_TREE\"," +
                "                \"name\": \"Location\"," +
                "                \"displayName\": \"Location\"," +
                "                \"value\": []" +
                "            }," +
                "            {" +
                "                \"type\": \"SINGLE_SELECT_ARRAY\"," +
                "                \"name\": \"DeviceType\"," +
                "                \"displayName\": \"Device Type\"," +
                "                \"value\": []" +
                "            }," +
                "            {" +
                "                \"type\": \"MULTI_SELECT\"," +
                "                \"name\": \"SSID\"," +
                "                \"displayName\": \"SSID\"," +
                "				\"value\": []" +
//				"                \"value\": [{\"value\":\"VSPL_Domain\",\"displayValue\":\"VSPL_Domain\"}]" +
                "            }," +
                "            {" +
                "                \"type\": \"MULTI_SELECT\"," +
                "                \"name\": \"Band\"," +
                "                \"displayName\": \"Band\"," +
                "                \"value\": []" +
                "            }," +
                "            {" +
                "                \"type\": \"TIME_RANGE\"," +
                "                \"name\": \"TimeRange\"," +
                "                \"displayName\": \"Time Range\"," +
                "                \"value\": {" +
                "                    \"timeRangeOption\": \"CUSTOM\"," +
                "\"startDateTime\": "+new DateTimeUtil().get1hrBeforeTimestamp()+"," +
                "                    \"endDateTime\": "+new DateTimeUtil().getCurrentTimestap()+
                "                }," +
                "                \"timeOptions\": null" +
                "            }" +
                "        ]," +
                "        \"format\": {" +
                "            \"name\": \"JSON\"," +
                "            \"formatType\": \"JSON\"" +
                "        }," +
                "        \"viewId\": \"e8e66b17-4aeb-4857-af81-f472023bb05e\"" +
                "    }," +
                "    \"viewGroupId\": \"d7afe5c9-4941-4251-8bf5-0fb643e90847\"," +
                "    \"viewGroupVersion\": \"2.0.0\"" +
                "}";
        try {
            String _token = getAuthTokenFromDNAC();
            HttpHeaders _headers  = createHeaders(_token);
            String _url = dnac_report_api;
            System.out.println("dnac_report_api:: "+dnac_report_api);
            ResponseEntity<String> response = service.CallPostRequest(_headers, jsonPayLoad, _url);
            if(response.getStatusCode() ==HttpStatus.OK) {
                String _body = response.getBody();

                _body = _body.replaceAll("\"type\":\"MULTI_SELECT_TREE\",", "");
                _body = _body.replaceAll("\"type\":\"SCHEDULE_NOW\",", "");
                _body = _body.replaceAll("\"type\":\"SINGLE_SELECT_ARRAY\",", "");
                _body = _body.replaceAll("\"type\":\"MULTI_SELECT\",", "");
                _body = _body.replaceAll("\"type\":\"TIME_RANGE\",", "");
                JSONObject jsonData = new JSONObject(_body);
                String _reportId =  jsonData.getString("reportId");
                System.out.println(_reportId);
                Thread.sleep(1000);
                JSONObject _obj = getReportContent(_reportId, _headers);

                JSONArray clientArr = _obj.getJSONArray("client_details");
                JSONArray dateTimeArr = _obj.getJSONArray("filters");
                System.out.println("dateTimeArr: "+dateTimeArr);
                String startTime = "";
                String endTime = "";

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (int i = 0; i < dateTimeArr.length(); i++) {
                    JSONObject dateTimeJson = dateTimeArr.getJSONObject(i);
                    if (dateTimeJson.getString("name").equalsIgnoreCase("startTime")){
                        startTime = dateTimeJson.getJSONArray("values").getString(0);
                    }
                    if (dateTimeJson.getString("name").equalsIgnoreCase("endTime")){
                        endTime = dateTimeJson.getJSONArray("values").getString(0);
                    }
                }
                System.out.println(startTime+ " time: "+endTime);

                List<DnacClient> dataList = new ArrayList<>();

                for (int i = 0; i < clientArr.length(); i++) {
                    JSONObject modifyJson = clientArr.getJSONObject(i);

                    Date a = simpleDateFormat.parse(startTime);
                    Date a1 = simpleDateFormat.parse(endTime);


                    DnacClient data = new DnacClient();
                    data.setJsonDocument(modifyJson.toString());
                    data.setStartTime(a);
                    data.setEndTime(a1);

                    dataList.add(data);
                }

                System.out.println("Save--->>>>");

                dnacClientService.save(dataList);


                System.out.println("Record Inserted Successfully....");
                deleteReportFromDnac(_reportId, _headers);
            }
        } catch (Exception e) {
            System.err.println("Exception: "+e);
            e.printStackTrace();
        }finally {
            System.gc();
        }
    }
    private void deleteReportFromDnac(String _reportId, HttpHeaders _headers) throws Exception {
        JSONObject responseJson = null;
//		String _url = "https://{ip}/dna/intent/api/v1/data/reports/"+_reportId;
        String _url = dnac_report_api+_reportId;
        ResponseEntity<String> response = service.CallDeleteRequest(_headers, _url);
        System.out.println("deleteReportFromDnac:: "+response);
        if(response.getStatusCode() ==HttpStatus.OK) {
            String _body = response.getBody();
            responseJson = new JSONObject(_body);
        }
        System.out.println("Report Deleted: "+responseJson);

    }

    public JSONObject getReportContent(String _reportId, HttpHeaders _headers) throws Exception {
        String _executionId = getExecutionId(_reportId, _headers);
        System.out.println("_executionId: "+ _executionId);
        Thread.sleep(200000);
        return downloadReportContent(_reportId, _headers, _executionId);
    }
    private String getExecutionId(String _reportId, HttpHeaders _headers) throws Exception {
        String _executionId = null;
        String _url = dnac_report_api;//"https://{ip}/dna/intent/api/v1/data/reports/";
        ResponseEntity<String> response = service.CallGetRequest(_headers, _reportId, _url);
        if(response.getStatusCode() ==HttpStatus.OK) {
            String _body = response.getBody();
            _body = _body.replaceAll("\"type\":\"MULTI_SELECT_TREE\",", "");
            _body = _body.replaceAll("\"type\":\"SCHEDULE_NOW\",", "");
            _body = _body.replaceAll("\"type\":\"SINGLE_SELECT_ARRAY\",", "");
            _body = _body.replaceAll("\"type\":\"MULTI_SELECT\",", "");
            _body = _body.replaceAll("\"type\":\"TIME_RANGE\",", "");
            System.out.println("executionID body: "+_body);
            JSONObject _json = new JSONObject(_body);
            JSONArray responseArr = _json.getJSONArray("executions");
            if(responseArr.length()>0)
                _executionId = responseArr.getJSONObject(0).getString("executionId");
            else
                return getExecutionId(_reportId, _headers);
        }
        return _executionId;
    }

    private JSONObject downloadReportContent(String _reportId, HttpHeaders _headers, String _executionId) throws Exception {
        JSONObject responseJson = null;
//		String _url = "https://{ip}/dna/intent/api/v1/data/reports/"+_reportId+"/executions/"+_executionId;
        String _url = dnac_report_api+_reportId+"/executions/"+_executionId;
        ResponseEntity<String> response = service.CallGetRequest(_headers, "", _url);
        System.out.println("downloadReportContent:: "+response);
        if(response.getStatusCode() ==HttpStatus.OK) {
            String _body = response.getBody();
            responseJson = new JSONObject(_body);
        }else if(response.getStatusCode() ==HttpStatus.NOT_FOUND) {
            return downloadReportContent(_reportId, _headers, _executionId);
        }
        return responseJson;
    }

    public HttpHeaders createHeaders(String l_token) {
        HttpHeaders _headerSet = new HttpHeaders();
        _headerSet.setContentType(MediaType.APPLICATION_JSON);
        _headerSet.set("x-auth-token", l_token);
        return _headerSet;
    }

    private HttpHeaders prepareAuthHeaders(String username, String password) throws Exception {
        String auth=username+":"+password;
        HttpHeaders _headerSet = new HttpHeaders();
        _headerSet.setContentType(MediaType.APPLICATION_JSON);
        _headerSet.set("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString(auth.getBytes(Charset.forName("UTF-8"))));
        return _headerSet;
    }

    public String getAuthTokenFromDNAC() {
        ResponseEntity<String> _response = null;
        String _responseString=null;
        String _token = null;
        try {
            if(dnac_user_name == null || dnac_password == null) {
                Properties prop = new Properties();
                InputStream input=getClass().getClassLoader().getResourceAsStream("com/vel/resources/constant.properties");
                prop.load(input);
                dnac_user_name = prop.getProperty("dnac_user_name");
                dnac_password = prop.getProperty("dnac_password");
                dnac_auth_api = prop.getProperty("dnac_auth_api");
            }
            HttpHeaders _httpHeaders = null;
            _httpHeaders = prepareAuthHeaders(dnac_user_name,dnac_password);
            _response =service.CallPostRequest(_httpHeaders, "", dnac_auth_api);
            _responseString =_response.getBody();
            System.out.println("_responseString for AUth :: "+_responseString);
            JSONObject _json = new JSONObject(_responseString);
            _token= _json.get("Token").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _token;
    }
}
