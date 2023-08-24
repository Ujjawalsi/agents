package com.example.agents.inventoryData;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.agents.inventoryData.InventoryDataModel;
import org.springframework.data.jpa.repository.Query;

public interface InventoryDataRepository extends JpaRepository<InventoryDataModel, Long> {


    @Query(value = "SELECT * FROM ms_inventory WHERE data->> hostName = 'connectedDeviceName'", nativeQuery = true)
    InventoryDataModel findByHostname(String connectedDeviceName);
}

