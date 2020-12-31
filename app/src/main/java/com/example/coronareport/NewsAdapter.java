package com.example.coronareport;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<NewsData> {

    private Context callerContext;

    public NewsAdapter(Context context, ArrayList<NewsData> newsList) {
        super(context, 0, newsList);
        this.callerContext = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        final NewsData n = getItem(position);

        ImageView iw = (ImageView) listItemView.findViewById(R.id.newsImage);
        iw.setClipToOutline(true);
        Picasso.get()
                .load(n.imgSrc)
                .placeholder(R.drawable.news_placeholder)
                .fit()
                .into(iw);

        TextView tw = (TextView) listItemView.findViewById(R.id.source);
        tw.setText(n.source);

        TextView tw2 = (TextView) listItemView.findViewById(R.id.title);

        String displayTitle = n.title.split(" - | \\| ")[0];

        tw2.setText(displayTitle);

        TextView tw3 = (TextView) listItemView.findViewById(R.id.time);

        String displayDate = "";
        try {
            Date parsedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(n.time);
            displayDate = new SimpleDateFormat("HH:mm MMM dd, yyyy", Locale.getDefault()).format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tw3.setText(displayDate);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChromeCustomTab(n.url);
            }
        });

        return listItemView;

    }

    private void openChromeCustomTab(String url) {

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabColorSchemeParams params = new CustomTabColorSchemeParams.Builder()
                .setNavigationBarColor(ContextCompat.getColor(callerContext,R.color.colorPrimaryDark))
                .setToolbarColor(ContextCompat.getColor(callerContext,R.color.colorAccent))
                .setSecondaryToolbarColor(ContextCompat.getColor(callerContext,R.color.colorPrimaryDark))
                .build();
        builder.setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK, params);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(callerContext, Uri.parse(url));

    }

}

