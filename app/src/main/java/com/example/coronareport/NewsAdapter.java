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

import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<NewsData> {

    public NewsAdapter(Context context, ArrayList<NewsData> newsList) {
        super(context, 0, newsList);
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
                Uri newsUri = Uri.parse(n.url);
                Intent intent = new Intent(Intent.ACTION_VIEW, newsUri);
                getContext().startActivity(intent);
            }
        });

        return listItemView;

    }

}

