package com.example.agents.dnacNetworkHealthData;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.example.agents.dnacNetworkHealthData.DnacNetworkHealthDataModel;
import com.example.agents.dnacNetworkHealthData.DnacNetworkHealthDataRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.vel.common.connector.service.IBUSAPIConnectorService;
import org.json.JSONObject;


import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@EnableScheduling
@PropertySource("classpath:com/vel/common/connector/service/dnacconstants.properties")
public class DnacNetworkHealthDataService {
	@Value("${dnac_auth_api}")
    String dnac_auth_api;
	@Value("${dnac_user_name}")
    String dnac_user_name;
	@Value("${dnac_password}")
	String dnac_password;
	@Value("${dnac_network_health_api}")
	String dnac_network_health_api;
    @Autowired
    private IBUSAPIConnectorService service;

    @Autowired
    private DnacNetworkHealthDataRepository dnacRepository;

    @Scheduled(cron = "0 */1 * * * ?") // Run every 15 minutes
    private void getNetworkHealth() {
        try {
            String token = getAuthTokenFromDNAC();
            HttpHeaders httpHeader = createHeaders(token);
            ResponseEntity<String> response = service.CallGetRequest(httpHeader, "", dnac_network_health_api);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String formattedDate = simpleDateFormat.format(new Date());
				Date timeStamp = simpleDateFormat.parse(formattedDate);
				System.out.println(timeStamp);

                DnacNetworkHealthDataModel dnacData = new DnacNetworkHealthDataModel();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                
                JsonNode responseArray = jsonNode.get("response");
                if (responseArray != null && responseArray.isArray() && responseArray.size() > 0) {
                    JsonNode responseObject = responseArray.get(0); // Assuming you only want the first item
                    String responseJson = objectMapper.writeValueAsString(responseObject);

                    // Now you have the "response" JSON as a string
                    System.out.println("Response JSON: " + responseJson);
                dnacData.setResponse(responseJson);
                dnacData.setTimeStamp(timeStamp);

                dnacRepository.save(dnacData);
                System.out.println("DBE3 DONEEEE");
            }}
                
        }
            catch (Exception e) {
            e.printStackTrace();
        }
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
		System.out.println("token"+_token);
		return _token;
	}
    
    public HttpHeaders createHeaders(String l_token) {
		HttpHeaders _headerSet = new HttpHeaders();
		_headerSet.setContentType(MediaType.APPLICATION_JSON);
		_headerSet.set("x-auth-token", l_token);
		return _headerSet;
	}


 public   List<DnacNetworkHealthDataModel> getDnacOneDayNetworkData(Date olddate, Date newdate) {
       return dnacRepository.findByTimeRange(olddate, newdate);
	}



//	public JSONArray getDnacOneDayNetworkData(String start_time, String end_time){
//		List<DnacNetworkHealthDataModel> list = dnacRepository.findByTimeStampLessThanEqualAndTimeStampGreaterThanEqual(end_time, start_time);
//
//		JSONArray jsonarray = new JSONArray();
//		for (DnacNetworkHealthDataModel data: list) {
//
//			jsonarray.put(data);
//
//		}
//		return jsonarray;
//	}

}

