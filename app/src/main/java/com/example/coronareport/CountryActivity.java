package com.example.coronareport;

import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;

public class CountryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<CountryData>  {

    public static final String LOG_TAG = CountryActivity.class.getName();

    private static final int COUNTRY_LOADER_ID = 2;

    private LinearLayout countryInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_activity);

        Bundle bundle = getIntent().getExtras();

        countryInfo = (LinearLayout) findViewById(R.id.country_info);

        android.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(COUNTRY_LOADER_ID, bundle, this);

        getCountryData();

        ImageView closeBtn = (ImageView) findViewById(R.id.close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getCountryData() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            android.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(COUNTRY_LOADER_ID, null, this);
            countryInfo.setVisibility(View.GONE);
        }
        else
        {
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Loader<CountryData> onCreateLoader(int i, Bundle bundle) {
        StringBuilder builder = new StringBuilder("https://disease.sh/v3/covid-19/countries/");
        builder.append(bundle.getString("COUNTRY_NAME"));
        builder.append("?yesterday=false&twoDaysAgo=false&strict=true&allowNull=true");
        return new CountryLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<CountryData> loader, CountryData countryData) {
        final View loadingIndicator = findViewById(R.id.progress);

        ImageView iw = (ImageView) findViewById(R.id.flag) ;
        Picasso.get()
                .load(countryData.flagSrc)
                .into(iw, new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess() {
                        loadingIndicator.setVisibility(View.GONE);
                        countryInfo.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

        setTexts(countryData);
    }

    @Override
    public void onLoaderReset(Loader<CountryData> loader) {

    }

    public static CountryData fetchCountryData(String requestUrl) {
        URL url = CountryQueryUtils.createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = CountryQueryUtils.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        CountryData coronaData = CountryQueryUtils.extractFeatureFromJson(jsonResponse);

        return coronaData;
    }

    private void setTexts(CountryData countryData) {
        TextView tw = (TextView) findViewById(R.id.ccountry);
        tw.setText(countryData.country);

        TextView tw2 = (TextView) findViewById(R.id.cactiveCount);
        CoronaAdapter.setText(tw2, countryData.active);

        TextView tw3 = (TextView) findViewById(R.id.ccasesCount);
        CoronaAdapter.setText(tw3, countryData.todayCases);

        TextView tw4 = (TextView) findViewById(R.id.ctotalCasesCount);
        CoronaAdapter.setText(tw4, countryData.totalCases);

        TextView tw5 = (TextView) findViewById(R.id.cdeathCount);
        CoronaAdapter.setText(tw5, countryData.todayDeaths);

        TextView tw6 = (TextView) findViewById(R.id.ctotalDeathCount);
        CoronaAdapter.setText(tw6, countryData.totalDeaths);

        TextView tw7 = (TextView) findViewById(R.id.crecovered);
        CoronaAdapter.setText(tw7, countryData.recovered);

        TextView tw8 = (TextView) findViewById(R.id.ccritical);
        CoronaAdapter.setText(tw8, countryData.critical);

        TextView tw9 = (TextView) findViewById(R.id.ctests);
        CoronaAdapter.setText(tw9, countryData.totalTests);

        TextView tw10 = (TextView) findViewById(R.id.ccasespermil);
        CoronaAdapter.setText(tw10, countryData.casesPerMillion);

        TextView tw11 = (TextView) findViewById(R.id.cdeathspermil);
        CoronaAdapter.setText(tw11, countryData.deathsPerMillion);

        TextView tw12 = (TextView) findViewById(R.id.ctestspermil);
        CoronaAdapter.setText(tw12, countryData.testsPerMillion);

    }

}