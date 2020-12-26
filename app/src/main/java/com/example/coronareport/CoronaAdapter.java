package com.example.coronareport;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CoronaAdapter extends ArrayAdapter<CoronaData> implements Filterable {

    private ArrayList<CoronaData> originalData;
    private ArrayList<CoronaData> displayData;

    public CoronaAdapter(Context context, ArrayList<CoronaData> coronaList) {
        super(context, 0, coronaList);
        this.originalData = coronaList;
        this.displayData = coronaList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        final CoronaData c = displayData.get(position);

        TextView tw = (TextView) listItemView.findViewById(R.id.country);
        tw.setText(c.country);

        TextView tw2 = (TextView) listItemView.findViewById(R.id.casesCount);
        setText(tw2, c.todayCases);

        TextView tw3 = (TextView) listItemView.findViewById(R.id.deathCount);
        setText(tw3, c.todayDeaths);

        TextView tw4 = (TextView) listItemView.findViewById(R.id.activeCount);
        setText(tw4, c.active);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getContext(), CountryActivity.class);
                bundle.putString("COUNTRY_NAME", c.country);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });

        return listItemView;

    }

    @Override
    public int getCount() {
        return displayData.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<CoronaData> filteredList = new ArrayList<>();

                if(originalData == null) {
                    originalData = new ArrayList<>(displayData);
                }

                if(charSequence == null || charSequence.length() == 0) {
                    results.count = originalData.size();
                    results.values = originalData;
                }

                else {
                    charSequence = charSequence.toString().toLowerCase();
                    for (CoronaData c : originalData) {
                        if (c.country.toLowerCase().startsWith(charSequence.toString())) {
                            filteredList.add(c);
                        }
                    }
                    results.count = filteredList.size();
                    results.values = filteredList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                displayData = (ArrayList<CoronaData>) filterResults.values;
                if(displayData.isEmpty())
                    Toast.makeText(getContext(), R.string.not_found, Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        };

        return filter;
    }

    public static void setText(TextView tw, int count)
    {
        if(count == -1)
            tw.setText(R.string.not_known);
        else
            tw.setText(NumberFormat.getNumberInstance(Locale.US).format(count));
    }

}
