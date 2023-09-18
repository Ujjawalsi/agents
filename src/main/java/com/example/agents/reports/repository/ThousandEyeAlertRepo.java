package com.example.agents.reports.repository;

import com.example.agents.reports.entities.ThousandEyeAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ThousandEyeAlertRepo extends JpaRepository<ThousandEyeAlert, Long> {

    @Query(value = "SELECT * FROM ms_thousandeye_alert WHERE  new_event_id ='newEventId' " , nativeQuery = true)
    ThousandEyeAlert findByNewEventId(String newEventId);

    @Query(value = "SELECT * FROM ms_thousandeye_alert WHERE alert IS NOT NULL AND " +
            "AND CAST(alert AS JSON)->>'dateStart' <= :endTime AND " +
            "AND CAST(alert AS JSON)->>'testName' = :application", nativeQuery = true)
    List<ThousandEyeAlert> findAlertsByCriteria(@Param("endTime") String endTime,@Param("application") String application);

    @Query(value = "SELECT * FROM public.ms_thousandeye_alert WHERE alert IS NOT NULL " +
            "AND CAST(alert AS JSON)->>'dateStartZoned' <= :endTime " +
            "AND CAST(alert AS JSON)->>'dateStartZoned' >= :startTime",
            nativeQuery = true)
    List<ThousandEyeAlert> findByTimeRange(@Param("startTime") String startTime, @Param("endTime") String endTime);


    @Query(value = "SELECT * FROM ms_thousandeye_alert WHERE alert IS NOT NULL AND " +
            "AND CAST(alert AS JSON)->>'dateStartZoned' >= :ahead AND " +
            "AND CAST(alert AS JSON)->>'dateStartZoned' <= :startdate", nativeQuery = true)
    List<ThousandEyeAlert> findByTimingGap(@Param("ahead") String ahead, @Param("startdate") String startdate);

    @Query(value = "SELECT * FROM ms_thousandeye_alert WHERE alert IS NOT NULL AND " +
            "AND CAST(alert AS JSON)->>'testName'= :application", nativeQuery = true)
    List<ThousandEyeAlert> findByTestName(@Param("application") String application);

    @Query(value = "SELECT * FROM ms_thousandeye_alert WHERE alert IS NOT NULL AND " +
            "AND CAST(alert AS JSON)->>'dateStart' >= :startTime AND " +
            "AND CAST(alert AS JSON)->>'dateStart' <= :endTime AND " +
            "AND CAST(alert AS JSON)->>'testName' = :application", nativeQuery = true)
    List<ThousandEyeAlert> findByTimeGapAndTestName(@Param("startTime") String startTime,@Param("endTime")String endTime, @Param("application")String application);

}
