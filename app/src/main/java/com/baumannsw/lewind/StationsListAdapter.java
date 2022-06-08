package com.baumannsw.lewind;

import android.content.Context;
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
        ImageView imgArrow = convertView.findViewById(R.id.imgArrowList);
        ImageButton btnHistory = convertView.findViewById(R.id.btnHistoryList);
        LinearLayout layoutColor = convertView.findViewById(R.id.layoutColorList);

        btnHistory.setImageDrawable(convertView.getResources().getDrawable(android.R.drawable.ic_menu_recent_history, context.getTheme()));
        imgArrow.setImageDrawable(convertView.getResources().getDrawable(R.drawable.left_arrow_icon_free_svg_file, context.getTheme()));
        tvName.setText(stations.get(position).getName());

        if(stations.get(position).getRotation() != null) {
            imgArrow.setRotation(stations.get(position).getRotation());
            imgArrow.setVisibility(View.VISIBLE);
        }
        if(stations.get(position).getWind() != null) {
            int[] colors;
            int index = (int)(stations.get(position).getWind()/2);
            ArrayList<Integer> colorList = new ArrayList<>();
            colorList.clear();
            colorList.add(convertView.getResources().getColor(R.color.wind_0, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_2, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_4, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_6, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_8, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_10, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_12, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_14, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_16, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_18, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_20, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_22, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_24, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_26, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_28, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_30, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_32, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_34, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_36, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_38, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_40, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_42, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_44, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_46, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_48, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_50, context.getTheme()));
            colorList.add(convertView.getResources().getColor(R.color.wind_52, context.getTheme()));
            GradientDrawable gradient = (GradientDrawable) convertView.getResources().getDrawable(R.drawable.listview_gradient, context.getTheme()).getConstantState().newDrawable();
            gradient.mutate();
            if(index >= colorList.size())
                index = colorList.size() - 1;
            colors = gradient.getColors();
            colors[0] = colorList.get(index);
            gradient.setColors(colors);
            layoutColor.setBackground(gradient);
        }

        return convertView;
    }
}
