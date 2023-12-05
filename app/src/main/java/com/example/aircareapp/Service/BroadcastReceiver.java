package com.example.aircareapp.Service;

import static com.example.aircareapp.SSLHandle.SSLHandle.handleSSLHandshake;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aircareapp.AccessAPI.AccessAPI;
import com.example.aircareapp.SQLite.AQIAsset;
import com.example.aircareapp.SQLite.WeatherAsset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BroadcastReceiver extends android.content.BroadcastReceiver {

    JsonArrayRequest jsonArrayRequest;
    JSONArray assetBounds;
    int Count = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            if (isNetworkAvailable(context)) {
                //Create Database
                WeatherAsset weatherAsset = new WeatherAsset(context);
//                weatherAsset.QueryData("CREATE TABLE IF NOT EXISTS WeatherAsset(temperature char, humidity integer, wind char, time char)");
                handleSSLHandshake();
                RequestQueue aRequestQueue = Volley.newRequestQueue(context);
                StringRequest stringRequestWeather = new StringRequest(Request.Method.GET, AccessAPI.getUrlGoogleWeather(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("MyResponseWeather", "" + response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                                    JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                                    JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");

                                    //get and set temp
                                    String temp = jsonObjectMain.getString("temp");
                                    Log.e("MyRespond Temp", temp);

                                    //get and set humidity
                                    String humidity = jsonObjectMain.getString("humidity");
                                    Log.e("MyRespond humidity", humidity);

                                    //get and set cloud
                                    String cloud = jsonObjectCloud.getString("all");
                                    Log.e("MyRespond Cloud", cloud);

                                    //get and set windy
                                    String windy = jsonObjectWind.getString("speed");
                                    Log.e("MyRespond Windy", windy);

                                    String time = jsonObject.getString("dt");
                                    long l1 = Long.valueOf(time);
                                    Date date1 = new Date(l1 * 1000L);
                                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-dd-MM");
                                    String formatDays = simpleDateFormat1.format(date1);
                                    Log.e("Days", formatDays);

//                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('26', '83', '1.66','2023-27-11')");
//                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('33', '66', '2.22','2023-28-11')");
//                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('33', '85', '1.82','2023-29-11')");
//                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('32', '87', '2.5','2023-30-11')");
//                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('30', '72', '2.22','2023-01-12')");
//                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('26', '91', '1.38', '2023-02-12')");
//                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('31', '94', '1.66', '2023-03-12')");
//                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('33', '76', '1.94','2023-04-12')");
                                    if (!isTimeWeatherExist(weatherAsset, "WeatherAsset", "time", formatDays)) {
                                        // Nếu không tồn tại, thêm mới
                                        weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('" + temp + "','" + humidity + "','" + windy + "','" + formatDays + "')");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("MyErrorDataBroadcast", "" + error);
                            }
                        }) {
                };

                AQIAsset aqiAsset = new AQIAsset(context);
                StringRequest stringRequestAqi = new StringRequest(Request.Method.GET, AccessAPI.getUrlAirPollution(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("MyRespondAqi Success", response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArrayList = jsonObject.getJSONArray("list");

                                    Double pm10, pm25;
                                    String time;

                                    for (int j = 0; j < jsonArrayList.length(); j++) {
                                        JSONObject jsonObject1 = jsonArrayList.getJSONObject(j);
                                        JSONObject jsonObjectComponents = jsonObject1.getJSONObject("components");
                                        pm10 = jsonObjectComponents.getDouble("pm10");
                                        pm25 = jsonObjectComponents.getDouble("pm2_5");

                                        time = jsonObject1.getString("dt");
                                        long l1 = Long.valueOf(time);
                                        Date date1 = new Date(l1 * 1000L);
                                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-dd-MM");
                                        String formatDays = simpleDateFormat1.format(date1);
                                        Log.e("DaysAqi", formatDays);
                                        Log.d("kq", "onResponse: " + pm10 + " - " + pm25 + " - " + formatDays + "\n");

//                                        aqiAsset.QueryData("INSERT INTO AqiAsset VALUES('17', '17.7','2023-27-11')");
//                                        aqiAsset.QueryData("INSERT INTO AqiAsset VALUES('28', '28.6','2023-28-11')");
//                                        aqiAsset.QueryData("INSERT INTO AqiAsset VALUES('32.4', '37.6','2023-29-11')");
//                                        aqiAsset.QueryData("INSERT INTO AqiAsset VALUES('36.1', '38.2','2023-30-11')");
//                                        aqiAsset.QueryData("INSERT INTO AqiAsset VALUES('30.2', '33', '2023-01-12')");
//                                        aqiAsset.QueryData("INSERT INTO AqiAsset VALUES('19.6', '21.2', '2023-02-12')");
//                                        aqiAsset.QueryData("INSERT INTO AqiAsset VALUES('12', '13.4','2023-03-12')");
//                                        aqiAsset.QueryData("INSERT INTO AqiAsset VALUES('18', '20.5','2023-04-12')");
                                        if (!isTimeAqiExist(aqiAsset, "AqiAsset", "time", formatDays)) {
                                            // Nếu không tồn tại, thêm mới
                                            aqiAsset.QueryData("INSERT INTO AqiAsset VALUES(" + pm10 + "," + pm25 + ",'" + formatDays + "')");
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("MyRespond2 Failed", "fail");
                            }
                        });
                aRequestQueue.add(stringRequestWeather);
                aRequestQueue.add(stringRequestAqi);
                SQLiteDatabase databaseWeather = weatherAsset.getReadableDatabase();
                SQLiteDatabase databaseAqi = aqiAsset.getReadableDatabase();
                databaseWeather.close();
                databaseAqi.close();

            } else {
                //Toast.makeText(context, "No Connect Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isTimeWeatherExist(WeatherAsset weatherAsset, String weathertb, String time, String value) {
        SQLiteDatabase db = weatherAsset.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + weathertb + " WHERE " + time + "=?", new String[]{value});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    private boolean isTimeAqiExist(AQIAsset aqiAsset, String aqitb, String time, String value) {
        SQLiteDatabase db = aqiAsset.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + aqitb + " WHERE " + time + "=?", new String[]{value});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }


    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return false;
            }
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }

    }
}
