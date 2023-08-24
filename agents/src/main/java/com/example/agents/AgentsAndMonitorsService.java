package com.example.agents;

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
import org.springframework.transaction.annotation.Transactional;

import com.vel.common.connector.service.IBUSAPIConnectorService;
@Component
@EnableScheduling
@PropertySource("classpath:com/vel/common/connector/service/constants.properties")
public class AgentsAndMonitorsService {

    @Value("${te_agents_api}")
    private String te_agents_api;

    @Value("${te_api_key}")
    private String te_api_key;

    @Autowired
    IBUSAPIConnectorService service;

    @Autowired
    private AgentsAndMonitorsRepository repository;

    @Scheduled(cron = "0 */1 * * * ?")
    private void excuteAgentsAndMonitorsAPI() throws Exception {
        HttpHeaders _headerSet = new HttpHeaders();
        _headerSet.setContentType(MediaType.APPLICATION_JSON);
        _headerSet.set("Authorization", "Bearer 01f4a6f0-408a-4b42-820b-7b1c725f6bb7");
        String _url = "https://api.thousandeyes.com/v6/agents.json";
        ResponseEntity<String> response = service.CallGetRequest(_headerSet, "", _url);

        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject _json = new JSONObject(response.getBody());

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            _json.put("time_stamp", dtf.format(now));

            AgentsAndMonitorsModel entity = new AgentsAndMonitorsModel(_json.toString());
            entity.setJsonDocument(_json.toString());
            repository.save(entity);
            System.out.println("Inserted in the db");
        }
    }
}