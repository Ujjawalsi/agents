package com.example.agents.application.repository;

import com.example.agents.application.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Repository
public interface ApplicationRepo extends JpaRepository<Application, Long> {

    @Query(value = "SELECT * FROM applications WHERE app_name LIKE '%:appName%'", nativeQuery = true)
    List<Application> findByAppName(@Param("appName") String appName);
}
