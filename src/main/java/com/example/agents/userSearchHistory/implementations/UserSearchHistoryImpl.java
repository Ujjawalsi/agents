package com.example.agents.userSearchHistory.implementations;

import com.example.agents.userSearchHistory.entities.UserSearchHistory;
import com.example.agents.userSearchHistory.repo.UserSearchHistoryRepo;
import com.example.agents.userSearchHistory.services.UserSearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSearchHistoryImpl implements UserSearchHistoryService {

    @Autowired
    private UserSearchHistoryRepo userSearchHistoryRepo;
    @Override
    public void addUser(UserSearchHistory userSearch) {
        userSearchHistoryRepo.save(userSearch);
    }
}
