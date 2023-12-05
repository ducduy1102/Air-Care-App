package com.example.aircareapp.View;

import static com.example.aircareapp.SSLHandle.SSLHandle.handleSSLHandshake;

import android.annotation.SuppressLint;
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
import com.android.volley.toolbox.JsonArrayRequest;
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
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    JsonObjectRequest jsonObjectRequest;
    JsonArrayRequest jsonArrayRequest;
    JSONObject jsonOptions, jsonDefault;
    JSONArray center, bounds, assetBounds1, assetBounds2;
    double zoom, maxZoom, minZoom;
    boolean boxZoom;
    String geocodeUrl;
    int index;

    //    private RequestQueue requestQueue;
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
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        handleSSLHandshake();
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dataLogin", Context.MODE_PRIVATE);
//        String token = sharedPreferences.getString("token", "");
//        Log.d("tokenProfile", token);

        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());

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

                            // Call API userCurrent
                            RequestQueue aRequestQueue = Volley.newRequestQueue(getActivity());

                            JsonObjectRequest jsonObjectAssetRequest = new JsonObjectRequest(Request.Method.GET, AccessAPI.getUrlAsset1(), null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.e("MyResponsejsonObject1", "" + response);

                                            Log.e("MyResponseAsset1", "" + response);
                                            try {
//                                                    JSONObject jsonObject = new JSONObject(String.valueOf(response.get(String.valueOf(i))));

                                                JSONObject jsonObjectAttributes = response.getJSONObject("attributes");
                                                JSONObject jsonObjectRequestQueryParameters = jsonObjectAttributes.getJSONObject("requestQueryParameters");
                                                JSONObject jsonObjectValue = jsonObjectRequestQueryParameters.getJSONObject("value");
                                                assetBounds1 = jsonObjectValue.getJSONArray("lat");
                                                assetBounds2 = jsonObjectValue.getJSONArray("lon");
                                                Log.e("MyAssetBounds", "" + assetBounds1 + " - " + assetBounds2);

                                                String nameMarker = response.getString("name");
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
                                                        e.printStackTrace(); // Handle the JSONException as needed
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
                                                    try {
                                                        Log.e("MyResponsejsonObject2", "" + response);

                                                        JSONObject jsonObjectAttributes = response.getJSONObject("attributes");
//                                                            JSONObject jsonRequestQueryParameters = jsonObjectAttributes.getJSONObject("requestQueryParameters");

                                                        String nameMarker = response.getString("name");
//                                                            String nameMarker = "Trường Đại Học Công Nghệ Thông Tin";
                                                        Log.d("onMarkerClick22", "onMarkerClick: " + nameMarker);

                                                        if (tempName.equals(nameMarker) == true) {
                                                            View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet, null);
                                                            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                                                            bottomSheetDialog.setContentView(viewDialog);
                                                            bottomSheetDialog.show();

                                                            TextView tvNameAsset = viewDialog.findViewById(R.id.tvNameAsset);
                                                            tvNameAsset.setText(nameMarker);
                                                            Log.d("tvNameAsset", "tvNameAsset: " + tvNameAsset);


                                                            String ID = response.getString("id");
                                                            TextView tvID = viewDialog.findViewById(R.id.tvID);
                                                            tvID.setText(ID);
                                                            Log.d("tvID", "onMarkerClick: " + ID);


                                                            String version = response.getString("version");
                                                            TextView tvVersion = viewDialog.findViewById(R.id.tvVersion);
                                                            tvVersion.setText(version);

                                                            String createdOn = response.getString("createdOn");
                                                            long l1 = Long.valueOf(createdOn);
                                                            Date date1 = new Date(l1 * 1000L);
                                                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM-dd");
                                                            String formatDays = simpleDateFormat1.format(date1);
                                                            TextView tvCreatedOn = viewDialog.findViewById(R.id.tvCreatedOn);
                                                            tvCreatedOn.setText(formatDays);

                                                            String type = response.getString("type");
                                                            TextView tvType = viewDialog.findViewById(R.id.tvType);
                                                            tvType.setText(type);


                                                            JSONObject jsonObjectWeatherData = jsonObjectAttributes.getJSONObject("data");
                                                            JSONObject jsonObjectValue = jsonObjectWeatherData.getJSONObject("value");
                                                            JSONObject jsonObjectMain = jsonObjectValue.getJSONObject("main");
                                                            JSONObject jsonObjectWind = jsonObjectValue.getJSONObject("wind");
                                                            JSONObject jsonObjectCloud = jsonObjectValue.getJSONObject("clouds");


                                                            double temp = jsonObjectMain.getDouble("temp");
                                                            int humidity = jsonObjectMain.getInt("humidity");
                                                            int pressure = jsonObjectMain.getInt("pressure");
                                                            double temp_max = jsonObjectMain.getDouble("temp_max");
                                                            double temp_min = jsonObjectMain.getDouble("temp_min");
                                                            double feels_like = jsonObjectMain.getDouble("feels_like");
                                                            double speed = jsonObjectWind.getDouble("speed");
                                                            double all = jsonObjectCloud.getDouble("all");
                                                            Log.d("dataAsset1", "onMarkerClick: " + temp + humidity + pressure + temp_max + temp_min + feels_like + speed + all);


                                                            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnDetail = viewDialog.findViewById(R.id.btnDetail);
                                                            btnDetail.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {

                                                                    AqiAssetFragment.arrayList = new ArrayList<>();
                                                                    AqiAssetFragment.arrayList.add(new ItemsDetail("Temperature", temp + "°C", R.drawable.ic_temperature_color));
                                                                    AqiAssetFragment.arrayList.add(new ItemsDetail("Humidity", humidity + "%", R.drawable.ic_humidity));
                                                                    AqiAssetFragment.arrayList.add(new ItemsDetail("Pressure", pressure + "hPa", R.drawable.ic_pressure));
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
                                                            });
                                                        }


                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }


                                                    return false;
                                                }
                                            });
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
//                                                googleMap.addMarker(new MarkerOptions().position(new LatLng(10.869792504441904, 106.80308672274282
//                                                )).title("Weather Asset 3"));

                                            } catch (JSONException e) {
                                                Log.e("MyErrorLoadMakerMap", "" + e);
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
                            aRequestQueue.add(jsonObjectAssetRequest);

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
                }) {
//            @Override
//            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json;");
////                params.put("token", "Bearer " + token);
//                params.put("Authorization", "Bearer " + AccessAPI.getToken());
//                return params;
//            }
        };
        mRequestQueue.add(jsonObjectRequest);
    }

//    public void onMapReady(@NonNull GoogleMap googleMap) {
//
//        handleSSLHandshake();
////        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dataLogin", Context.MODE_PRIVATE);
////        String token = sharedPreferences.getString("token", "");
////        Log.d("tokenProfile", token);
//
//        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
//
//        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AccessAPI.getURLMap(), null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e("MyResponseMap", "" + response);
//                        try {
//                            jsonOptions = response.getJSONObject("options");
//                            jsonDefault = jsonOptions.getJSONObject("default");
//                            // Các thông số
//                            center = jsonDefault.getJSONArray("center");
//                            bounds = jsonDefault.getJSONArray("bounds");
//                            zoom = jsonDefault.getDouble("zoom");
//                            maxZoom = jsonDefault.getDouble("maxZoom");
//                            boxZoom = jsonDefault.getBoolean("boxZoom");
//                            geocodeUrl = jsonDefault.getString("geocodeUrl");
//                            minZoom = jsonDefault.getDouble("minZoom");
//
//                            LatLng UIT = new LatLng((Double) center.get(1), (Double) center.get(0));
//
//                            LatLngBounds UITBounds = new LatLngBounds(
//                                    new LatLng((Double) bounds.get(1), (Double) bounds.get(0)),
//                                    new LatLng((Double) bounds.get(3), (Double) bounds.get(2))
//                            );
//                            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//                            //add Marker
//                            googleMap.addMarker(new MarkerOptions().position(UIT).title("Trường Đại Học Công Nghệ Thông Tin"));
//                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UIT, (float) maxZoom - 2));
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(UIT, (float) maxZoom - 2));
//                            googleMap.setLatLngBoundsForCameraTarget(UITBounds);
//
//                            googleMap.addMarker(new MarkerOptions().position(new LatLng(10.870464609989156, 106.80273973945748
//                            )).title("Weather Asset"));
//                            googleMap.addMarker(new MarkerOptions().position(new LatLng(10.870537960015568, 106.80390988702072
//                            )).title("Weather Asset 2"));
//                            googleMap.addMarker(new MarkerOptions().position(new LatLng(10.869792504441904, 106.80308672274282
//                            )).title("Weather Asset 3"));
//                        } catch (JSONException e) {
//                            Log.e("MyErrorGetDataMap", "" + e);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("MyError", "" + error);
//                    }
//                });
//        mRequestQueue.add(jsonObjectRequest);
//    }
}