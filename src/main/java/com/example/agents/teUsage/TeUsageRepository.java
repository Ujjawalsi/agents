package com.example.agents.teUsage;
import com.example.agents.inventoryData.UpdateInventoryService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TeUsageRepository extends JpaRepository<TeUsageModel, Long> {

    @Query(value = "SELECT * FROM ms_te_usage WHERE time_stamp > :startTime AND time_stamp < :endTime" , nativeQuery = true)
    List<TeUsageModel> findByTimeRange(@Param("startTime")LocalDateTime startime, @Param("endTime")LocalDateTime endTime);
}
