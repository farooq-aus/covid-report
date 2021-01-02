package com.example.coronareport;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class GlobalLoader extends AsyncTaskLoader<GlobalData> {

    private static final String LOG_TAG = GlobalLoader.class.getName();

    String url1;
    String url2;

    public GlobalLoader(Context context, String... url) {
        super(context);
        this.url1 = url[0];
        this.url2 = url[1];
    }

    protected void onStartLoading() {
        forceLoad();
    }

    public GlobalData loadInBackground() {
        if(url1 == null || url2 == null)
            return null;

        GlobalData result = GlobalQueryUtils.fetchGlobalData(url1, url2);
        return result;
    }
}