package com.example.agents.reports.repository;

import com.example.agents.endpointAgent.EndpointAgentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EndPointAgentRepo extends JpaRepository<EndpointAgentModel, Long> {


    @Query(value = "SELECT * FROM ms_endpoint_agents WHERE UPPER(CAST(agent_data AS JSON)->>'agentName') = UPPER(:name)", nativeQuery = true)
    List<EndpointAgentModel> findByName(@Param("name") String name);

}
