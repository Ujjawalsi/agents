package com.example.agents.endpointAgent;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.agents.endpointAgent.EndpointAgentModel;

public interface EndpointAgentRepository extends JpaRepository<EndpointAgentModel, Long> {
	
}

