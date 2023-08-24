package com.example.agents.dnacNetworkHealthData;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.agents.dnacNetworkHealthData.DnacNetworkHealthDataModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DnacNetworkHealthDataRepository extends JpaRepository<DnacNetworkHealthDataModel, Long> {

    @Query(value = "SELECT * FROM dnac_network_health_data WHERE timestamp <= :endTime AND timestamp >= :startTime", nativeQuery = true)
    List<DnacNetworkHealthDataModel> findByTimeStampLessThanEqualAndTimeStampGreaterThanEqual(String endTime, String startTime);
    // No need to define custom queries unless necessary
}

