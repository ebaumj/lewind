package com.baumannsw.lewind;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ChartMarker extends MarkerView {

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MMMM.YYYY HH:mm", Locale.GERMAN);

    private TextView tvDate, tvWind, tvGust;
    private Date lastDate;
    private LineDataSet windData, gustData;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public ChartMarker(Context context, int layoutResource, Date lastDate, LineDataSet windData, LineDataSet gustData) {
        super(context, layoutResource);
        this.lastDate = lastDate;
        this.windData = windData;
        this.gustData = gustData;
        tvDate = findViewById(R.id.tvMarkerDate);
        tvWind = findViewById(R.id.tvMarkerWind);
        tvGust = findViewById(R.id.tvMarkerGust);
    }

    private MPPointF mOffset;

    /**
     * @param posX This is the X position at which the marker wants to be drawn.
     *             You can adjust the offset conditionally based on this argument.
     * @param posY This is the X position at which the marker wants to be drawn.
     * @return The offset for drawing at the specific `point`. This allows conditional adjusting of the Marker position.
     * If you have no adjustments to make, return getOffset().
     */
    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
        if(mOffset == null) {
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }
        return mOffset;
    }

    /**
     * This method enables a specified custom IMarker to update it's content every time the IMarker is redrawn.
     *
     * @param e         The Entry the IMarker belongs to. This can also be any subclass of Entry, like BarEntry or
     *                  CandleEntry, simply cast it at runtime.
     * @param highlight The highlight object contains information about the highlighted value such as it's dataset-index, the
     */
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int index = getChartView().getData().getDataSetForEntry(e).getEntryIndex(e);
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastDate);
        cal.add(Calendar.MILLISECOND, (int) e.getX());
        sdf.setTimeZone(TimeZone.getDefault());
        tvDate.setText(sdf.format(cal.getTime()));
        tvWind.setText("Wind: " + getChartView().getData().getDataSetByIndex(0).getEntryForIndex(index).getY());
        tvGust.setText("Gust: " + getChartView().getData().getDataSetByIndex(1).getEntryForIndex(index).getY());
        tvDate.setTypeface(getResources().getFont(R.font.andika_new_basic));
        tvWind.setTypeface(getResources().getFont(R.font.andika_new_basic));
        tvGust.setTypeface(getResources().getFont(R.font.andika_new_basic));
        super.refreshContent(e, highlight);
    }
}
