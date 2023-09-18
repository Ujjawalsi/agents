package com.example.agents.reports.implementation;

import com.example.agents.reports.entities.DnacClient;
import com.example.agents.reports.repository.DnacClientRepo;
import com.example.agents.reports.service.DnacClientService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public List<DnacClient> getDnacData(String agentName, String startTime, String endTime, String application) {
        List<DnacClient> jsonarray = new ArrayList<>();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start_Time = null;
            Date end_Time = null;
            try {
                start_Time = simpleDateFormat.parse(startTime);
                end_Time = simpleDateFormat.parse(endTime);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            List<DnacClient> dnacClients = dnacClientRepo.findByTimeRangeGap(start_Time, end_Time);
            for (DnacClient client: dnacClients) {
                JSONObject jsonObject = new JSONObject(client.getJsonDocument());
                String username = jsonObject.getString("username");
                List<String> userNameList = Arrays.asList(username.split(""));
                List<String> agentNameList = Arrays.asList(agentName.split(""));
                if (userNameList.containsAll(agentNameList)) {
                    jsonarray.add(client);
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return jsonarray;
    }
}
