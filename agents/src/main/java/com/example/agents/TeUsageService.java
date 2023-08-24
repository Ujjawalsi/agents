package com.example.agents;

// ...


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vel.common.connector.service.IBUSAPIConnectorService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
@Component
@EnableScheduling
@PropertySource("classpath:com/vel/common/connector/service/constants.properties")
public class TeUsageService {

    @Value("${te_usage_api_key}")
    private String te_usage_api_key;
    
    @Value("${te_usage_api}")
    private String te_usage_api;

    @Autowired
    IBUSAPIConnectorService service;

    @Autowired
    private TeUsageRepository teUsageRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(cron = "0 */1 * * * ?") // Schedule as required
    private void excuteUsageAPI() throws Exception {
        HttpHeaders _headerSet = new HttpHeaders();
        _headerSet.setContentType(MediaType.APPLICATION_JSON);
        _headerSet.set("Authorization", te_usage_api_key);
        String _url = te_usage_api;
        ResponseEntity<String> response = service.CallGetRequest(_headerSet, "", _url);
        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            LocalDateTime now = LocalDateTime.now();

            TeUsageModel teUsage = new TeUsageModel();
            teUsage.setJsonDocument(jsonNode.toString());
            teUsage.setTimeStamp(now);

            teUsageRepository.save(teUsage);
            System.out.println("Inserted in the db 2");
        }
    }
    // ...
}
