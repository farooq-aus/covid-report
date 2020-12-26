package com.example.coronareport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class CountryLoader extends AsyncTaskLoader<CountryData> {

    private static final String LOG_TAG = CoronaLoader.class.getName();

    String url;

    public CountryLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    protected void onStartLoading() {
        forceLoad();
    }

    public CountryData loadInBackground() {
        if(url == null)
            return null;

        CountryData result = CountryActivity.fetchCountryData(url);
        return result;
    }
}