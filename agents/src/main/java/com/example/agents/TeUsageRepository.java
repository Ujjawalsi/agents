package com.example.agents;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TeUsageRepository extends JpaRepository<TeUsageModel, Long> {
}
