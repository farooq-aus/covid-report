package com.example.coronareport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class NewsQueryUtils extends CoronaQueryUtils {

    public static final String LOG_TAG = NewsQueryUtils.class.getName();

    NewsQueryUtils() {}

    public static ArrayList<NewsData> fetchNewsData(String requestUrl)
    {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        ArrayList<NewsData> newsList = extractFeatureFromJson(jsonResponse);

        return newsList;
    }

    private static ArrayList<NewsData> extractFeatureFromJson(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        ArrayList<NewsData> newsList = new ArrayList<>();

        try {

            JSONArray baseJsonResponse = new JSONObject(newsJSON).getJSONArray("articles");

            for (int i = 0; i < baseJsonResponse.length(); i++) {

                JSONObject currentArticle = baseJsonResponse.getJSONObject(i);

                String source = currentArticle.getJSONObject("source").getString("name");

                String title = currentArticle.getString("title");

                String url = currentArticle.getString("url");

                String imgSrc = currentArticle.getString("urlToImage");

                String time = currentArticle.getString("publishedAt");

                NewsData newsData = new NewsData(source, title, url, imgSrc, time);

                newsList.add(newsData);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        return newsList;
    }

}
