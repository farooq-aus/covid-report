package com.example.coronareport;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class GlobalLoader extends AsyncTaskLoader<String> {

    private static final String LOG_TAG = GlobalLoader.class.getName();

    String url;

    public GlobalLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    protected void onStartLoading() {
        forceLoad();
    }

    public String loadInBackground() {
        if(url == null)
            return null;

        String result = GlobalQueryUtils.fetchGlobalData(url);
        return result;
    }
}