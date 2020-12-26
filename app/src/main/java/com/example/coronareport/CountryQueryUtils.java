package com.example.coronareport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class CountryQueryUtils extends QueryUtils {

    CountryQueryUtils(){
        super();
    }

    public static CountryData extractFeatureFromJson(String countryJSON) {

        if (TextUtils.isEmpty(countryJSON)) {
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
            int casesPerMillion = baseJsonResponse.optInt("casesPerOneMillion",-1);
            int deathsPerMillion = baseJsonResponse.optInt("deathsPerOneMillion",-1);
            int testsPerMillion = baseJsonResponse.optInt("testsPerOneMillion",-1);

            JSONObject countryInfo = baseJsonResponse.getJSONObject("countryInfo");
            String flagSrc = countryInfo.getString("flag");

            countryData = new CountryData(countryName, todayCases, todayDeaths, active, totalCases, totalDeaths,totalTests,recovered,critical,casesPerMillion, deathsPerMillion, testsPerMillion, flagSrc);

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the corona JSON results", e);
        }

        return countryData;
    }

}
