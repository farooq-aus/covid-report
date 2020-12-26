package com.example.coronareport;

import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Paint;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;

public class CountryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<CountryData>  {

    public static final String LOG_TAG = CountryActivity.class.getName();

    private static final int COUNTRY_LOADER_ID = 2;

    private LinearLayout countryInfo;

    GraphView gw;
    LineGraphSeries<DataPoint> caseSeries;
    LineGraphSeries<DataPoint> deathSeries;
    LineGraphSeries<DataPoint> recoverySeries;

    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_activity);

        Bundle bundle = getIntent().getExtras();

        countryInfo = (LinearLayout) findViewById(R.id.country_info);

        android.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(COUNTRY_LOADER_ID, bundle, this);

        ImageView closeBtn = (ImageView) findViewById(R.id.close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getCountryData();

        final TextView trendText = (TextView) findViewById(R.id.trendlinetext);

        radioGroup = (RadioGroup) findViewById(R.id.rg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                gw.removeAllSeries();
                switch(i){
                    case R.id.rb2:
                        gw.addSeries(deathSeries);
                        trendText.setTextColor(getResources().getColor(R.color.colorDeathTrend));
                        break;
                    case R.id.rb3:
                        gw.addSeries(recoverySeries);
                        trendText.setTextColor(getResources().getColor(R.color.colorRecoveredTrend));
                        break;
                    default:
                        gw.addSeries(caseSeries);
                        trendText.setTextColor(getResources().getColor(R.color.colorAccent));
                        break;
                }
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
        StringBuilder builder2 = new StringBuilder("https://disease.sh/v3/covid-19/historical/");
        builder2.append(bundle.getString("COUNTRY_NAME"));
        builder2.append("?lastdays=15");
        return new CountryLoader(this, builder.toString(), builder2.toString());
    }

    @Override
    public void onLoadFinished(Loader<CountryData> loader, CountryData countryData) {
        final View loadingIndicator = findViewById(R.id.progress);

        ImageView iw = (ImageView) findViewById(R.id.flag);
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
                        Log.e(LOG_TAG, "Problem loading Flag");
                    }
                });

        gw = (GraphView) findViewById(R.id.graph);
        gw.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        try {
            DataPoint[] caseDataPoints = new DataPoint[countryData.casesTrend.size()];
            DataPoint[] deathDataPoints = new DataPoint[countryData.casesTrend.size()];
            DataPoint[] recoveryDataPoints = new DataPoint[countryData.casesTrend.size()];

            for(int i = 0; i < countryData.casesTrend.size(); i++) {
                caseDataPoints[i] = new DataPoint(i, countryData.casesTrend.get(i));
                deathDataPoints[i] = new DataPoint(i, countryData.deathsTrend.get(i));
                recoveryDataPoints[i] = new DataPoint(i, countryData.recoveriesTrend.get(i));
            }

            caseSeries = new LineGraphSeries<>(caseDataPoints);
            deathSeries = new LineGraphSeries<>(deathDataPoints);
            recoverySeries = new LineGraphSeries<>(recoveryDataPoints);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(getResources().getColor(R.color.colorAccent));
            caseSeries.setCustomPaint(paint);


            Paint paint2 = new Paint();
            paint2.setStyle(Paint.Style.STROKE);
            paint2.setColor(getResources().getColor(R.color.colorDeathTrend));
            deathSeries.setCustomPaint(paint2);

            Paint paint3 = new Paint();
            paint3.setStyle(Paint.Style.STROKE);
            paint3.setColor(getResources().getColor(R.color.colorRecoveredTrend));
            recoverySeries.setCustomPaint(paint3);

            caseSeries.setDataPointsRadius(10);
            deathSeries.setDataPointsRadius(10);
            recoverySeries.setDataPointsRadius(10);

            caseSeries.setDrawDataPoints(true);
            deathSeries.setDrawDataPoints(true);
            recoverySeries.setDrawDataPoints(true);

            gw.addSeries(caseSeries);

        } catch (IllegalArgumentException e) {
            Log.e(LOG_TAG, "Problem setting trend data.");
        }

        setTexts(countryData);
    }

    @Override
    public void onLoaderReset(Loader<CountryData> loader) {

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

    }

}