package com.example.agents.application.implementation;

import com.example.agents.application.entities.Application;
import com.example.agents.application.repository.ApplicationRepo;
import com.example.agents.application.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationImpl implements ApplicationService {

    @Autowired
    private ApplicationRepo applicationRepo;
    @Override
    public List<Application> findByName(String appName) {
        return applicationRepo.findByAppName(appName);
    }

    @Override
    public Application addApplication(Application application) {
     return  applicationRepo.save(application);

    }

}
