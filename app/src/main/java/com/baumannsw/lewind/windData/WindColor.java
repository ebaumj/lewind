package com.baumannsw.lewind.windData;

import android.content.Context;

import com.baumannsw.lewind.R;

import java.util.ArrayList;

public abstract class WindColor {

    public static Integer getWindColor(Context context, double wind) {
        ArrayList<Integer> colorList = new ArrayList<>();
        int index;
        colorList.clear();
        colorList.add(context.getResources().getColor(R.color.wind_0, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_2, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_4, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_6, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_8, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_10, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_12, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_14, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_16, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_18, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_20, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_22, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_24, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_26, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_28, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_30, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_32, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_34, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_36, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_38, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_40, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_42, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_44, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_46, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_48, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_50, context.getTheme()));
        colorList.add(context.getResources().getColor(R.color.wind_52, context.getTheme()));
        index = (int)(wind/2);
        if(index >= colorList.size())
            index = colorList.size() - 1;
        return colorList.get(index);
    }
}
