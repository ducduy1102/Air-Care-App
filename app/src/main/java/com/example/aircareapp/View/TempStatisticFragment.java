package com.example.aircareapp.View;

import static com.example.aircareapp.SSLHandle.SSLHandle.handleSSLHandshake;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.example.aircareapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TempStatisticFragment extends Fragment {

    RequestQueue mRequestQueue;
    XYPlot plot;
    SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.aircareapp/databases/WeatherAsset.db", null, SQLiteDatabase.OPEN_READWRITE);
    ArrayList<Double> temperatures = new ArrayList<>();
    ArrayList<Double> humidities = new ArrayList<>();
    ArrayList<Double> winds = new ArrayList<>();
    ArrayList<String> times = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temp_statistic, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        plot = (XYPlot) view.findViewById(R.id.plot1);
        handleSSLHandshake();
        mRequestQueue = Volley.newRequestQueue(getActivity());

        Cursor cursor = db.rawQuery("SELECT * FROM WeatherAsset", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            @SuppressLint("Range") String dateString = cursor.getString(cursor.getColumnIndex("time"));
            // Chuyển đổi ngày từ chuỗi sang đối tượng Date
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-dd-MM");
            Date date = null;
            try {
                date = inputFormat.parse(dateString);
                // Định dạng lại đối tượng Date thành chuỗi trong định dạng 'dd-MM'
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM");
                String formattedDate =  outputFormat.format(date);
                temperatures.add(cursor.getDouble(0));
                humidities.add(cursor.getDouble(1));
                winds.add(cursor.getDouble(2));
                times.add(formattedDate);
                cursor.moveToNext();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        cursor.close();
        Log.d("timeWeather", "onViewCreated: " + times);

        XYSeries series1 = new SimpleXYSeries(
                temperatures, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, getResources().getString(R.string.titleTemp));

        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(getActivity(), R.xml.line_point_formatter_with_labels);
//        LineAndPointFormatter series1Format =
//                new LineAndPointFormatter(Color.RED, Color.GREEN, null, null);

        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));
        plot.addSeries(series1, series1Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(times.get(i));
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });

        PanZoom.attach(plot);
    }
}