package com.FinalProject.demo.Models;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanvasjsChartData {


    static Map<Object, Object> casesMap = null;
    static List<List<Map<Object, Object>>> casesList = new ArrayList<List<Map<Object, Object>>>();
    static List<Map<Object, Object>> casesDataPoints = new ArrayList<Map<Object, Object>>();

    static {
        String url = "https://disease.sh/v3/covid-19/historical/all?lastdays=365";

        try {
            HttpResponse<String> response = Unirest.get(url).asString();
            String json = response.getBody();
            JSONObject jsonObj = new JSONObject(json);


            JSONObject cases = jsonObj.getJSONObject("cases");

            String month[] = new String[12];
            String[] monthNames = {"January","February","March","April","May","June","July","August","September","October","November","December"};


            int monthCases[] = new int[11];
            int j = 1;
            for(int i = 0; i < 11; i++) {
                j++;
                month[i] = j + "/1/20";
                monthCases[i] = cases.getInt(month[i]);
                System.out.println("CASES BY MONTH  " + monthCases[i] + "\n");
                casesMap = new HashMap<Object, Object>();
                casesMap.put("label", monthNames[i+1]);
                casesMap.put("y", monthCases[i]);
                casesDataPoints.add(casesMap);


            }
            casesList.add(casesDataPoints);

        } catch (Exception ex) {
            System.out.println(ex);

        }
    }
public static List<List<Map<Object, Object>>> getCanvasjsCasesDataList() {
        return casesList;
    }

}
