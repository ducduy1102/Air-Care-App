package com.example.aircareapp.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aircareapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailPlaceFragment extends Fragment {
    private View view;

    private TextView tvTemp, tvHumidity, tvWind, tvPm10, tvPm25, tvCo, tvSo2, tvNh3, tvNo, tvNo2, tvO3, statusAqi;
    private ImageView imgBackHome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail_place, container, false);

        initUi();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListener();
        GetCurrentWeatherData();
        GetCurrentAQIData();

    }

    private void initListener() {
        imgBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                DetailPlaceFragment detailPlaceFragment = (DetailPlaceFragment) getFragmentManager().findFragmentByTag("fragDetailPlaceFragment");
                transaction.remove(detailPlaceFragment);
                transaction.replace(R.id.mainActivity, new HomeFragment());
                transaction.commit();
            }
        });
    }

    public void GetCurrentWeatherData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "https://api.openweathermap.org/data/2.5/weather?q=Saigon&units=metric&appid=ac212f5768bf3e2f84201adbd2bc7961";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("MyRespond", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");

                            //get and set temp
                            String strTemp = jsonObjectMain.getString("temp");
                            Double tempDouble = Double.valueOf(strTemp);
                            String temp = String.valueOf(tempDouble.intValue());
                            Log.e("MyRespond Temp", temp);
                            tvTemp.setText(temp + "°C");

                            //get and set humidity
                            String humidity = jsonObjectMain.getString("humidity");
                            Log.e("MyRespond humidity", humidity);
                            tvHumidity.setText(humidity + "%");


                            //get and set wind
                            String strWind = jsonObjectWind.getString("speed");
                            Double windDouble = Double.valueOf(strWind);
                            String wind = String.valueOf(windDouble.intValue());
                            Log.e("MyRespond Windy", wind);
                            tvWind.setText(wind + "m/s");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MyRespond", "fail");
                    }
                });
        requestQueue.add(stringRequest);

    }

    public void GetCurrentAQIData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "https://api.openweathermap.org/data/2.5/air_pollution?lat=10.8333&lon=106.6667&appid=ac212f5768bf3e2f84201adbd2bc7961";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("MyRespond22 Success", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");

                            Double pm10, pm25, co, no, so2, no2, nh3, o3;

                            for (int j = 0; j < jsonArrayList.length(); j++) {
                                JSONObject jsonObject1 = jsonArrayList.getJSONObject(j);
                                JSONObject jsonObjectComponents = jsonObject1.getJSONObject("components");
                                pm10 = jsonObjectComponents.getDouble("pm10");
                                pm25 = jsonObjectComponents.getDouble("pm2_5");
                                co = jsonObjectComponents.getDouble("co");
                                no = jsonObjectComponents.getDouble("no");
                                no2 = jsonObjectComponents.getDouble("no2");
                                so2 = jsonObjectComponents.getDouble("so2");
                                nh3 = jsonObjectComponents.getDouble("nh3");
                                o3 = jsonObjectComponents.getDouble("o3");
//                                    Toast.makeText(getActivity(), pm10 + " - " + pm25 + " - " + co + " - " + co2 + " - " + no2 + " - " + so2 + " - " + nh3 + " - " + o3 + "\n", Toast.LENGTH_SHORT).show();
                                tvPm10.setText(pm10 + " µg/m³");
                                tvPm25.setText(pm25 + " µg/m³");
                                tvCo.setText(co + " µg/m³");
                                tvNo.setText(no + " µg/m³");
                                tvNo2.setText(no2+ " µg/m³");
                                tvSo2.setText(so2+ " µg/m³");
                                tvO3.setText(o3+ " µg/m³");
                                tvNh3.setText(nh3+ " µg/m³");
                                Log.d("kq", "onResponse: " + pm10 + " - " + pm25 + " - " + co + " - " + no + " - " + no2 + " - " + so2 + " - " + nh3 + " - " + o3 + "\n");
                            }

                            int status;
                            JSONObject jsonObject1 = jsonArrayList.getJSONObject(0);
                            JSONObject jsonObjectMain = jsonObject1.getJSONObject("main");
                            status = jsonObjectMain.getInt("aqi");
                            if(status == 1){
                                statusAqi.setText("Good");
                            } else if(status == 2){
                                statusAqi.setText("Fair");
                            } else if(status == 3){
                                statusAqi.setText("Moderate");
                            }else if(status == 4){
                                statusAqi.setText("Poor");
                            }else if(status == 5){
                                statusAqi.setText("Very Poor");
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
        requestQueue.add(stringRequest);

    }

    private void initUi() {
        imgBackHome = view.findViewById(R.id.icBackDetail);
        tvTemp = view.findViewById(R.id.tempValue);
        tvHumidity = view.findViewById(R.id.humidityValue);
        tvWind = view.findViewById(R.id.windValue);
        tvPm10 = view.findViewById(R.id.txPm10);
        tvPm25 = view.findViewById(R.id.txPm25);
        tvCo = view.findViewById(R.id.txCo);
        tvNo = view.findViewById(R.id.txNo);
        tvNo2 = view.findViewById(R.id.txNo2);
        tvO3 = view.findViewById(R.id.txO3);
        tvSo2 = view.findViewById(R.id.txSo2);
        tvNh3 = view.findViewById(R.id.txNh3);
        statusAqi = view.findViewById(R.id.statusAqi);
    }
}