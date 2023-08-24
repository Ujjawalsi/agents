package com.example.agents.reports.service;

import com.example.agents.endpointAgent.EndpointAgentModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EndPointAgentService {

//  @Query(value = "SELECT * FROM public.ms_endpoint_agents WHERE CAST(agent_data AS JSON)->>'agentName' LIKE 'domainName'",nativeQuery = true)
  List<EndpointAgentModel> findByAgentNameContainingIgnoreCase(String domainName);



  //SELECT * FROM public.ms_endpoint_agents WHERE CAST(agent_data AS JSON)->>'agentName' LIKE 'VELOCIS\\shreya.srivastava';
 // @Query(value = "SELECT * FROM ms_endpoint_agents WHERE CAST(agent_data AS JSON)->>'agentName' ILIKE '%:agentName%'",nativeQuery = true)
//  @Query(value = "SELECT * FROM ms_endpoint_agents WHERE CAST(agent_data AS JSON)->>'agentName' ILIKE CONCAT('%', :agentName, '%')", nativeQuery = true)

//  @Query(value = "SELECT * FROM ms_endpoint_agents ae WHERE JSON_VALUE(ae.agent_Data, '$.agentName') ILIKE CONCAT('%', :agentName, '%')")
  List<EndpointAgentModel> findByAgentNameOrClientUserName(@Param("agentName") String agentName);
}
