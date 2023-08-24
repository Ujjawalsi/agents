package com.example.agents.reports.implementation;

import com.example.agents.endpointAgent.EndpointAgentModel;
import com.example.agents.reports.repository.EndPointAgentRepo;
import com.example.agents.reports.service.EndPointAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EndPointAgentImpl implements EndPointAgentService {

    @Autowired
    private EndPointAgentRepo endPointAgentRepo;
    @Override
    public List<EndpointAgentModel> findByAgentNameContainingIgnoreCase(String domainName) {

        return endPointAgentRepo.findByAgentNameContainingIgnoreCase(domainName);

    }

    @Override
    public List<EndpointAgentModel> findByAgentNameOrClientUserName(String agentName) {

        return endPointAgentRepo.findByAgentNameOrClientUserName(agentName);
    }
}
