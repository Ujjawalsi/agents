package com.example.agents.reports.repository;

import com.example.agents.endpointAgent.EndpointAgentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EndPointAgentRepo extends JpaRepository<EndpointAgentModel, Long> {


    @Query(value = "SELECT * FROM ms_endpoint_agents WHERE agentData->>userName ILIKE %agentname% ", nativeQuery = true)
    List<EndpointAgentModel> findByAgentNameOrClientUserName(String agentName);

    @Query(value = "SELECT * FROM ms_endpoint_agents WHERE agentData->>userName ILIKE %domainName% ", nativeQuery = true)
    List<EndpointAgentModel> findByAgentNameContainingIgnoreCase(String domainName);
}
