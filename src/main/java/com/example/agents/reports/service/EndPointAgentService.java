package com.example.agents.reports.service;

import com.example.agents.endpointAgent.EndpointAgentModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EndPointAgentService {

  List<EndpointAgentModel> findByAgentNameContainingIgnoreCase(String domainName);
  List<EndpointAgentModel> findByAgentNameOrClientUserName(@Param("agentName") String agentName);
}
