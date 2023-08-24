package com.example.agents.dnacNetworkHealthData;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.agents.dnacNetworkHealthData.DnacNetworkHealthDataModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DnacNetworkHealthDataRepository extends JpaRepository<DnacNetworkHealthDataModel, Long> {


    @Query(value = "SELECT * FROM dnac_network_health_data WHERE time_stamp >= :newdate AND time_stamp <= :olddate" , nativeQuery = true)
    List<DnacNetworkHealthDataModel> findByTimeRange(@Param("olddate") Date olddate, @Param("newdate") Date newdate);
    // No need to define custom queries unless necessary
}

