package com.example.coronareport;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<NewsData>> {

    private static final String LOG_TAG = NewsLoader.class.getName();

    String url;

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    protected void onStartLoading() {
        forceLoad();
    }

    public ArrayList<NewsData> loadInBackground() {
        if(url == null)
            return null;

        ArrayList<NewsData> result = NewsQueryUtils.fetchNewsData(url);
        return result;
    }
}
