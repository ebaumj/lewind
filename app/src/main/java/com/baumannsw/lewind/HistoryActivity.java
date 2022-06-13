package com.baumannsw.lewind;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.baumannsw.lewind.windData.DataDownloader;
import com.baumannsw.lewind.windData.DataDownloaderCaller;
import com.baumannsw.lewind.windData.WindDataPoint;
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
    private final RectF valueSelected = new RectF();

    private int timeValue;
    private ArrayList<WindDataPoint> historyData;

    private int id;
    private String name;
    private ActionBar actionBar;
    private AlertDialog waitDialog;
    private Spinner dropdownTimeSelect;
    private LineChart chart;

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
        waitDialog.show();
        new DataDownloader(this, id, getResources().getInteger(R.integer.timeout_http_connection_ms)).execute();
    }

    private void loadChart() {
        if(historyData.size() == 0)
            return;

        chart = findViewById(R.id.chartHistory);

        ArrayList<Entry> windValues = new ArrayList<Entry>();
        ArrayList<Entry> gustValues = new ArrayList<Entry>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -timeValue);
        Date lastDate = cal.getTime();

        int i = 0;

        for(WindDataPoint dataPoint : historyData) {
            if(dataPoint.getDate().after(lastDate)) {
                long difference = dataPoint.getDate().getTime() - lastDate.getTime();
                float milliseconds = (float) difference;
                windValues.add(new Entry(milliseconds, (float)dataPoint.getAverage()));
                gustValues.add(new Entry(milliseconds, (float)dataPoint.getGust()));
            }
            i++;
        }

        LineDataSet windDataSet = new LineDataSet(windValues, "Wind");
        LineDataSet gustsDataSet = new LineDataSet(gustValues, "Gusts");

        windDataSet.setColor(getResources().getColor(R.color.rose_dark, getTheme()));
        windDataSet.setDrawIcons(false);
        windDataSet.setDrawCircles(false);
        windDataSet.setDrawValues(false);
        windDataSet.setDrawFilled(false);
        windDataSet.setDrawHorizontalHighlightIndicator(false);
        windDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        gustsDataSet.setColor(getResources().getColor(R.color.rose_red, getTheme()));
        gustsDataSet.setDrawIcons(false);
        gustsDataSet.setDrawCircles(false);
        gustsDataSet.setDrawValues(false);
        gustsDataSet.setDrawFilled(false);
        gustsDataSet.setDrawHorizontalHighlightIndicator(false);
        gustsDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(windDataSet);
        dataSets.add(gustsDataSet);

        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter() {
            private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm", Locale.GERMAN);

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


        chart.getXAxis().setLabelCount(4);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);

        /*chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;

                RectF bounds = valueSelected;
                chart.getClipBounds();
                //chart.getBarBounds((BarEntry) e, bounds);
                MPPointF position = chart.getPosition(e, YAxis.AxisDependency.LEFT);

                MPPointF.recycleInstance(position);
            }

            @Override
            public void onNothingSelected() {

            }
        });*/
        chart.setData(new LineData(dataSets));
        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();

        ChartMarker marker = new ChartMarker(getApplicationContext(), R.layout.marker_view, lastDate, windDataSet, gustsDataSet);
        marker.setChartView(chart);
        chart.setMarker(marker);
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
        // TODO: finish Activity or place Reload Button
        finish();
    }
}