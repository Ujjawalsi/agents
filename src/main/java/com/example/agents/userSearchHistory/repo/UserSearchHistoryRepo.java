package com.example.agents.userSearchHistory.repo;

import com.example.agents.userSearchHistory.entities.UserSearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSearchHistoryRepo extends JpaRepository<UserSearchHistory, Long> {

}
