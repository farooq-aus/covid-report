package com.example.coronareport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

public class GlobalFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    public static final String LOG_TAG = GlobalFragment.class.getName();

    private static final int GLOBAL_LOADER_ID = 4;

    public static String GLOBAL_REQUEST_URL =
            "https://disease.sh/v3/covid-19/all?yesterday=false&twoDaysAgo=false&allowNull=false\n";

    TextView countryName;

    private ImageView emptyView;

    private Spinner spinner;

    private ShimmerFrameLayout shimmerFrameLayout;

    public GlobalFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.global_fragment, container, false);

        countryName = rootView.findViewById(R.id.country_name);

        android.app.LoaderManager loaderManager = getActivity().getLoaderManager();
        loaderManager.initLoader(GLOBAL_LOADER_ID, null, this);

        loaderManager = getActivity().getLoaderManager();
        loaderManager.initLoader(GLOBAL_LOADER_ID, null, this);

        return rootView;
    }

    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String url = "http://ip-api.com/json";
        return new GlobalLoader(getActivity(), url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        countryName.setText(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}