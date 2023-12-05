package com.example.aircareapp.View;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.aircareapp.AccessAPI.AccessAPI;
import com.example.aircareapp.R;
import com.example.aircareapp.SQLite.WeatherAsset;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private Button btnPlace;
    private View view;
    private ImageView imgAvatar;
    TextView tvWeather, tvHumidity, tvCloud, tvWindy;


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

        GetCurrentWeatherData();
        showUserAvatar();
        initListener();
    }

    private void initListener() {
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailPlaceFragment detailPlaceFragment = new DetailPlaceFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.mainActivity, detailPlaceFragment,"fragDetailPlaceFragment");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void initUi() {
        imgAvatar = view.findViewById(R.id.imgAvatar);
        btnPlace = view.findViewById(R.id.buttonPlace);

        tvWeather = view.findViewById(R.id.tvWeather);
        tvHumidity = view.findViewById(R.id.tvHumidity);
        tvCloud = view.findViewById(R.id.tvCloud);
        tvWindy = view.findViewById(R.id.tvWindy);
    }

    public void GetCurrentWeatherData () {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "https://api.openweathermap.org/data/2.5/weather?q=Saigon&units=metric&appid=2cd54a866e6c50acd7951f230be4b369";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AccessAPI.getUrlGoogleWeather(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("MyRespond",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");

                            //get and set temp
                            String strTemp = jsonObjectMain.getString("temp");
                            Double a = Double.valueOf(strTemp);
                            String temp = String.valueOf(a.intValue());
//                            Log.e("MyRespond Temp",strTemp);
                            tvWeather.setText(temp + "°");

                            //get and set humidity
                            String humidity = jsonObjectMain.getString("humidity");
//                            Log.e("MyRespond humidity",humidity);
                            tvHumidity.setText(humidity + "%");

                            //get and set cloud
                            String cloud = jsonObjectCloud.getString("all");
//                            Log.e("MyRespond Cloud",cloud);
                            tvCloud.setText(cloud + "%");

                            //get and set windy
                            String strWindy = jsonObjectWind.getString("speed");
//                            Double b = Double.valueOf(strWindy);
//                            String windy = String.valueOf(b.intValue());
//                            Log.e("MyRespond Windy",strWindy);
                            tvWindy.setText(strWindy + "m/s");



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MyRespond","fail");
                    }
                });
        requestQueue.add(stringRequest);

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