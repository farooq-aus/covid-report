package com.example.coronareport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class GlobalQueryUtils extends CoronaQueryUtils {

    private static final String LOG_TAG = GlobalQueryUtils.class.getName();

    GlobalQueryUtils(){
        super();
    }

    public static GlobalData fetchGlobalData(String requestUrl1, String requestUrl2) {
        URL url = CoronaQueryUtils.createUrl(requestUrl1);
        URL url2 = createUrl(requestUrl2);

        String jsonResponse1 = null;
        String jsonResponse2 = null;

        try {
            jsonResponse1 = CoronaQueryUtils.makeHttpRequest(url);
            jsonResponse2 = CountryQueryUtils.makeHttpRequest(url2);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        GlobalData globalData = extractFeatureFromJson(jsonResponse1, jsonResponse2);

        return globalData;
    }

    public static GlobalData extractFeatureFromJson(String locationJSON, String worldJSON) {

        if (TextUtils.isEmpty(locationJSON)) {
            return null;
        }

        GlobalData globalData = new GlobalData();

        try {

            JSONObject locationJsonResponse = new JSONObject(locationJSON);

            String countryName = locationJsonResponse.getString("country");
            String cityName = locationJsonResponse.getString("regionName");

            JSONObject worldJsonResponse = new JSONObject(worldJSON);
            int worldCases = worldJsonResponse.getInt("cases");
            int worldDeaths = worldJsonResponse.getInt("deaths");

            String countryURL = "https://disease.sh/v3/covid-19/countries/" + countryName +
                    "?yesterday=false&twoDaysAgo=false&strict=true&allowNull=false";

            String countryJsonResponse = makeHttpRequest(createUrl(countryURL));
            JSONObject countryJsonObject = new JSONObject(countryJsonResponse);

            int countryCases = countryJsonObject.getInt("cases");
            int countryRecovered = countryJsonObject.getInt("recovered");
            int countryCriticalCases = countryJsonObject.getInt("critical");
            int countryDeaths = countryJsonObject.getInt("deaths");
            String flagUrl = countryJsonObject.getJSONObject("countryInfo").getString("flag");

            globalData = new GlobalData(worldCases, worldDeaths, cityName, countryName, countryCases, countryRecovered, countryCriticalCases, countryDeaths, flagUrl);

            } catch (JSONException | IOException e) {
            Log.e("QueryUtils", "Problem parsing the global JSON results", e);
        }

        return globalData;
    }

}
