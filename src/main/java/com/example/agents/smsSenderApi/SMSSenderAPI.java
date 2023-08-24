package com.example.agents.smsSenderApi;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class SMSSenderAPI {

    final static String URL_HOST="http://sms.hspsms.com";
    public static void main(String[] args) {
        String response = new SMSSenderAPI().SMSSender("9560065085","Dear Ranvijay, Credentials to access Velocis WiFi are as follows: User Name: ranvijay@velocis.in Password: qwerty123 Velocis Guest Portal");
        System.out.println(response);
//		System.out.println("Successfully Sent SMS");
    }

    public String SMSSender(String mobile,String message)
    {
        String strTemp = "";
        try {

            RestTemplate restTemplate = new RestTemplate();
            String baseUrl = "http://bulksmsindia.mobi/sendurl.aspx?user=20081030&pwd=samanvay@123&senderid=VLCPVT&mobileno="+mobile+"&msgtext="+message;
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
            ResponseEntity<String> result = restTemplate.exchange(baseUrl, HttpMethod.GET, requestEntity, String.class);
            System.out.println("SMS sent successfully"+result.getBody());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  strTemp;
    }
}