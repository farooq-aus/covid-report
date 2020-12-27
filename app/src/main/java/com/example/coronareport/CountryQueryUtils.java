package com.example.coronareport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class CountryQueryUtils extends QueryUtils {

    CountryQueryUtils(){
        super();
    }

    public static CountryData fetchCountryData(String requestUrl1, String requestUrl2) {
        URL url = createUrl(requestUrl1);
        URL url2 = createUrl(requestUrl2);

        String jsonResponse1 = null;
        String jsonResponse2 = null;

        try {
            jsonResponse1 = CountryQueryUtils.makeHttpRequest(url);
            jsonResponse2 = CountryQueryUtils.makeHttpRequest(url2);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        CountryData coronaData = extractFeatureFromJson(jsonResponse1, jsonResponse2);

        return coronaData;
    }

    public static CountryData extractFeatureFromJson(String countryJSON, String trendJSON) {

        if (TextUtils.isEmpty(countryJSON) || TextUtils.isEmpty(trendJSON)) {
            return null;
        }

        CountryData countryData = new CountryData();

        try {

            JSONObject baseJsonResponse = new JSONObject(countryJSON);

            String countryName = baseJsonResponse.getString("country");
            int todayCases = baseJsonResponse.optInt("todayCases",-1);
            int todayDeaths = baseJsonResponse.optInt("todayDeaths",-1);
            int recovered = baseJsonResponse.optInt("recovered",-1);
            int totalCases = baseJsonResponse.optInt("cases",-1);
            int totalDeaths = baseJsonResponse.optInt("deaths",-1);
            int totalTests = baseJsonResponse.optInt("tests",-1);
            int active = baseJsonResponse.optInt("active",-1);
            int critical = baseJsonResponse.optInt("critical",-1);

            JSONObject countryInfo = baseJsonResponse.getJSONObject("countryInfo");
            String flagSrc = countryInfo.getString("flag");

            ArrayList<Integer> casesTrend = new ArrayList<>();
            ArrayList<Integer> deathsTrend = new ArrayList<>();
            ArrayList<Integer> recoveriesTrend = new ArrayList<>();

            JSONObject timelineJsonResponse = new JSONObject(trendJSON).getJSONObject("timeline");
            for (Iterator<String> it = timelineJsonResponse.getJSONObject("cases").keys(); it.hasNext(); ) {
                String key = it.next();
                casesTrend.add(timelineJsonResponse.getJSONObject("cases").optInt(key,0));
                deathsTrend.add(timelineJsonResponse.getJSONObject("deaths").optInt(key,0));
                recoveriesTrend.add(timelineJsonResponse.getJSONObject("recovered").optInt(key,0));
            }

            countryData = new CountryData(countryName, todayCases, todayDeaths, active, totalCases, totalDeaths, totalTests, recovered, critical, flagSrc, casesTrend, deathsTrend, recoveriesTrend);

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the country JSON results", e);
        }

        return countryData;
    }

}
