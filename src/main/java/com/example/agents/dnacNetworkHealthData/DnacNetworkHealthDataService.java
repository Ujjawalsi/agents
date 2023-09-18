package com.example.agents.dnacNetworkHealthData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.example.agents.vel.common.connector.service.IBUSAPIConnectorService;
import org.json.JSONObject;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
@EnableScheduling
public class DnacNetworkHealthDataService {

    @Value("${dnac.user.name}")
    private String dnacUserName;

    @Value("${dnac.password}")
    private String dnacPassword;

    @Value("${dnac.auth.api}")
    private String dnacAuthApi;

    @Value("${dnac.network.health.api}")
    private String dnacNetworkHealthApi;

    @Autowired
    private IBUSAPIConnectorService service;

    @Autowired
    private DnacNetworkHealthDataRepository dnacRepository;

    @Scheduled(cron = "0 */15 * * * ?") // Run every 15 minutes
    private void getNetworkHealth() {
        try {
            String token = getAuthTokenFromDNAC();
            HttpHeaders httpHeader = createHeaders(token);
            ResponseEntity<String> response = service.CallGetRequest(httpHeader, "", dnacNetworkHealthApi);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String formattedDate = simpleDateFormat.format(new Date());
				Date timeStamp = simpleDateFormat.parse(formattedDate);

                DnacNetworkHealthDataModel dnacData = new DnacNetworkHealthDataModel();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                
                JsonNode responseArray = jsonNode.get("response");
                if (responseArray != null && responseArray.isArray() && responseArray.size() > 0) {
                    JsonNode responseObject = responseArray.get(0); // Assuming you only want the first item
                    String responseJson = objectMapper.writeValueAsString(responseObject);
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
            HttpHeaders _httpHeaders = null;
			_httpHeaders = prepareAuthHeaders(dnacUserName,dnacPassword);
			_response =service.CallPostRequest(_httpHeaders, "", dnacAuthApi);
			_responseString =_response.getBody();
			JSONObject _json = new JSONObject(_responseString);
			_token= _json.get("Token").toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
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

}

