package com.example.agents.inventoryData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.agents.vel.common.connector.service.IBUSAPIConnectorService;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;



@Component
@EnableScheduling
public class UpdateInventoryService {

    @Value("${dnac.user.name}")
    private String dnacUserName;

    @Value("${dnac.password}")
    private String dnacPassword;

    @Value("${dnac.auth.api}")
    private String dnacAuthApi;

    @Value("${dnac.inventory.api}")
    private String dnacInventoryApi;

    @Autowired
    private InventoryDataRepository inventoryDataRepository;

    @Autowired
    private IBUSAPIConnectorService service;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(cron = "0 10 11 * * *")
    public void updateDeviceInventory() {
        System.out.println("Updating Device Inventory!!");
        try {
            String token = getAuthTokenFromDNAC();
            HttpHeaders headers = createHeaders(token);
            String url = dnacInventoryApi;

            ResponseEntity<String> response = service.CallGetRequest(headers, "", url);
            if (response.getStatusCode() == HttpStatus.OK) {
                String body = response.getBody();

                JSONObject jsonData = new JSONObject(body);
                JSONArray inventoryArr = jsonData.getJSONArray("response");

                List<InventoryDataModel> inventoryDataList = new ArrayList<>();

                for (int i = 0; i < inventoryArr.length(); i++) {
                    JSONObject inventoryJson = inventoryArr.getJSONObject(i);
                    InventoryDataModel inventoryData = new InventoryDataModel();
                    inventoryData.setData(inventoryJson.toString());
                    inventoryData.setCreatedAt(LocalDateTime.now());
                    inventoryDataList.add(inventoryData);
                }

                inventoryDataRepository.saveAll(inventoryDataList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-auth-token", token);
        return headers;
    }

    private HttpHeaders prepareAuthHeaders(String username, String password) throws Exception {
        String auth = username + ":" + password;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString(auth.getBytes(Charset.forName("UTF-8"))));
        return headers;
    }

    public String getAuthTokenFromDNAC() {
        ResponseEntity<String> response = null;
        String responseString = null;
        String token = null;
        try {
            HttpHeaders httpHeaders = prepareAuthHeaders(dnacUserName, dnacPassword);
            response = service.CallPostRequest(httpHeaders, "", dnacAuthApi);
            responseString = response.getBody();
            JSONObject json = new JSONObject(responseString);
            token = json.get("Token").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }


}
