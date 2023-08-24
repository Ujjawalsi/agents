package com.example.agents.reports.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @RequestMapping(value="/", method=RequestMethod.GET)
    @ResponseBody
    public void createReportInDNAC(String l_name) {

    }

}

