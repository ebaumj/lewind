package com.baumannsw.lewind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.baumannsw.lewind.windData.DataDownloader;
import com.baumannsw.lewind.windData.DataDownloaderCaller;
import com.baumannsw.lewind.windData.WindDataPoint;
import com.baumannsw.lewind.windData.WindDirection;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HistoryActivity extends AppCompatActivity implements DataDownloaderCaller {

    public static final String EXTRA_STATION_NAME = "com.baumannsw.lewind.STATION_NAME";
    public static final String EXTRA_STATION_ID = "com.baumannsw.lewind.STATION_ID";

    private final String TAG = "HISTORY_ACTIVITY";

    private int timeValue;
    private ArrayList<WindDataPoint> historyData;

    private int id;
    private String name;
    private ActionBar actionBar;
    private AlertDialog waitDialog;
    private Spinner dropdownTimeSelect;
    private LineChart chart, chartTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        id = getIntent().getIntExtra(EXTRA_STATION_ID, 0);
        name = getIntent().getStringExtra(EXTRA_STATION_NAME);
        historyData = new ArrayList<>();

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(name);

        // UI
        dropdownTimeSelect = findViewById(R.id.dropdownTimeSelect);
        chart = findViewById(R.id.chartHistory);
        chartTemp = findViewById(R.id.chartTemperature);

        // Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(getLayoutInflater().inflate(R.layout.dialog_wait, null));
        builder.setCancelable(false);
        waitDialog = builder.create();

        // Actions
        dropdownTimeSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeValue = getResources().getIntArray(R.array.times_array_values)[position];
                loadChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Get Data
        if(id > 0) {
            waitDialog.show();
            new DataDownloader(this, id, getResources().getInteger(R.integer.timeout_http_connection_ms)).execute();
        }
        else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_id), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadChart() {
        if(historyData.size() == 0)
            return;

        ArrayList<Entry> windValues = new ArrayList<>();
        ArrayList<Entry> gustValues = new ArrayList<>();
        ArrayList<Entry> tempValues = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -timeValue);
        Date lastDate = cal.getTime();

        double maximum = 0;
        double tempMax = 0;
        double tempMin = 0;

        boolean showTemperature = false;

        for(WindDataPoint dataPoint : historyData) {
            if(dataPoint.getDate().after(lastDate)) {
                long difference = dataPoint.getDate().getTime() - lastDate.getTime();
                float milliseconds = (float) difference;
                windValues.add(new Entry(milliseconds, (float)dataPoint.getAverage()));
                gustValues.add(new Entry(milliseconds, (float)dataPoint.getGust()));
                //tempValues.add(new Entry(milliseconds, (float)dataPoint.getTemperature()));
                tempValues.add(new Entry(milliseconds, (float)dataPoint.getDirectionInt()));
                if(dataPoint.getAverage() > maximum)
                    maximum = dataPoint.getAverage();
                if(dataPoint.getGust() > maximum)
                    maximum = dataPoint.getGust();
                /*if(dataPoint.getTemperature() > tempMax)
                    tempMax = dataPoint.getTemperature();
                if(dataPoint.getTemperature() < tempMin)
                    tempMin = dataPoint.getTemperature();
                if(dataPoint.getTemperature() != 0)
                    showTemperature = true;*/
            }
        }

        tempMax = 360;
        showTemperature = true;

        LineDataSet windDataSet = new LineDataSet(windValues, getResources().getString(R.string.label_wind));
        LineDataSet gustsDataSet = new LineDataSet(gustValues, getResources().getString(R.string.label_gust));
        LineDataSet tempDataSet = new LineDataSet(tempValues, getResources().getString(R.string.label_direction));

        windDataSet.setColor(getResources().getColor(R.color.rose_dark, getTheme()));
        windDataSet.setDrawIcons(false);
        windDataSet.setDrawCircles(false);
        windDataSet.setDrawValues(false);
        windDataSet.setDrawFilled(false);
        windDataSet.setDrawHorizontalHighlightIndicator(false);
        windDataSet.setHighLightColor(getResources().getColor(R.color.rose_dark_red, getTheme()));
        windDataSet.setHighlightLineWidth((float)1.2);
        windDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        gustsDataSet.setColor(getResources().getColor(R.color.rose_red, getTheme()));
        gustsDataSet.setDrawIcons(false);
        gustsDataSet.setDrawCircles(false);
        gustsDataSet.setDrawValues(false);
        gustsDataSet.setDrawFilled(false);
        gustsDataSet.setDrawHorizontalHighlightIndicator(false);
        gustsDataSet.setHighLightColor(getResources().getColor(R.color.rose_dark_red, getTheme()));
        gustsDataSet.setHighlightLineWidth((float)1.2);
        gustsDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        tempDataSet.setColor(getResources().getColor(R.color.rose_dark, getTheme()));
        tempDataSet.setDrawIcons(false);
        tempDataSet.setDrawCircles(false);
        tempDataSet.setDrawValues(false);
        tempDataSet.setDrawFilled(false);
        tempDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(windDataSet);
        dataSets.add(gustsDataSet);

        ArrayList<ILineDataSet> dataSets2 = new ArrayList<>();
        dataSets2.add(tempDataSet);

        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter() {
            private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm", Locale.getDefault());

            @Override
            public String getFormattedValue(float value) {
                cal.setTime(lastDate);
                cal.add(Calendar.MILLISECOND, (int) value);
                sdf.setTimeZone(TimeZone.getDefault());
                return sdf.format(cal.getTime());
            }
        });
        chart.getLegend().setTypeface(getResources().getFont(R.font.andika_new_basic));
        chart.getXAxis().setTypeface(getResources().getFont(R.font.andika_new_basic));
        chart.getAxisLeft().setTypeface(getResources().getFont(R.font.andika_new_basic));
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisLeft().setAxisMaximum((float) (maximum * 1.5));
        chartTemp.getAxisLeft().setMinWidth(20);
        chartTemp.getAxisLeft().setMaxWidth(20);


        chart.getXAxis().setLabelCount(4);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setScaleEnabled(false);
        //chart.setDragEnabled(true);
        chart.setHighlightPerDragEnabled(true);
        chart.animateX(500);

        chartTemp.getXAxis().setValueFormatter(new IndexAxisValueFormatter() {
            private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm", Locale.getDefault());

            @Override
            public String getFormattedValue(float value) {
                cal.setTime(lastDate);
                cal.add(Calendar.MILLISECOND, (int) value);
                sdf.setTimeZone(TimeZone.getDefault());
                return sdf.format(cal.getTime());
            }
        });
        chartTemp.getAxisLeft().setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return WindDirection.getWindDirectionString((int) value);
            }
        });
        chartTemp.getAxisLeft().setLabelCount(5, true);
        chartTemp.getLegend().setTypeface(getResources().getFont(R.font.andika_new_basic));
        chartTemp.getXAxis().setTypeface(getResources().getFont(R.font.andika_new_basic));
        chartTemp.getAxisLeft().setTypeface(getResources().getFont(R.font.andika_new_basic));
        chartTemp.getAxisLeft().setAxisMinimum(0);
        chartTemp.getAxisLeft().setAxisMaximum(360);
        chartTemp.getAxisLeft().setMinWidth(20);
        chartTemp.getAxisLeft().setMaxWidth(20);

        chartTemp.getXAxis().setLabelCount(4);
        chartTemp.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chartTemp.getAxisRight().setEnabled(false);
        chartTemp.getDescription().setEnabled(false);
        chartTemp.setScaleEnabled(false);
        chartTemp.setDragEnabled(false);
        chartTemp.setTouchEnabled(false);
        chartTemp.animateX(500);

        chart.setData(new LineData(dataSets));
        ChartMarker marker = new ChartMarker(getApplicationContext(), R.layout.marker_view, lastDate, windDataSet, gustsDataSet, tempDataSet);
        marker.setChartView(chart);
        chart.setMarker(marker);

        if(showTemperature)
            chartTemp.setData(new LineData(dataSets2));
        else
            chartTemp.setVisibility(LineChart.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(StationActivity.EXTRA_STATION_NAME, name);
        intent.putExtra(StationActivity.EXTRA_STATION_ID, id);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    public void onDownloadCompleted(ArrayList<WindDataPoint> data) {
        historyData = data;
        loadChart();
        waitDialog.cancel();
    }

    @Override
    public void onDownloadFailed(String errorMessage) {
        runOnUiThread(() -> {
            waitDialog.cancel();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.station_download_failed), Toast.LENGTH_SHORT).show();
        });
        finish();
    }
}