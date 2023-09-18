package com.example.agents.application.implementation;

import com.example.agents.application.entities.Application;
import com.example.agents.application.repository.ApplicationRepo;
import com.example.agents.application.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationImpl implements ApplicationService {

    @Autowired
    private ApplicationRepo applicationRepo;
    @Override
    public List<Application> findByName(String appName) {
        List<Application> all = applicationRepo.findAll();
    return all.stream().filter(application -> application.getAppName().equalsIgnoreCase(appName)).collect(Collectors.toList());
    }

    @Override
    public Application addApplication(Application application) {
     return  applicationRepo.save(application);

    }

}
