package com.example.agents.inventoryData;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.agents.inventoryData.InventoryDataModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryDataRepository extends JpaRepository<InventoryDataModel, Long> {


    @Query(value = "SELECT * FROM ms_inventory WHERE data IS NOT NULL AND " +
            "CAST(alert AS JSON)->>'hostname' = :connectedDeviceName", nativeQuery = true)
    List<InventoryDataModel> findByHostname(@Param("connectedDeviceName")String connectedDeviceName);
}

