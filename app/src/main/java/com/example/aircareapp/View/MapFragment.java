package com.example.aircareapp.View;

import static com.example.aircareapp.SSLHandle.SSLHandle.handleSSLHandshake;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.aircareapp.AccessAPI.AccessAPI;
import com.example.aircareapp.Model.ItemsDetail;
import com.example.aircareapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private int humidity, pressure;
    private long sunriseTimestamp, sunsetTimestamp;
    private double temp, temp_max, temp_min, feels_like, speed, all;
    private TextView tvNameAsset, tvID, tvVersion, tvCreatedOn, tvType;
    private JsonObjectRequest jsonObjectRequest, jsonObjectAssetRequest, jsonObjectAsset2Request;
    private JSONObject jsonOptions, jsonDefault, jsonObjectWeatherData, jsonObjectValue, jsonObjectMain, jsonObjectWind, jsonObjectSys, jsonObjectCloud,
            jsonObjectAttributes, jsonObjectRequestQueryParameters, jsonObjectLocation, jsonObjectBrightness, jsonObjectColourTemperature, jsonObjectEmail;
    private JSONArray center, bounds, assetBounds1, assetBounds2, jsonObjectCoordinates;
    private double zoom, maxZoom, minZoom;
    private boolean boxZoom;
    private Date sunriseDate, sunsetDate;
    private String geocodeUrl, sunrise, sunset, ID, version, createdOn, formatDays, type, nameMarker;
    private View viewDialog;
    private Button btnDetail;
    private BottomSheetDialog bottomSheetDialog;
    private RequestQueue mapRequestQueue, asset1RequestQueue, asset2RequestQueue;

    //    private TokenManager tokenManager;
//
//    public MapFragment(RequestQueue requestQueue, TokenManager tokenManager) {
//        this.requestQueue = requestQueue;
//        this.tokenManager = tokenManager;
//    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        initUI();
    }

    private void initUI() {
        viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet, null);
        tvNameAsset = viewDialog.findViewById(R.id.tvNameAsset);
        tvID = viewDialog.findViewById(R.id.tvID);
        tvVersion = viewDialog.findViewById(R.id.tvVersion);
        tvCreatedOn = viewDialog.findViewById(R.id.tvCreatedOn);
        tvType = viewDialog.findViewById(R.id.tvType);
        btnDetail = viewDialog.findViewById(R.id.btnDetail);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        handleSSLHandshake();
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dataLogin", Context.MODE_PRIVATE);
//        String token = sharedPreferences.getString("token", "");
//        Log.d("tokenProfile", token);

        mapRequestQueue = Volley.newRequestQueue(getActivity());

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AccessAPI.getURLMap(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("MyResponseMap", "" + response);
                        try {
                            jsonOptions = response.getJSONObject("options");
                            jsonDefault = jsonOptions.getJSONObject("default");
                            // Các thông số
                            center = jsonDefault.getJSONArray("center");
                            bounds = jsonDefault.getJSONArray("bounds");
                            zoom = jsonDefault.getDouble("zoom");
                            maxZoom = jsonDefault.getDouble("maxZoom");
                            boxZoom = jsonDefault.getBoolean("boxZoom");
                            geocodeUrl = jsonDefault.getString("geocodeUrl");
                            minZoom = jsonDefault.getDouble("minZoom");

                            // Call API asset 1
                            asset1RequestQueue = Volley.newRequestQueue(getActivity());
                            jsonObjectAssetRequest = new JsonObjectRequest(Request.Method.GET, AccessAPI.getUrlAsset1(), null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.e("MyResponsejsonObject1", "" + response);

                                            try {
                                                ID = response.getString("id");
                                                version = response.getString("version");
                                                type = response.getString("type");
                                                createdOn = response.getString("createdOn");
                                                nameMarker = response.getString("name");
                                                jsonObjectAttributes = response.getJSONObject("attributes");
                                                jsonObjectRequestQueryParameters = jsonObjectAttributes.getJSONObject("requestQueryParameters");
                                                jsonObjectValue = jsonObjectRequestQueryParameters.getJSONObject("value");
                                                assetBounds1 = jsonObjectValue.getJSONArray("lat");
                                                assetBounds2 = jsonObjectValue.getJSONArray("lon");

                                                jsonObjectWeatherData = jsonObjectAttributes.getJSONObject("data");
                                                jsonObjectValue = jsonObjectWeatherData.getJSONObject("value");
                                                jsonObjectMain = jsonObjectValue.getJSONObject("main");
                                                jsonObjectWind = jsonObjectValue.getJSONObject("wind");
                                                jsonObjectCloud = jsonObjectValue.getJSONObject("clouds");
                                                jsonObjectSys = jsonObjectValue.getJSONObject("sys");

                                                temp = jsonObjectMain.getDouble("temp");
                                                humidity = jsonObjectMain.getInt("humidity");
                                                pressure = jsonObjectMain.getInt("pressure");
                                                temp_max = jsonObjectMain.getDouble("temp_max");
                                                temp_min = jsonObjectMain.getDouble("temp_min");
                                                feels_like = jsonObjectMain.getDouble("feels_like");
                                                speed = jsonObjectWind.getDouble("speed");
                                                all = jsonObjectCloud.getDouble("all");
                                                sunriseTimestamp = jsonObjectSys.getLong("sunrise");
                                                sunsetTimestamp = jsonObjectSys.getLong("sunset");
                                                Log.e("MyAssetBounds", "" + assetBounds1 + " - " + assetBounds2);

                                                nameMarker = response.getString("name");
                                                Log.e("nameMarker", "nameMarker: " + nameMarker);

                                                if (assetBounds1.length() > 0 && assetBounds2.length() > 0) {
                                                    try {
                                                        // Extracting the first element from each JSONArray
                                                        Object lat = assetBounds1.get(0);
                                                        Object lon = assetBounds2.get(0);

                                                        // Checking if the elements are not null and are instances of Number
                                                        if (lat != null && lon != null && lat instanceof Number && lon instanceof Number) {
                                                            double doublelat = ((Number) lat).doubleValue();
                                                            double doublelon = ((Number) lon).doubleValue();
                                                            LatLng locationsAssetCurrent = new LatLng(doublelat, doublelon);
                                                            googleMap.addMarker(new MarkerOptions().position(locationsAssetCurrent).title(nameMarker));
                                                        } else {
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                Log.e("MyassetBounds", "" + assetBounds1 + " - " + assetBounds2);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            //set onclick marker
                                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                                @Override
                                                public boolean onMarkerClick(Marker marker) {
                                                    String tempName = marker.getTitle();
                                                    Log.d("tempName", "onMarkerClick: " + tempName);
                                                        Log.d("onMarkerClick22", "onMarkerClick: " + nameMarker);

                                                        if (tempName.equals(nameMarker) == true) {
                                                            bottomSheetDialog = new BottomSheetDialog(getActivity());
                                                            bottomSheetDialog.setContentView(viewDialog);
                                                            bottomSheetDialog.show();

                                                            tvNameAsset.setText(nameMarker);
                                                            tvID.setText(ID);
                                                            tvVersion.setText(version);
                                                            tvType.setText(type);

                                                            long l1 = Long.valueOf(createdOn);
                                                            Date date1 = new Date(l1 * 1000L);
                                                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM-dd");
                                                            formatDays = simpleDateFormat1.format(date1);
                                                            tvCreatedOn.setText(formatDays);

                                                            // Convert timestamps to Date objects & seconds to milliseconds
                                                            sunriseDate = new Date(sunriseTimestamp * 1000L);
                                                            sunsetDate = new Date(sunsetTimestamp * 1000L);
                                                            // Format the dates as strings with HH:mm format
                                                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                                            sunrise = sdf.format(sunriseDate);
                                                            sunset = sdf.format(sunsetDate);
                                                            Log.d("dataAsset1", "onMarkerClick: " + temp + humidity + pressure + sunrise + sunset + temp_max + temp_min + feels_like + speed + all);

                                                            btnDetail.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    ViewAssetData1();
                                                                }
                                                            });
                                                        }
                                                    return false;
                                                }
                                            });
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

//                            asset2RequestQueue = Volley.newRequestQueue(getActivity());
//                            jsonObjectAsset2Request = new JsonObjectRequest(Request.Method.GET, AccessAPI.getUrlAsset2(), null,
//                                    new Response.Listener<JSONObject>() {
//                                        @Override
//                                        public void onResponse(JSONObject response) {
//                                            Log.e("MyResponsejsonObject2", "" + response);
//
//                                            try {
//                                                jsonObjectAttributes = response.getJSONObject("attributes");
//                                                jsonObjectLocation = jsonObjectAttributes.getJSONObject("location");
//                                                jsonObjectValue = jsonObjectLocation.getJSONObject("value");
//                                                jsonObjectCoordinates = jsonObjectValue.getJSONArray("coordinates");
//
//                                                jsonObjectBrightness = jsonObjectAttributes.getJSONObject("brightness");
//                                                jsonObjectColourTemperature = jsonObjectAttributes.getJSONObject("colourTemperature");
//                                                jsonObjectEmail = jsonObjectAttributes.getJSONObject("email");
//
//                                                ID = response.getString("id");
//                                                version = response.getString("version");
//                                                type = response.getString("type");
//                                                createdOn = response.getString("createdOn");
//                                                int valueBrightness = jsonObjectBrightness.getInt("value");
//                                                int valueColourTemperature = jsonObjectColourTemperature.getInt("value");
//                                                String email = jsonObjectEmail.getString("value");
//
//                                                double lat = jsonObjectCoordinates.getDouble(0);
//                                                double lon = jsonObjectCoordinates.getDouble(1);
//                                                Log.e("MyAsset2Bounds", "" + lat + " - " + lon);
//
//                                                nameMarker = response.getString("name");
//                                                Log.e("nameMarker", "nameMarker: " + nameMarker);
//
//                                                if (lat > 0 && lon > 0) {
//                                                    LatLng locationsAsset2 = new LatLng(lat, lon);
//                                                    googleMap.addMarker(new MarkerOptions().position(locationsAsset2).title(nameMarker));
//                                                }
//                                                Log.e("MyassetBounds", "" + lat + " - " + lon);
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//
//                                            //set onclick marker
//                                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                                                @Override
//                                                public boolean onMarkerClick(Marker marker) {
//                                                    String tempName = marker.getTitle();
//                                                    Log.d("tempName", "onMarkerClick: " + tempName);
//                                                    try {
//                                                        nameMarker = response.getString("name");
//                                                        Log.d("onMarkerClick22", "onMarkerClick: " + nameMarker);
//
//                                                        if (tempName.equals(nameMarker) == true) {
////                                                            ViewGroup parent = (ViewGroup) viewDialog.getParent();
////                                                            if (parent != null) {
////                                                                parent.removeView(viewDialog);
////                                                            }
//
//                                                            bottomSheetDialog = new BottomSheetDialog(getActivity());
//                                                            bottomSheetDialog.setContentView(viewDialog);
//                                                            bottomSheetDialog.show();
//
//                                                            tvNameAsset.setText(nameMarker);
//                                                            tvID.setText(ID);
//                                                            tvVersion.setText(version);
//                                                            tvType.setText(type);
//
//                                                            long l1 = Long.valueOf(createdOn);
//                                                            Date date1 = new Date(l1 * 1000L);
//                                                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM-dd");
//                                                            formatDays = simpleDateFormat1.format(date1);
//                                                            tvCreatedOn.setText(formatDays);
//
////                                                            sunriseTimestamp = jsonObjectSys.getLong("sunrise");
////                                                            sunsetTimestamp = jsonObjectSys.getLong("sunset");
////                                                            // Convert timestamps to Date objects & seconds to milliseconds
////                                                            sunriseDate = new Date(sunriseTimestamp * 1000L);
////                                                            sunsetDate = new Date(sunsetTimestamp * 1000L);
////                                                            // Format the dates as strings with HH:mm format
////                                                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
////                                                            sunrise = sdf.format(sunriseDate);
////                                                            sunset = sdf.format(sunsetDate);
////                                                            Log.d("dataAsset1", "onMarkerClick: " + temp + humidity + pressure + sunrise + sunset + temp_max + temp_min + feels_like + speed + all);
//
//                                                            btnDetail.setOnClickListener(new View.OnClickListener() {
//                                                                @Override
//                                                                public void onClick(View v) {
//                                                                    ViewAssetData2();
//                                                                }
//                                                            });
//                                                        }
//                                                    } catch (JSONException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                    return false;
//                                                }
//                                            });
//                                        }
//                                    },
//                                    new Response.ErrorListener() {
//                                        @Override
//                                        public void onErrorResponse(VolleyError error) {
//                                            Log.e("MyErrorUserCurrent", "" + error);
//                                        }
//                                    }) {
//                                @Override
//                                public java.util.Map<String, String> getHeaders() throws AuthFailureError {
//                                    Map<String, String> params = new HashMap<String, String>();
//                                    params.put("accept", "application/json;");
//                                    params.put("Authorization", "Bearer " + AccessAPI.getToken());
//                                    return params;
//                                }
//                            };
//                            asset2RequestQueue.add(jsonObjectAsset2Request);

                            // Add data on Map
                            try {
                                LatLng UIT = new LatLng((Double) center.get(1), (Double) center.get(0));
                                LatLngBounds UITBounds = new LatLngBounds(
                                        new LatLng((Double) bounds.get(1), (Double) bounds.get(0)),
                                        new LatLng((Double) bounds.get(3), (Double) bounds.get(2))
                                );
                                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                //add Marker
                                googleMap.addMarker(new MarkerOptions().position(UIT).title("Weather HTTP"));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UIT, (float) maxZoom - 2));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(UIT, (float) maxZoom - 2));
                                googleMap.setLatLngBoundsForCameraTarget(UITBounds);
                                googleMap.addMarker(new MarkerOptions().position(new LatLng(10.870464609989156, 106.80273973945748
                                )).title("Weather Asset"));
                                googleMap.addMarker(new MarkerOptions().position(new LatLng(10.870537960015568, 106.80390988702072
                                )).title("Weather Asset 2"));
                                googleMap.addMarker(new MarkerOptions().position(new LatLng(10.869905172970164, 106.80345028525176
                                )).title("Light"));

                            } catch (JSONException e) {
                                Log.e("MyErrorLoadMakerMap", "" + e);
                            }
                            asset1RequestQueue.add(jsonObjectAssetRequest);
                        } catch (JSONException e) {
                            Log.e("MyErrorGetDataMap", "" + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MyError", "" + error);
                    }
                });
        mapRequestQueue.add(jsonObjectRequest);
    }

    private void ViewAssetData2() {

    }

    private void ViewAssetData1() {
        AqiAssetFragment.arrayList = new ArrayList<>();
        AqiAssetFragment.arrayList.add(new ItemsDetail("Temperature", temp + "°C", R.drawable.ic_temperature_color));
        AqiAssetFragment.arrayList.add(new ItemsDetail("Humidity", humidity + "%", R.drawable.ic_humidity));
        AqiAssetFragment.arrayList.add(new ItemsDetail("Pressure", pressure + "hPa", R.drawable.ic_pressure));
        AqiAssetFragment.arrayList.add(new ItemsDetail("Sunrise", sunrise, R.drawable.icon_sunrise));
        AqiAssetFragment.arrayList.add(new ItemsDetail("Sunset", sunset, R.drawable.ic_sunset));
        AqiAssetFragment.arrayList.add(new ItemsDetail("Temp max", temp_max + "°C", R.drawable.ic_temperature_high));
        AqiAssetFragment.arrayList.add(new ItemsDetail("Temp min", temp_min + "°C", R.drawable.ic_temperature_low));
        AqiAssetFragment.arrayList.add(new ItemsDetail("Feels like", feels_like + "°C", R.drawable.ic_temperature_color));
        AqiAssetFragment.arrayList.add(new ItemsDetail("Speed", speed + "m/s", R.drawable.ic_wind));
        AqiAssetFragment.arrayList.add(new ItemsDetail("Clouds", all + "%", R.drawable.ic_cloud));

        bottomSheetDialog.dismiss();
        AqiAssetFragment weatherAssetFragment = new AqiAssetFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainActivity, weatherAssetFragment, "fragDetails");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}