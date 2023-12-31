package com.example.agents.endpointAgent;

import com.fasterxml.jackson.databind.JsonNode;
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
import java.util.Arrays;
import java.util.List;

@Component
@EnableScheduling
public class EndPointAgentsService {

    @Autowired
    private EndpointAgentRepository endpointAgentRepository;

    @Autowired
    private IBUSAPIConnectorService service;

    private ObjectMapper objectMapper = new ObjectMapper();

@Scheduled(cron = "0 */15 * * * ?")
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
		List<EndpointAgentModel> list = new ArrayList<>();
		String userName = null;
		try {

			List<EndpointAgentModel> models1 = endpointAgentRepository.findAll();
			for (EndpointAgentModel endPointModel : models1) {
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode rootNode = objectMapper.readTree(endPointModel.getAgentData());
					userName = rootNode
							.path("clients")
							.get(0)
							.path("userProfile")
							.path("userName")
							.asText();
				} catch (Exception e) {
					e.printStackTrace();
				}

				List<String> userNameArray = Arrays.asList(checkIssuesWithUser.split(""));
				List<String> userNameList = Arrays.asList(userName.split(""));
				if (userNameList.containsAll(userNameArray)) {
					list.add(endPointModel);
				} else {
					JSONObject jsonObject = new JSONObject(endPointModel.getAgentData());
					String string = jsonObject.getString("agentName");
					List<String> agentNameList = Arrays.asList(string.split(""));
					if (agentNameList.containsAll(userNameArray)) {
						list.add(endPointModel);
					}

				}
			}
		EndpointAgentModel endpointAgent = list.get(0);// Assuming there's only one matching entry
			String jsonDocument = endpointAgent.getAgentData();
			JSONObject jsonObject = new JSONObject(jsonDocument);
			_domainName = jsonObject.getString("agentName");
	} catch (Exception e) {
		e.printStackTrace();
	}
        return _domainName;
	}
}
