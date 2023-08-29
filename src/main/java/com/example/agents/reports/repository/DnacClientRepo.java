package com.example.agents.reports.repository;

import com.example.agents.reports.entities.DnacClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface DnacClientRepo extends JpaRepository<DnacClient, Long > {

    @Query(value = "SELECT * FROM dnac_clients_reports_data WHERE start_time >= :newdate AND end_time <= :olddate" , nativeQuery = true)
    List<DnacClient> findByTimeRange(@Param("newdate") Date newdate, @Param("olddate") Date olddate);

    @Query(value = "SELECT * FROM dnac_clients_reports_data WHERE start_time >= :startTime AND end_time <= :endTime", nativeQuery = true)
    List<DnacClient> findByTimeRangeGap(@Param("startTime") Date startTime, @Param("endTime") Date endTime);



}
