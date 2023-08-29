package com.example.agents.reports.service;

import com.example.agents.endpointAgent.EndpointAgentModel;

import java.util.List;

public interface EndPointAgentService {

  List<EndpointAgentModel> findByAgentNameContainingIgnoreCase(String domainName);
}
