package com.example.agents.agentsandmonitors;

import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.agents.vel.common.connector.service.IBUSAPIConnectorService;
@Component
@EnableScheduling
public class AgentsAndMonitorsService {

//    @Value("${te_agents_api}")
//    private String te_agents_api;

//    @Value("${te_api_key}")
//    private String te_api_key;

    @Autowired
    IBUSAPIConnectorService service;

    @Autowired
    private AgentsAndMonitorsRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(AgentsAndMonitorsService.class);

  @Scheduled(cron = "0 */3 * * * ?")
    private void excuteAgentsAndMonitorsAPI() throws Exception {
        HttpHeaders _headerSet = new HttpHeaders();
        _headerSet.setContentType(MediaType.APPLICATION_JSON);
        _headerSet.set("Authorization", "Bearer 01f4a6f0-408a-4b42-820b-7b1c725f6bb7");
        String _url = "https://api.thousandeyes.com/v6/agents.json";
        ResponseEntity<String> response = service.CallGetRequest(_headerSet, "", _url);
        logger.warn("Getting Response from CallGetRequest : {}", response);
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject _json = new JSONObject(response.getBody());
            logger.warn("Getting _json from response and status ok : {} , {}", response.getStatusCode(),_json);
            LocalDateTime now = LocalDateTime.now();
            AgentsAndMonitorsModel entity = new AgentsAndMonitorsModel(_json.toString());
            entity.setJsonDocument(_json.toString());
            entity.setTimeStamp(now);
            repository.save(entity);
            logger.warn("After Inserting entity into Db :{}", entity);
            System.out.println("Inserted in the db");
        }
    }


    public List<AgentsAndMonitorsModel> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
      return repository.findByTimeRange(startTime, endTime);
    }
}