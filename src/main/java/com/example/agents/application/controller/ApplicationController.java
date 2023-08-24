package com.example.agents.application.controller;

import com.example.agents.application.entities.Application;
import com.example.agents.application.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/add")
    public ResponseEntity<Application> addApplication(@RequestBody Application application){
        Application applications = applicationService.addApplication(application);
        return  new ResponseEntity<>(applications, HttpStatus.CREATED);
    }
}
