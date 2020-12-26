package com.example.coronareport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

public class CountryLoader extends AsyncTaskLoader<CountryData> {

    private static final String LOG_TAG = CoronaLoader.class.getName();

    String url1;
    String url2;

    public CountryLoader(Context context, String... urls) {
        super(context);
        this.url1 = urls[0];
        this.url2 = urls[1];
        Log.e(LOG_TAG, url2);
    }

    protected void onStartLoading() {
        forceLoad();
    }

    public CountryData loadInBackground() {
        if(url1 == null || url2 == null)
            return null;

        CountryData result = CountryQueryUtils.fetchCountryData(url1, url2);
        return result;
    }
}