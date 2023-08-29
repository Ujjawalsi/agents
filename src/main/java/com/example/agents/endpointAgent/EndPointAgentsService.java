package com.example.agents.endpointAgent;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.agents.vel.common.connector.service.IBUSAPIConnectorService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling
public class EndPointAgentsService {

    @Autowired
    private EndpointAgentRepository endpointAgentRepository;

    @Autowired
    private IBUSAPIConnectorService service;

    private ObjectMapper objectMapper = new ObjectMapper();

   // @Scheduled(cron = "0 */15 * * * ?")
	@Scheduled(cron = "0 */1 * * * ?")
//
    private void executeAgentsAPI() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer 01f4a6f0-408a-4b42-820b-7b1c725f6bb7");
        String url = "https://api.thousandeyes.com/v6/endpoint-agents.json";
        
        ResponseEntity<String> response;
		try {
			response = service.CallGetRequest(headers, "", url);
			if (response.getStatusCode() == HttpStatus.OK) {
	            JSONObject jsonResponse = new JSONObject(response.getBody());
	            JSONArray agentsArr = jsonResponse.getJSONArray("endpointAgents");

	            List<EndpointAgentModel> endpointAgents = new ArrayList<>();

	            for (int i = 0; i < agentsArr.length(); i++) {
	                JSONObject agentJson = agentsArr.getJSONObject(i);
	                EndpointAgentModel endpointAgent = new EndpointAgentModel();
	                endpointAgent.setAgentData(agentJson.toString());
	                endpointAgent.setCreatedAt(LocalDateTime.now());
	                endpointAgents.add(endpointAgent);
	            }

				endpointAgentRepository.deleteAll();

	            endpointAgentRepository.saveAll(endpointAgents);
	            System.out.println("Working scenario");
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
    }

 public List<EndpointAgentModel> getAll(){
     return endpointAgentRepository.findAll();

 }


	public String getDomainName(String checkIssuesWithUser) {
		String _domainName = null;
		try{
		String name = checkIssuesWithUser.replace(".", " ");
		List<EndpointAgentModel> models = endpointAgentRepository.findByName(name);
			System.out.println(models.size());

		if (!models.isEmpty()) {
			EndpointAgentModel endpointAgent = models.get(0);// Assuming there's only one matching entry
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
}
