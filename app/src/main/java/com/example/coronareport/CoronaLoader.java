package com.example.coronareport;

import android.content.Context;
import android.content.AsyncTaskLoader;
import java.util.ArrayList;

public class CoronaLoader extends AsyncTaskLoader<ArrayList<CoronaData>> {

    private static final String LOG_TAG = CoronaLoader.class.getName();

    String url;

    public CoronaLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    protected void onStartLoading() {
        forceLoad();
    }

    public ArrayList<CoronaData> loadInBackground() {
        if(url == null)
            return null;

        ArrayList<CoronaData> result = QueryUtils.fetchCoronaData(url);
        return result;
    }
}
