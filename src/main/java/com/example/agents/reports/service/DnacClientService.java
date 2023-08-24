package com.example.agents.reports.service;

import com.example.agents.reports.entities.DnacClient;
import org.json.JSONArray;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Component
public interface DnacClientService {

    void save(List<DnacClient> jsonObject);

    List<DnacClient> findByUsernameContainingIgnoreCaseAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(String userName, String endTime, String startTime);
//     public JSONArray getDnacClientData(Date startTime, Date endTime);


    JSONArray getDnacOneDayClientData(Date olddate, Date newdate);

    JSONArray getDnacClientData(Date startTime, Date endTime);
}
