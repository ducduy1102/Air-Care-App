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
            jsonObjectAttributes, jsonObjectRequestQueryParameters, jsonObjectAttributes3, jsonObjectPM25, jsonObjectPM10, jsonObjectCO2, jsonObjectHumidity, jsonObjectAQI, jsonObjectAQIPredict, jsonObjectAssetUser, jsonObjectId;

    private Date sunriseDate, sunsetDate, dateformat;

    private String date, date2, createdOn, formatDays, formatDays2;

    private double pm25, pm10;
    private String temp, humidity, wind;
    private long dateTimestamp;
    private JsonObjectRequest jsonObjectAssetRequest, jsonObjectAsset3Request;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            if (isNetworkAvailable(context)) {
                //Create Database
                WeatherAsset weatherAsset = new WeatherAsset(context);
                handleSSLHandshake();
                RequestQueue aRequestQueue = Volley.newRequestQueue(context);
//                StringRequest stringRequestWeather = new StringRequest(Request.Method.GET, AccessAPI.getUrlGoogleWeather(),
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Log.e("MyResponseWeather", "" + response);
//
//                                try {
//                                    JSONObject jsonObject = new JSONObject(response);
//                                    JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
//                                    JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
//                                    JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
//
//                                    //get and set temp
//                                    String temp = jsonObjectMain.getString("temp");
//                                    Log.e("MyRespond Temp", temp);
//
//                                    //get and set humidity
//                                    String humidity = jsonObjectMain.getString("humidity");
//                                    Log.e("MyRespond humidity", humidity);
//
//                                    //get and set cloud
//                                    String cloud = jsonObjectCloud.getString("all");
//                                    Log.e("MyRespond Cloud", cloud);
//
//                                    //get and set windy
//                                    String windy = jsonObjectWind.getString("speed");
//                                    Log.e("MyRespond Windy", windy);
//
//                                    String time = jsonObject.getString("dt");
//                                    long l1 = Long.valueOf(time);
//                                    Date date1 = new Date(l1 * 1000L);
//                                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-dd-MM");
//                                    String formatDays = simpleDateFormat1.format(date1);
//                                    Log.e("Days", formatDays);
//
////                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('26', '83', '1.66','2023-27-11')");
////                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('33', '66', '2.22','2023-28-11')");
////                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('33', '85', '1.82','2023-29-11')");
////                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('32', '87', '2.5','2023-30-11')");
////                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('30', '72', '2.22','2023-01-12')");
////                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('26', '91', '1.38', '2023-02-12')");
////                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('31', '94', '1.66', '2023-03-12')");
////                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('33', '76', '1.94','2023-04-12')");
//                                    if (!isTimeWeatherExist(weatherAsset, "WeatherAsset", "time", formatDays)) {
//                                        // Nếu không tồn tại, thêm mới
//                                        weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('" + temp + "','" + humidity + "','" + windy + "','" + formatDays + "')");
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.e("MyErrorDataBroadcast", "" + error);
//                            }
//                        }) {
//                };

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

//                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('26', '83', '1.66','2023-03-12')");
//                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('33', '66', '2.22','2023-04-12')");
//                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('33', '85', '1.82','2023-05-12')");
//                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('32', '87', '2.5','2023-06-12')");
//                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('30', '72', '2.22','2023-07-12')");
//                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('26', '91', '1.38', '2023-08-12')");
//                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('31', '94', '1.66', '2023-09-12')");
//                                    weatherAsset.QueryData("INSERT INTO WeatherAsset VALUES('33', '76', '1.94','2023-10-12')");


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

//                                        aqiAsset.QueryData("INSERT INTO AqiAsset VALUES('17', '17.7','2023-03-12')");
//                                        aqiAsset.QueryData("INSERT INTO AqiAsset VALUES('28', '28.6','2023-04-12')");
//                                        aqiAsset.QueryData("INSERT INTO AqiAsset VALUES('32.4', '37.6','2023-05-12')");
//                                        aqiAsset.QueryData("INSERT INTO AqiAsset VALUES('36.1', '38.2','2023-06-12')");
//                                        aqiAsset.QueryData("INSERT INTO AqiAsset VALUES('30.2', '33', '2023-07-12')");
//                                        aqiAsset.QueryData("INSERT INTO AqiAsset VALUES('19.6', '21.2', '2023-08-12')");
//                                        aqiAsset.QueryData("INSERT INTO AqiAsset VALUES('12', '13.4','2023-09-12')");
//                                        aqiAsset.QueryData("INSERT INTO AqiAsset VALUES('18', '20.5','2023-10-12')");
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

                // Call API asset 3
//                jsonObjectAsset3Request = new JsonObjectRequest(Request.Method.GET, AccessAPI.getUrlAsset3(), null,
//                        new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                Log.e("MyResponsejsonObject3", "" + response);
//
//                                try {
//                                    jsonObjectAttributes3 = response.getJSONObject("attributes");
//                                    jsonObjectPM25 = jsonObjectAttributes3.getJSONObject("PM25");
//                                    jsonObjectPM10 = jsonObjectAttributes3.getJSONObject("PM10");
//                                    jsonObjectAQI = jsonObjectAttributes3.getJSONObject("AQI");
//                                    dateTimestamp = jsonObjectPM25.getLong("timestamp");
//
//
//                                    pm10 = jsonObjectPM10.getDouble("value");
//                                    pm25 = jsonObjectPM25.getDouble("value");
//                                    Log.d("dateInsert2", "onResponse: " + date2);
//
//                                    if (!isTimeAqiExist(aqiAsset, "AqiAsset", "time", date2)) {
//                                        // Nếu không tồn tại, thêm mới
//                                        aqiAsset.QueryData("INSERT INTO AqiAsset VALUES(" + pm10 + "," + pm25 + ",'" + date2 + "')");
//                                    }
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.e("MyErrorUserCurrent", "" + error);
//                            }
//                        }) {
//                    @Override
//                    public java.util.Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("accept", "application/json;");
//                        params.put("Authorization", "Bearer " + AccessAPI.getToken());
//                        return params;
//                    }
//                };

//                aRequestQueue.add(stringRequestWeather);
                aRequestQueue.add(stringRequestAqi);
//                aRequestQueue.add(jsonObjectAsset3Request);
                SQLiteDatabase databaseAqi = aqiAsset.getReadableDatabase();
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
