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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;


public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<NewsData>>  {

    private static final String LOG_TAG = NewsFragment.class.getName();

    private static final int NEWS_LOADER_ID = 3;

    NewsAdapter newsAdapter;

    public static final String NEWS_REQUEST_URL =
            "https://saurav.tech/NewsAPI/top-headlines/category/health/us.json";

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

        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }

        if (newsList != null && !newsList.isEmpty()) {
            newsAdapter.addAll(newsList);
        }
    }

    public void onLoaderReset(Loader<ArrayList<NewsData>> loader) {
        newsAdapter.clear();
    }

}