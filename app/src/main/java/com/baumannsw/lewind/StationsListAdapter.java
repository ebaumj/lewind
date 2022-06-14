package com.baumannsw.lewind;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baumannsw.lewind.stations.WindStation;
import com.baumannsw.lewind.windData.WindColor;

import java.util.ArrayList;

public class StationsListAdapter extends BaseAdapter {
    Context context;
    ArrayList<ListElement> stations;
    LayoutInflater inflater;

    public StationsListAdapter(Context appContext, ArrayList<ListElement> stations) {
        this.stations = stations;
        context = appContext;
        inflater = LayoutInflater.from(appContext);
    }

    @Override
    public int getCount() { return stations.size(); }

    @Override
    public Object getItem(int position) { return stations.get(position); }

    @Override
    public long getItemId(int position) { return stations.get(position).getId(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.station_list_element, null);
        TextView tvName = convertView.findViewById(R.id.tvStationNameList);
        tvName.setTypeface(convertView.getResources().getFont(R.font.andika_new_basic));
        ImageView imgArrow = convertView.findViewById(R.id.imgArrowList);
        LinearLayout layoutColor = convertView.findViewById(R.id.layoutColorList);

        imgArrow.setImageDrawable(convertView.getResources().getDrawable(R.drawable.left_arrow_icon_free_svg_file, context.getTheme()));
        tvName.setText(stations.get(position).getName());

        if(stations.get(position).getRotation() != null) {
            imgArrow.setRotation(stations.get(position).getRotation());
            imgArrow.setVisibility(View.VISIBLE);
        }
        if(stations.get(position).getWind() != null) {
            int[] colors;
            GradientDrawable gradient = (GradientDrawable) convertView.getResources().getDrawable(R.drawable.listview_gradient, context.getTheme()).getConstantState().newDrawable();
            gradient.mutate();
            colors = gradient.getColors();
            colors[0] = WindColor.getWindColor(context, stations.get(position).getWind());
            gradient.setColors(colors);
            layoutColor.setBackground(gradient);
        }

        return convertView;
    }
}
