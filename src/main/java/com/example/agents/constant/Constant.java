package com.example.agents.constant;


import com.sun.jdi.event.StepEvent;
import com.sun.jdi.request.StepRequest;
import lombok.Getter;


@Getter
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
    public static final String dnac_health_api="https://10.6.1.25/dna/intent/api/v1/device-health?deviceRole=AP&startTime=";
    public static final String flag = "false";
    public static final String Enterprise_Agent="INNADTEYE01L";
    public static final String te_api_key="Bearer 01f4a6f0-408a-4b42-820b-7b1c725f6bb7";
    public static final String  te_alert_api="https://api.thousandeyes.com/v6/alerts/";
    public static final String DOMAIN = "velocis.in";
    public static final String PROVIDER_URL = "ldap://192.168.100.240:389";

    public static final String dnac_user_name="admin";
    public static final String dnac_password="Pass2022";
    public static final String dnac_auth_api="https://10.6.1.25/dna/system/api/v1/auth/token";
    public static final String dnac_report_api="https://10.6.1.25/dna/intent/api/v1/data/reports/";
    public static final String te_agents_api="https://api.thousandeyes.com/v6/agents.json";
    public static final String dnac_network_health_api="https://10.6.1.25/dna/intent/api/v1/network-health";
    public static final String dnac_inventory_api="https://10.6.1.25/api/v1/network-device/";
    public static final String te_usage_api="https://api.thousandeyes.com/v6/usage.json";
    public static final String te_usage_api_key="Bearer 0b78607a-ce7d-4ee0-b3ad-5ae9d4e4542a";


    public static final String ldapUser="Asheesh Sharma";
    public static final String ldapPassword="Prashant@2023";




//    public  String name = userName;
//    public String pass = password;





}
