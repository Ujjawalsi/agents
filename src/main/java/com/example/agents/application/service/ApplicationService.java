package com.example.agents.application.service;

import com.example.agents.application.entities.Application;

import java.util.List;

public interface ApplicationService {
    List<Application> findByName(String name);

    Application addApplication(Application application);
}
