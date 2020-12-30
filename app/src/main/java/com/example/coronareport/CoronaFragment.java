package com.example.coronareport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class CoronaFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<CoronaData>>  {

    public static final String LOG_TAG = CoronaFragment.class.getName();

    private static final int CORONA_LOADER_ID = 1;

    private CoronaAdapter coronaAdapter;

    public static String COVID_REQUEST_URL =
            "https://disease.sh/v3/covid-19/countries?yesterday=false&twoDaysAgo=false&sort=cases&allowNull=false";

    private ImageView emptyView;

    private SwipeRefreshLayout swipeRefresh;

    private Spinner spinner;

    private ShimmerFrameLayout shimmerFrameLayout;

    private SearchView searchView;

    public CoronaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.corona_activity, container, false);

        shimmerFrameLayout = (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_list);

        ListView coronaListView = (ListView) rootView.findViewById(R.id.list);

        coronaAdapter = new CoronaAdapter(getActivity(), new ArrayList<CoronaData>());
        coronaListView.setAdapter(coronaAdapter);

        emptyView = (ImageView) rootView.findViewById(R.id.empty_tw);
        coronaListView.setEmptyView(emptyView);

        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimaryDark));
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });

        spinner = (Spinner) rootView.findViewById(R.id.day_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
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

        android.app.LoaderManager loaderManager = getActivity().getLoaderManager();
        loaderManager.initLoader(CORONA_LOADER_ID, null, this);

        searchView = (SearchView) rootView.findViewById(R.id.search);
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

        ImageView searchButton = (ImageView) rootView.findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView i = (ImageView) v;
                if(searchView.getVisibility() == View.GONE) {
                    searchView.setVisibility(View.VISIBLE);
                    i.setImageResource(R.drawable.search_off_icon);
                }
                else {
                    searchView.setVisibility(View.GONE);
                    i.setImageResource(R.drawable.search_icon);
                }
            }
        });

        return rootView;
    }

    private void refreshList() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            android.app.LoaderManager loaderManager = getActivity().getLoaderManager();
            loaderManager.restartLoader(CORONA_LOADER_ID, null, this);
        }
        else {
            coronaAdapter.clear();
            emptyView.setImageResource(R.drawable.disconnected_icon);
            Toast.makeText(getActivity(), R.string.no_connection, Toast.LENGTH_SHORT).show();
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
        return new CoronaLoader(getActivity(), COVID_REQUEST_URL);
    }

    public void onLoadFinished(Loader<ArrayList<CoronaData>> loader, ArrayList<CoronaData> coronaList) {

        emptyView.setImageResource(R.drawable.not_found);
        coronaAdapter.clear();

        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);

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