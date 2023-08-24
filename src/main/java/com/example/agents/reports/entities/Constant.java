package com.example.agents.reports.entities;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@PropertySource("classpath:com/vel/common/connector/service/constants.properties")
public class Constant {



//    @Value("${dnac.user.name.second}")
//    public String dnacUserName;
//
//    @Value("${dnac.password.second}")
//    private String dnacPassword;
//
//    @Value("${app.domain.name}")
//    private String userName;
//
//    @Value("${app.agent.name}")
//    private String password;


//
//    @Value("${awsFlag}")
//    private static boolean awsFlag;

//    @Value("${dnac_health_api}")
//    public static String dnac_health_api;


    public  static final String USERNAME = "admin";
    public static final String PASSWORD = "Pass2018";
    public static final boolean awsFlag = false;
    public static String dnac_health_api="https://10.6.1.25/dna/intent/api/v1/device-health?deviceRole=AP&startTime=";



//    public  String name = userName;
//    public String pass = password;





}
