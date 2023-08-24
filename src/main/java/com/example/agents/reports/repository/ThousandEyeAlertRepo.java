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

    @Query(value = "SELECT * FROM ms_thousandeye_alert WHERE data->> severity='critical' AND date ='date'", nativeQuery = true)
    List<ThousandEyeAlert> findBySeverityAndDateLessThanEqual(String critical, Date date);

    @Query(value = "SELECT * FROM ms_thousandeye_alert WHERE data->> severity='critical' AND date ='date'", nativeQuery = true)
    List<ThousandEyeAlert> findBySeverityAndDateStartZonedBetween(String critical, String seconddate, String firstdate);

    @Query(value = "SELECT alert FROM ThousandEyeAlert alert " +
            "WHERE alert.severity = ?1 AND alert.call = ?2 AND alert.date = ?3", nativeQuery = true)
    List<ThousandEyeAlert> getCoudnByServerity(String critical, int i, Date date);


    @Query(value = "SELECT * FROM thousand_eye_alert\n" +
            "WHERE date_start_zoned BETWEEN :startTime AND :endTime\n" +
            "  AND alert_agents_agent_name LIKE '%' || :agentName || '%'\n" +
            "  AND alert_test_name LIKE '%' || :application || '%'\n" +
            "  AND alert_domain_name LIKE '%' || :domainName || '%';", nativeQuery = true)
    List<ThousandEyeAlert> findAlertsByFilters(String startTime, String endTime, String agentName, String application, String domainName);

    @Query(value = "SELECT alert FROM ThousandEyeAlert alert " +
            "WHERE alert.timeStamp BETWEEN ?1 AND ?2 AND alert.application = ?3", nativeQuery = true)
    List<ThousandEyeAlert> findAlertsByTimeRangeAndApplication(String startTime, String endTime, String application);

    @Query(value = "SELECT t FROM ThousandEyeAlert t WHERE t.startTime >= startTime AND t.endTime <= endTime AND t.agentName = agentName AND t.application = application AND t.domainName = domainName", nativeQuery = true)
    List<ThousandEyeAlert> findApplicationIssues(String startTime, String endTime, String enterpriseAgentName, String application);
//    @Query(value = "SELECT * FROM ms_thousandeye_alert  WHERE dateStartZoned >= :startTime AND dateStartZoned <= :endTime", nativeQuery = true)

//    @Query(value = "SELECT * FROM ms_thousandeye_alert WHERE alert IS NOT NULL AND (alert ->> 'dateStartZoned')::text >= :startTime AND (alert ->> 'dateStartZoned')::text <= :endTime", nativeQuery = true)
//    List<ThousandEyeAlert> findByTimeRange(@Param("startTime")String startTime, @Param("endTime") String endTime);

    @Query(value = "SELECT * FROM ms_thousandeye_alert WHERE alert IS NOT NULL AND " +
            "SUBSTRING(alert, '\"dateStartZoned\":\"(.*?)\"') >= :startTime AND " +
            "SUBSTRING(alert, '\"dateStartZoned\":\"(.*?)\"') <= :endTime", nativeQuery = true)
    List<ThousandEyeAlert> findByTimeRange(@Param("startTime") String startTime, @Param("endTime") String endTime);



}
