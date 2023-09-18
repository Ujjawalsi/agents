package com.example.agents.reports.controller;

import com.example.agents.reports.service.ThousandEyeAlertService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Enumeration;

@RestController
public class CallbackPost {

    @Autowired
    private ThousandEyeAlertService thousandEyeAlertService;

    @PostMapping("/BullsEye/reports-callback")
    public ResponseEntity<String> readURL(HttpServletRequest l_request) {
        if (l_request != null) {
            Enumeration<String> authorizationHeader = l_request.getHeaderNames();
            if (authorizationHeader != null) {
                thousandEyeAlertService.process(l_request);
                return new ResponseEntity<>("Completed Successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Authorization header missing", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("HttpServletRequest is null", HttpStatus.BAD_REQUEST);
        }
    }
}









