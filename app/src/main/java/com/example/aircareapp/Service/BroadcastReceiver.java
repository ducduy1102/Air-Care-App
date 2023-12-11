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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.HashMap;
import java.util.Map;

public class BroadcastReceiver extends android.content.BroadcastReceiver {

    int Count = 0;

    private JSONObject jsonWeatherDes, jsonObjectWeatherData, jsonObjectValue, jsonObjectMain, jsonObjectWind, jsonObjectSys, jsonObjectCloud,
            jsonObjectAttributes, jsonObjectRequestQueryParameters, jsonObjectPM25, jsonObjectPM10, jsonObjectCO2, jsonObjectHumidity, jsonObjectAQI, jsonObjectAQIPredict, jsonObjectAssetUser, jsonObjectId;

    private Date sunriseDate, sunsetDate, dateformat;

    private String date, date2, createdOn, formatDays, formatDays2;

    private double pm25, pm10;
    private String temp, humidity, wind;
    private long dateTimestamp;
    private JsonObjectRequest jsonObjectAssetRequest;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            if (isNetworkAvailable(context)) {
                //Create Database
                WeatherAsset weatherAsset = new WeatherAsset(context);
                handleSSLHandshake();
                RequestQueue aRequestQueue = Volley.newRequestQueue(context);

                jsonObjectAssetRequest = new JsonObjectRequest(Request.Method.GET, AccessAPI.getUrlAsset1(), null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("MyResponsejsonObject1", "" + response);
                                try {
                                    createdOn = response.getString("createdOn");
                                    jsonObjectAttributes = response.getJSONObject("attributes");
                                    jsonObjectRequestQueryParameters = jsonObjectAttributes.getJSONObject("requestQueryParameters");
                                    jsonObjectValue = jsonObjectRequestQueryParameters.getJSONObject("value");

                                    jsonObjectWeatherData = jsonObjectAttributes.getJSONObject("data");
                                    jsonObjectValue = jsonObjectWeatherData.getJSONObject("value");
                                    jsonObjectMain = jsonObjectValue.getJSONObject("main");
                                    jsonObjectWind = jsonObjectValue.getJSONObject("wind");
                                    jsonObjectCloud = jsonObjectValue.getJSONObject("clouds");
                                    jsonObjectSys = jsonObjectValue.getJSONObject("sys");

                                    temp = jsonObjectMain.getString("temp");
                                    humidity = jsonObjectMain.getString("humidity");
                                    wind = jsonObjectWind.getString("speed");
                                    dateTimestamp = jsonObjectSys.getLong("sunrise");

                                    // Convert timestamps to Date objects & seconds to milliseconds
                                    dateformat = new Date(dateTimestamp * 1000L);
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM");
                                    date = sdf.format(dateformat);
                                    date2 = date;
                                    Log.d("dateInsert", "onResponse: " + date + " " + date2);

                                    if (!isTimeWeatherExist(weatherAsset, "WeatherAsset", "time", date)) {
                                        // Nếu không tồn tại, thêm mới
                                        weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('" + temp + "','" + humidity + "','" + wind + "','" + date + "')");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("MyErrorAsset1", "" + error);
                            }
                        }) {
                    @Override
                    public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("accept", "application/json;");
                        params.put("Authorization", "Bearer " + AccessAPI.getToken());
                        return params;
                    }
                };
                aRequestQueue.add(jsonObjectAssetRequest);
                SQLiteDatabase databaseWeather = weatherAsset.getReadableDatabase();
                databaseWeather.close();


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

                aRequestQueue.add(stringRequestAqi);
                SQLiteDatabase databaseAqi = aqiAsset.getReadableDatabase();
                databaseAqi.close();

            } else {
                Toast.makeText(context, "No Connect Internet", Toast.LENGTH_SHORT).show();
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
