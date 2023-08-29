package com.example.agents.reports.controller;

//@RestController
//public class CallbackPost {
//
//    @Autowired
//    private ThousandEyeAlertService thousandEyeAlertService;
//
//
//@PostMapping("/reports-callback")
//    public ResponseEntity<String> readURL(@RequestHeader(value = "Authorization", required = false) HttpServletRequest l_request) {
//    System.out.println("servlet --->>" + l_request);
//        System.out.println("======== " + l_request.getHeaderNames());
//        thousandEyeAlertService.process(l_request);
//        return new ResponseEntity<>("Amazing.....>", HttpStatus.OK);
//
//    }
//
//
//}



import com.example.agents.reports.service.ThousandEyeAlertService;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("BullsEye/reports-callback")
    public ResponseEntity<String> readURL(HttpServletRequest l_request) {
        if (l_request != null) {
            Enumeration<String> authorizationHeader = l_request.getHeaderNames();
            if (authorizationHeader != null) {
                System.out.println("Authorization Header: " + authorizationHeader);
                thousandEyeAlertService.process(l_request);

                return new ResponseEntity<>("Amazing.....>", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Authorization header missing", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("HttpServletRequest is null", HttpStatus.BAD_REQUEST);
        }
    }
}









