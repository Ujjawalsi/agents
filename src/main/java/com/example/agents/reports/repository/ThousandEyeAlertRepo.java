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

    @Query(value = "SELECT * FROM ms_thousandeye_alert " +
            "WHERE (data->>'start_time')::timestamp >= :startTime::timestamp " +
            "AND (data->>'end_time')::timestamp <= :endTime::timestamp " +
            "AND (data->>'agent_name') ILIKE %:agentName% " +
            "AND (data->>'application') ILIKE %:application% " +
            "AND (data->>'domain_name') ILIKE %:domainName%",
            nativeQuery = true)
    List<ThousandEyeAlert> findAlertsByCriteria(String startTime, String endTime, String agentName, String application, String domainName);


    @Query(value = "SELECT * FROM ms_thousandeye_alert " +
            "WHERE (data->>'start_time')::timestamp >= :startTime::timestamp " +
            "AND (data->>'end_time')::timestamp <= :endTime::timestamp " +
            "AND (data->>'agent_name') ILIKE %:agentName% " +
            "AND (data->>'application') ILIKE %:application% " +
            "AND (data->>'domain_name') ILIKE %:domainName%",
            nativeQuery = true)
    List<ThousandEyeAlert> findAlertsByCriteria1(String startTime, String endTime, String agentName, String application, String domainName);


    @Query(value = "SELECT * FROM ms_thousandeye_alert " +
            "WHERE (data->>'start_time')::timestamp >= :startTime::timestamp " +
            "AND (data->>'end_time')::timestamp <= :endTime::timestamp " +
            "AND (data->>'agent_name') ILIKE %:enterpriseAgentName% " +
            "AND (data->>'application') ILIKE %:application% ",
            nativeQuery = true)
    List<ThousandEyeAlert> findAlertsForApplicationIssues(String startTime, String endTime, String enterpriseAgentName, String application);

    @Query(value = "SELECT * FROM ms_thousandeye_alert WHERE alert IS NOT NULL AND " +
            "SUBSTRING" +
            "(alert, '\"dateStartZoned\":\"(.*?)\"') >= :startTime AND " +
            "SUBSTRING(alert, '\"dateStartZoned\":\"(.*?)\"') <= :endTime", nativeQuery = true)
    List<ThousandEyeAlert> findByTimeRange(@Param("startTime") String startTime, @Param("endTime") String endTime);


    @Query(value = "SELECT * FROM ms_thousandeye_alert WHERE alert IS NOT NULL AND " +
            "SUBSTRING(alert, '\"dateStartZoned\":\"(.*?)\"') >= :ahead AND " +
            "SUBSTRING(alert, '\"dateStartZoned\":\"(.*?)\"') <= :startdate", nativeQuery = true)
    List<ThousandEyeAlert> findByTimingGap(@Param("ahead") String ahead, @Param("startdate") String startdate);

    @Query(value = "SELECT * FROM ms_thousandeye_alert WHERE alert IS NOT NULL AND " +
            "SUBSTRING(alert, '\"testName\":\"(.*?)\"') = :application", nativeQuery = true)
    List<ThousandEyeAlert> findByTestName(@Param("application") String application);

    @Query(value = "SELECT * FROM ms_thousandeye_alert WHERE alert IS NOT NULL AND " +
            "SUBSTRING(alert, '\"dateStart\":\"(.*?)\"') >= :startTime AND " +
            "SUBSTRING(alert, '\"dateStart\":\"(.*?)\"') <= :endTime AND " +
            "SUBSTRING(alert, '\"testName\":\"(.*?)\"') = :application", nativeQuery = true)
    List<ThousandEyeAlert> findByTimeGapAndTestName(@Param("startTime") String startTime,@Param("endTime")String endTime, @Param("application")String application);

}
