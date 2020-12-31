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

    public static String fetchGlobalData(String requestUrl) {
        URL url = CoronaQueryUtils.createUrl(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = CoronaQueryUtils.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        String globalData = extractFeatureFromJson(jsonResponse);

        return globalData;
    }

    public static String extractFeatureFromJson(String locationJSON) {

        if (TextUtils.isEmpty(locationJSON)) {
            return null;
        }

        String countryName = "";

        try {

            JSONObject locationJsonResponse = new JSONObject(locationJSON);

            countryName = locationJsonResponse.getString("country");

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the country JSON results", e);
        }

        return countryName;
    }


}
