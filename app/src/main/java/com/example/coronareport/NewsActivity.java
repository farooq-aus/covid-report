package com.example.coronareport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsData>>  {

    private static final String LOG_TAG = NewsActivity.class.getName();

    private static final int NEWS_LOADER_ID = 1;

    NewsAdapter newsAdapter;

    public static final String NEWS_REQUEST_URL =
            "https://saurav.tech/NewsAPI/top-headlines/category/health/us.json";

    private ImageView emptyView;

    private SwipeRefreshLayout swipeRefresh;

    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.news_shimmer_list);

        ListView newsListView = (ListView) findViewById(R.id.news_list);
        newsAdapter = new NewsAdapter(this, new ArrayList<NewsData>());
        newsListView.setAdapter(newsAdapter);

        emptyView = (ImageView) findViewById(R.id.empty_tw_news);
        newsListView.setEmptyView(emptyView);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.news_swipe_refresh);
        swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimaryDark));
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });

        android.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(NEWS_LOADER_ID, null, this);
    }

    private void refreshList() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            android.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.restartLoader(NEWS_LOADER_ID, null, this);
        }
        else {
            newsAdapter.clear();
            emptyView.setImageResource(R.drawable.disconnected_icon);
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
            emptyView.setVisibility(View.VISIBLE);
        }
        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    public Loader<ArrayList<NewsData>> onCreateLoader(int i, Bundle bundle) {
        if(!swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(true);
        }
        return new NewsLoader(this, NEWS_REQUEST_URL);
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