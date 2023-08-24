package com.example.agents.agentsandmonitors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface AgentsAndMonitorsRepository extends JpaRepository<AgentsAndMonitorsModel, Long> {


    @Query(value = "SELECT * FROM ms_te_agents_and_monitors WHERE time_stamp > :startTime AND time_stamp < :endTime" , nativeQuery = true)
    List<AgentsAndMonitorsModel> findByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

}
