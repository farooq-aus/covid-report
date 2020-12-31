package com.example.coronareport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CoronaQueryUtils {

    public static final String LOG_TAG = CoronaQueryUtils.class.getName();

    CoronaQueryUtils() {}

    public static ArrayList<CoronaData> fetchCoronaData(String requestUrl)
    {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        ArrayList<CoronaData> coronaList = extractFeatureFromJson(jsonResponse);

        return coronaList;
    }

    private static ArrayList<CoronaData> extractFeatureFromJson(String coronaJSON) {

        if (TextUtils.isEmpty(coronaJSON)) {
            return null;
        }

        ArrayList<CoronaData> coronaList = new ArrayList<>();

        try {

            JSONArray baseJsonResponse = new JSONArray(coronaJSON);

            for (int i = 0; i < baseJsonResponse.length(); i++) {

                JSONObject currentCountry = baseJsonResponse.getJSONObject(i);

                String countryName = currentCountry.getString("country");

                int todayCases = currentCountry.getInt("todayCases");

                int todayDeaths = currentCountry.getInt("todayDeaths");

                int active = currentCountry.getInt("active");

                CoronaData coronaData = new CoronaData(countryName, todayCases, todayDeaths, active);

                coronaList.add(coronaData);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the corona JSON results", e);
        }

        return coronaList;
    }

    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(20000 /* milliseconds */);
            urlConnection.setConnectTimeout(30000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the corona JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
