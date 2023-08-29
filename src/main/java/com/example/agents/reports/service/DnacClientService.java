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

    JSONArray getDnacOneDayClientData(Date olddate, Date newdate);

    JSONArray getDnacClientData(Date startTime, Date endTime);

    List<DnacClient> getDnacData(String agentName, String startTime, String endTime, String application);
}
