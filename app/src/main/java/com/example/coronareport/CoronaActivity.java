package com.example.coronareport;

import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;

import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class CoronaActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<CoronaData>> {

    public static final String LOG_TAG = CoronaActivity.class.getName();

    private static final int CORONA_LOADER_ID = 1;

    private CoronaAdapter coronaAdapter;

    public static String COVID_REQUEST_URL =
            "https://disease.sh/v3/covid-19/countries?yesterday=false&twoDaysAgo=false&sort=cases&allowNull=false";

    private ImageView emptyView;

    private SwipeRefreshLayout swipeRefresh;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.corona_activity);

        ListView coronaListView = (ListView) findViewById(R.id.list);

        coronaAdapter = new CoronaAdapter(this, new ArrayList<CoronaData>());
        coronaListView.setAdapter(coronaAdapter);

        emptyView = (ImageView) findViewById(R.id.empty_tw);
        coronaListView.setEmptyView(emptyView);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimaryDark));
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });

        spinner = (Spinner) findViewById(R.id.day_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_options, R.layout.spinner_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1:
                        COVID_REQUEST_URL = "https://disease.sh/v3/covid-19/countries?yesterday=true&twoDaysAgo=false&sort=cases&allowNull=false";
                        break;
                    case 2:
                        COVID_REQUEST_URL = "https://disease.sh/v3/covid-19/countries?yesterday=false&twoDaysAgo=true&sort=cases&allowNull=false";
                        break;
                    default:
                        COVID_REQUEST_URL = "https://disease.sh/v3/covid-19/countries?yesterday=false&twoDaysAgo=false&sort=cases&allowNull=false";
                        break;
                }
                swipeRefresh.setRefreshing(true);
                refreshList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        android.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(CORONA_LOADER_ID, null, this);

        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                coronaAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void refreshList() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            android.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.restartLoader(CORONA_LOADER_ID, null, this);
        }
        else {
            coronaAdapter.clear();
            emptyView.setImageResource(R.drawable.disconnected_icon);
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
            emptyView.setVisibility(View.VISIBLE);
        }
        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    public Loader<ArrayList<CoronaData>> onCreateLoader(int i, Bundle bundle) {
        if(!swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(true);
        }
        return new CoronaLoader(this, COVID_REQUEST_URL);
    }

    public void onLoadFinished(Loader<ArrayList<CoronaData>> loader, ArrayList<CoronaData> coronaList) {

        emptyView.setImageResource(R.drawable.not_found);
        coronaAdapter.clear();

        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }

        if (coronaList != null && !coronaList.isEmpty()) {
            coronaAdapter.addAll(coronaList);
        }
    }

    public void onLoaderReset(Loader<ArrayList<CoronaData>> loader) {
        coronaAdapter.clear();
    }

}