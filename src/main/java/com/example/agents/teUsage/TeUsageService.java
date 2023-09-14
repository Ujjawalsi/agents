package com.example.agents.teUsage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.agents.vel.common.connector.service.IBUSAPIConnectorService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling
public class TeUsageService {

    @Value("${te.usage.api}")
    private String teUsageApi;

    @Value("${te.usage.api.key}")
    private String teUsageApiKey;

    @Autowired
    IBUSAPIConnectorService service;

    @Autowired
    private TeUsageRepository teUsageRepository;


    private static final ObjectMapper objectMapper = new ObjectMapper();

  @Scheduled(cron = "0 */15 * * * ?") // Schedule as required
    private void excuteUsageAPI() throws Exception {
        HttpHeaders _headerSet = new HttpHeaders();
        _headerSet.setContentType(MediaType.APPLICATION_JSON);
        _headerSet.set("Authorization", teUsageApiKey);
        String _url = teUsageApi;
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


    public List<TeUsageModel> findByTimeRange(LocalDateTime startime, LocalDateTime endTime) {
      return teUsageRepository.findByTimeRange(startime,endTime);
    }
}
