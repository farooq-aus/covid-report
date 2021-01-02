package com.example.coronareport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

public class GlobalFragment extends Fragment implements LoaderManager.LoaderCallbacks<GlobalData> {

    public static final String LOG_TAG = GlobalFragment.class.getName();

    private static final int GLOBAL_LOADER_ID = 4;

    public static String GLOBAL_REQUEST_URL =
            "https://disease.sh/v3/covid-19/all?yesterday=false&twoDaysAgo=false&allowNull=false\n";

    TextView worldCases,worldDeaths, countryName,cityName, countryCases,countryRecovered,countryCritical,countryDeaths;
    ImageView countryFlag;

    LinearLayout globalLayout, countryLayout;

    private Spinner spinner;

    private ShimmerFrameLayout shimmerFrameLayout;

    public GlobalFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.global_fragment, container, false);

        globalLayout = rootView.findViewById(R.id.global_box);
        countryLayout = rootView.findViewById(R.id.gcountry_box);

        shimmerFrameLayout = rootView.findViewById(R.id.global_shimmer);
        globalLayout.setVisibility(View.GONE);
        countryLayout.setVisibility(View.GONE);

        worldCases = rootView.findViewById(R.id.wcasesCount);
        worldDeaths = rootView.findViewById(R.id.wdeathCount);

        countryName = rootView.findViewById(R.id.gcountry_name);
        cityName = rootView.findViewById(R.id.gcity_name);

        countryCases = rootView.findViewById(R.id.gcasesCount);
        countryRecovered = rootView.findViewById(R.id.grecoveredCount);
        countryCritical = rootView.findViewById(R.id.gcriticalCount);
        countryDeaths = rootView.findViewById(R.id.gdeathCount);

        countryFlag = rootView.findViewById(R.id.gflag);

        android.app.LoaderManager loaderManager = getActivity().getLoaderManager();
        loaderManager.initLoader(GLOBAL_LOADER_ID, null, this);

        return rootView;
    }

    @Override
    public Loader<GlobalData> onCreateLoader(int id, @Nullable Bundle args) {
        String url1 = "http://ip-api.com/json";
        return new GlobalLoader(getActivity(), url1, GLOBAL_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<GlobalData> loader, final GlobalData data) {

        Picasso.get()
                .load(data.flagUrl)
                .placeholder(R.drawable.world_icon)
                .into(countryFlag, new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess() {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        globalLayout.setVisibility(View.VISIBLE);
                        countryLayout.setVisibility(View.VISIBLE);
                        //loadingIndicator.setVisibility(View.GONE);
                        //countryInfo.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(LOG_TAG, "Problem loading Flag");
                    }
                });

        CoronaAdapter.setText(worldCases, data.worldCases);
        CoronaAdapter.setText(worldDeaths, data.worldDeaths);

        countryName.setText(data.countryName);
        cityName.setText(data.cityName);

        CoronaAdapter.setText(countryCases, data.countryCases);
        CoronaAdapter.setText(countryRecovered, data.countryRecovered);
        CoronaAdapter.setText(countryCritical, data.countryCriticalCases);
        CoronaAdapter.setText(countryDeaths, data.countryDeaths);

        countryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getContext(), CountryActivity.class);
                bundle.putString("COUNTRY_NAME", data.countryName);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });

    }

    @Override
    public void onLoaderReset(@NonNull Loader<GlobalData> loader) {

    }
}