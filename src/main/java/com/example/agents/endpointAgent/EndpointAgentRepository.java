package com.example.agents.endpointAgent;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.agents.endpointAgent.EndpointAgentModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EndpointAgentRepository extends JpaRepository<EndpointAgentModel, Long> {


}

