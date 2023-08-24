package com.example.agents.reports.implementation;

import com.example.agents.customDate.DateTimeUtil;
import com.example.agents.reports.entities.DnacClient;
import com.example.agents.reports.repository.DnacClientRepo;
import com.example.agents.reports.service.DnacClientService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Service
public class DnacClientImpl implements DnacClientService {


    @Autowired
    private DnacClientRepo dnacClientRepo;

    @Override
    public void save(List<DnacClient> jsonObject) {

        dnacClientRepo.saveAll(jsonObject);

    }

    @Override
    public List<DnacClient> findByUsernameContainingIgnoreCaseAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(String userName, String endTime, String startTime) {
        return dnacClientRepo.findByUsernameContainingIgnoreCaseAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(userName,endTime,startTime);
    }


    @Override
    public JSONArray getDnacOneDayClientData(Date olddate, Date newdate) {
     List<DnacClient>dnacClients= dnacClientRepo.findByTimeRange(olddate, newdate);

        JSONArray jsonArray = new JSONArray();
        for (DnacClient data : dnacClients) {
            jsonArray.put(new JSONObject(data));
        }
        return jsonArray;
    }

    @Override
    public JSONArray getDnacClientData(Date startTime, Date endTime) {
        List<DnacClient> list = dnacClientRepo.findByTimeRangeGap(startTime, endTime);

        JSONArray jsonArray = new JSONArray();
        for (DnacClient data : list) {
            jsonArray.put(new JSONObject(data));
        }

        return jsonArray;
    }
}
