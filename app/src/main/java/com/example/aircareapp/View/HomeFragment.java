package com.example.aircareapp.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.aircareapp.APICLient.APIClient;
import com.example.aircareapp.APIService.APIService;
import com.example.aircareapp.AccessAPI.AccessAPI;
import com.example.aircareapp.Model.User;
import com.example.aircareapp.R;
import com.example.aircareapp.SQLite.WeatherAsset;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment {

    private View view;
    private ImageView imgAvatar, imgCloud;
    private TextView tvWeatherDes, tvHumidity, tvDate, tvWind, tvTemp, tvPressure, tvSunset, tvSunrise, tvPm10, tvPm25, tvCo2, tvUsername;
    private JsonObjectRequest jsonObjectAssetRequest, jsonObjectAsset3Request;
    private JSONArray jsonArrayWeather;
    private JSONObject jsonWeatherDes, jsonObjectWeatherData, jsonObjectValue, jsonObjectMain, jsonObjectWind, jsonObjectSys, jsonObjectCloud,
            jsonObjectAttributes, jsonObjectRequestQueryParameters, jsonObjectAttributes3, jsonObjectPM25, jsonObjectPM10, jsonObjectCO2;

    private Date sunriseDate, sunsetDate, dateformat;
    private RequestQueue asset1RequestQueue, asset3RequestQueue;
    private String sunrise, sunset, date, createdOn;

    private double pm25, pm10, C02, temp, speed, all;
    private int humidity, pressure;
    private long sunriseTimestamp, sunsetTimestamp, dateTimestamp;
    private APIService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        initUi();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GetInfoUser();
        GetCurrentWeatherData();
        showUserAvatar();
    }

    private void initUi() {
        imgAvatar = view.findViewById(R.id.imgAvatar);
        imgCloud = view.findViewById(R.id.imgCloud);

        tvWeatherDes = view.findViewById(R.id.tvWeatherDes);
        tvTemp = view.findViewById(R.id.tvTemp);
        tvHumidity = view.findViewById(R.id.tvHumidity);
        tvWind = view.findViewById(R.id.tvWind);
        tvSunrise = view.findViewById(R.id.tvSunrise);
        tvSunset = view.findViewById(R.id.tvSunset);
        tvPm10 = view.findViewById(R.id.tvPm10);
        tvPm25 = view.findViewById(R.id.tvPm25);
        tvCo2 = view.findViewById(R.id.tvC02);
        tvPressure = view.findViewById(R.id.tvPressure);
        tvDate = view.findViewById(R.id.tvDate);
        tvUsername = view.findViewById(R.id.tvUsername);
    }

    public void  GetInfoUser(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dataLogin", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        tvUsername.setText(requireContext().getString(R.string.Hello) + " " + sharedPreferences.getString("prefUsername", null));
        String username = sharedPreferences.getString("prefUsername", null);
        if(username == null){
            apiService = APIClient.getClient("https://uiot.ixxc.dev/api/master/user/", token).create(APIService.class);
            // Make the API call
            Call<User> call = apiService.getUser();
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                    if (response.isSuccessful()) {
                        User data = response.body();
                        if(username == null) {
                            tvUsername.setText(requireContext().getString(R.string.Hello) + " " + data.getUsername());
                        }
                    } else {
                        Toast.makeText(getContext(), "Error" + response.errorBody(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
        Log.d("tokenProfile", token);
    }

    public void GetCurrentWeatherData () {
        // Call API asset 1
        asset1RequestQueue = Volley.newRequestQueue(getActivity());
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

                            jsonArrayWeather = jsonObjectValue.getJSONArray("weather");
                            jsonWeatherDes = new JSONObject(String.valueOf(jsonArrayWeather.get(0)));

                            temp = jsonObjectMain.getDouble("temp");
                            humidity = jsonObjectMain.getInt("humidity");
                            pressure = jsonObjectMain.getInt("pressure");
                            speed = jsonObjectWind.getDouble("speed");
                            all = jsonObjectCloud.getDouble("all");
                            sunriseTimestamp = jsonObjectSys.getLong("sunrise");
                            sunsetTimestamp = jsonObjectSys.getLong("sunset");
                            dateTimestamp = jsonObjectSys.getLong("sunrise");

                            // Convert timestamps to Date objects & seconds to milliseconds
                            sunriseDate = new Date(sunriseTimestamp * 1000L);
                            sunsetDate = new Date(sunsetTimestamp * 1000L);
                            dateformat = new Date(dateTimestamp * 1000L);

                            // Format the dates as strings with HH:mm format
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                            SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE", Locale.getDefault());

                            sunrise = sdf.format(sunriseDate);
                            sunset = sdf.format(sunsetDate);
                            date = sdf2.format(dateformat);

                            int tempInt = (int) Math.round(temp);

                            tvSunrise.setText(sunrise);
                            tvSunset.setText(sunset);
                            tvDate.setText(getDay(date));
                            Log.d("timestampsun", "onResponse: " + dateformat);

                            tvPressure.setText(pressure+"hPa");
                            tvHumidity.setText(humidity + "%");
                            tvTemp.setText(tempInt + "°C");

                            String strWind = jsonObjectWind.getString("speed");
                            tvWind.setText(strWind + "m/s");

                            String description = jsonWeatherDes.getString("description");
                            tvWeatherDes.setText(descriptionWeather(description));

//                            String icon = jsonWeatherDes.getString("icon");
//                            Glide.with(getActivity()).load("http://openweathermap.org/img/wn/" + icon + ".png").error(R.drawable.ic_cloud_v2).into(imgCloud);

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

        // Call API asset 3
        asset3RequestQueue = Volley.newRequestQueue(getActivity());
        jsonObjectAsset3Request = new JsonObjectRequest(Request.Method.GET, AccessAPI.getUrlAsset3(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("MyResponsejsonObject3", "" + response);

                        try {
                            jsonObjectAttributes3 = response.getJSONObject("attributes");
                            jsonObjectPM25 = jsonObjectAttributes3.getJSONObject("PM25");
                            jsonObjectPM10 = jsonObjectAttributes3.getJSONObject("PM10");
                            jsonObjectCO2 = jsonObjectAttributes3.getJSONObject("CO2");

                            pm10 = jsonObjectPM10.getDouble("value");
                            pm25 = jsonObjectPM25.getDouble("value");
                            C02 = jsonObjectCO2.getDouble("value");

                            tvPm10.setText(pm10 + "µg/m³");
                            tvPm25.setText(pm25 + "µg/m³");
                            tvCo2.setText(C02 + "µg/m³");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MyErrorUserCurrent", "" + error);
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

        asset1RequestQueue.add(jsonObjectAssetRequest);
        asset3RequestQueue.add(jsonObjectAsset3Request);

    }

    public String getDay(String day){
        switch (day) {
            case "Monday":
                day = getResources().getString(R.string.monday);
                break;
            case "Tuesday":
                day = getResources().getString(R.string.tuesday);
                break;
            case "Wednesday":
                day = getResources().getString(R.string.wednesday);
                break;
            case "Thursday":
                day = getResources().getString(R.string.thursday);
                break;
            case "Friday":
                day = getResources().getString(R.string.friday);
                break;
            case "Saturday":
                day = getResources().getString(R.string.saturday);
                break;
            case "Sunday":
                day = getResources().getString(R.string.sunday);
                break;
            default:
                day = getResources().getString(R.string.day);
                break;
        }
        return day;
    }
    public String descriptionWeather(String description){
        switch (description) {
            case "clear sky":
                description = getResources().getString(R.string.clearSky);
                break;
            case "few clouds":
                description = getResources().getString(R.string.fewClouds);
                break;
            case "scattered clouds":
                description = getResources().getString(R.string.scatteredClouds);
                break;
            case "broken clouds":
                description = getResources().getString(R.string.brokenClouds);
                break;
            case "shower rain":
                description = getResources().getString(R.string.showerRain);
                break;
            case "thunderstorm":
                description = getResources().getString(R.string.thunderstorm);
                break;
            case "snow":
                description = getResources().getString(R.string.snow);
                break;
            case "mist":
                description = getResources().getString(R.string.mist);
                break;
            default:
                description = getResources().getString(R.string.scriptWeather);
                break;
        }
        return description;
    }

    private void showUserAvatar() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }
        Uri photoUrl = user.getPhotoUrl();
        Glide.with(this).load(photoUrl).error(R.drawable.avatar_default).into(imgAvatar);
    }
}