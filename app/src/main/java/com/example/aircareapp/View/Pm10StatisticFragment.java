package com.example.aircareapp.View;

import static com.example.aircareapp.SSLHandle.SSLHandle.handleSSLHandshake;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.example.aircareapp.R;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
public class Pm10StatisticFragment extends Fragment {

    RequestQueue mRequestQueue;
    XYPlot plot;
    SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.aircareapp/databases/AqiAsset.db", null, SQLiteDatabase.OPEN_READWRITE);
    ArrayList<Double> pm10 = new ArrayList<>();
    ArrayList<Double> pm25 = new ArrayList<>();
    ArrayList<String> times = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pm10_statistic, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        plot = (XYPlot) view.findViewById(R.id.plot4);
        handleSSLHandshake();
        mRequestQueue = Volley.newRequestQueue(getActivity());
        Cursor cursor = db.rawQuery("SELECT * FROM AqiAsset", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            @SuppressLint("Range") String dateString = cursor.getString(cursor.getColumnIndex("time"));
            // Chuyển đổi ngày từ chuỗi sang đối tượng Date
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-dd-MM");
            Date date = null;
            try {
                date = inputFormat.parse(dateString);
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM");
                String formattedDate =  outputFormat.format(date);
                pm10.add(cursor.getDouble(0));
                pm25.add(cursor.getDouble(1));
                times.add(formattedDate);
                cursor.moveToNext();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        cursor.close();

        XYSeries series1 = new SimpleXYSeries(
                pm10, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "PM10 (µg/m³)");

        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(getActivity(), R.xml.line_point_formatter_with_labels);

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