package com.example.coronareport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;


public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<NewsData>>  {

    private static final String LOG_TAG = NewsFragment.class.getName();

    private static final int NEWS_LOADER_ID = 3;

    NewsAdapter newsAdapter;

    public static String NEWS_REQUEST_URL =
            "https://saurav.tech/NewsAPI/top-headlines/category/health/us.json";

    String SOURCE_PARAM = "us";

    private ImageView emptyView;

    private SwipeRefreshLayout swipeRefresh;

    private ShimmerFrameLayout shimmerFrameLayout;

    public NewsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news_fragment, container, false);;

        shimmerFrameLayout = (ShimmerFrameLayout) rootView.findViewById(R.id.news_shimmer_list);

        ListView newsListView = (ListView) rootView.findViewById(R.id.news_list);
        newsAdapter = new NewsAdapter(getActivity(), new ArrayList<NewsData>());
        newsListView.setAdapter(newsAdapter);

        emptyView = (ImageView) rootView.findViewById(R.id.empty_tw_news);
        newsListView.setEmptyView(emptyView);

        Spinner spinner = (Spinner) rootView.findViewById(R.id.news_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.news_spinner_options, R.layout.spinner_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        SOURCE_PARAM = "in";
                        break;
                    case 2:
                        SOURCE_PARAM = "au";
                        break;
                    case 3:
                        SOURCE_PARAM = "ru";
                        break;
                    case 4:
                        SOURCE_PARAM = "fr";
                        break;
                    case 5:
                        SOURCE_PARAM = "gb";
                        break;
                    default:
                        SOURCE_PARAM = "us";
                        break;
                }
                NEWS_REQUEST_URL = "https://saurav.tech/NewsAPI/top-headlines/category/health/"+SOURCE_PARAM+".json";
                swipeRefresh.setRefreshing(true);
                refreshList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.news_swipe_refresh);
        swipeRefresh.setProgressBackgroundColorSchemeColor(rootView.getResources().getColor(R.color.colorPrimaryDark));
        swipeRefresh.setColorSchemeColors(rootView.getResources().getColor(R.color.colorAccent));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });

        android.app.LoaderManager loaderManager = getActivity().getLoaderManager();
        loaderManager.initLoader(NEWS_LOADER_ID, null, this);

        return rootView;
    }

    private void refreshList() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            android.app.LoaderManager loaderManager = getActivity().getLoaderManager();
            loaderManager.restartLoader(NEWS_LOADER_ID, null, this);
        }
        else {
            newsAdapter.clear();
            emptyView.setImageResource(R.drawable.disconnected_icon);
            Toast.makeText(getActivity(), R.string.no_connection, Toast.LENGTH_SHORT).show();
            emptyView.setVisibility(View.VISIBLE);
        }
        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    public Loader<ArrayList<NewsData>> onCreateLoader(int i, Bundle bundle) {
//        if(!swipeRefresh.isRefreshing()) {
//            swipeRefresh.setRefreshing(true);
//        }
        return new NewsLoader(getActivity(), NEWS_REQUEST_URL);
    }

    public void onLoadFinished(Loader<ArrayList<NewsData>> loader, ArrayList<NewsData> newsList) {

        emptyView.setImageResource(R.drawable.not_found);
        newsAdapter.clear();

        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);

        if (newsList != null && !newsList.isEmpty()) {
            newsAdapter.addAll(newsList);
        }

        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }

    }

    public void onLoaderReset(Loader<ArrayList<NewsData>> loader) {
        newsAdapter.clear();
    }

}