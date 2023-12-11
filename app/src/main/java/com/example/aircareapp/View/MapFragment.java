package com.example.aircareapp.View;

import static com.example.aircareapp.SSLHandle.SSLHandle.handleSSLHandshake;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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
import java.util.Locale;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private int humidity, pressure, aqi, aqiPredict;
    private long sunriseTimestamp, sunsetTimestamp;
    private double temp, temp_max, temp_min, feels_like, speed, all;
    private TextView tvNameAsset, tvID, tvVersion, tvCreatedOn, tvType, tvTitle2, tvTitle3, tvValue1, tvValue2, tvValue3, tvValue4, tvValue5, tvValue6, tvValue7, tvNameDefault,
            tvNameAsset2, tvID2, tvVersion2, tvType2, tvBrightness, tvCreatedOn2, tvEmail, tvColorTemp;
    private JsonObjectRequest jsonObjectRequest, jsonObjectAssetRequest, jsonObjectAsset2Request, jsonObjectAsset3Request, jsonObjectAssetDefaultRequest;
    private JSONObject jsonOptions, jsonDefault, jsonObjectWeatherData, jsonObjectValue, jsonObjectMain, jsonObjectWind, jsonObjectSys, jsonObjectCloud,
            jsonObjectAttributes, jsonObjectRequestQueryParameters, jsonObjectAttributes2, jsonObjectLocation2, jsonObjectValue2, jsonObjectBrightness2, jsonObjectColourTemperature2, jsonObjectEmail2,
            jsonObjectAttributes3, jsonObjectPM25, jsonObjectPM10, jsonObjectCO2, jsonObjectHumidity, jsonObjectAQI, jsonObjectAQIPredict, jsonObjectAssetUser, jsonObjectId, jsonObjectAttributesDefault,
            jsonObjectHumidityDefault, jsonObjectRainfall, jsonObjectWindSpeed, jsonObjectManufacturer, jsonObjectTemperature, jsonObjectWindDirection, jsonObjectPlace;
    private JSONArray center, bounds, assetBounds1, assetBounds2, jsonObjectCoordinates2;
    private JsonArrayRequest jsonArrayAssetUser;
    private double zoom, maxZoom, minZoom, pm25, pm10, C02;
    private boolean boxZoom;
    private Date sunriseDate, sunsetDate;
    private String geocodeUrl, sunrise, sunset, ID, version, createdOn, formatDays, type, nameMarker1, nameMarker2, ID2, version2, createdOn2, formatDays2, type2,
            email2, nameMarkerAssetUser, createdOnAssetUser, assetName, parentAssetName, userFullName, idAssetUser, rainfall, manufacturer, windSpeed, windDirection,
            temperature, humidityDefault, place, nameDefault, valueBrightness2, valueColourTemperature2;
    private String[] users = new String[4];
    private View viewDialog, viewDialogWeather;
    private Button btnDetail, btnView;
    private BottomSheetDialog bottomSheetDialog, bottomSheetDialogWeather;
    private RequestQueue mapRequestQueue, asset1RequestQueue, asset2RequestQueue, asset3RequestQueue, assetUserRequestQueue, assetDefaultRequestQueue;

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
        tvTitle2 = viewDialog.findViewById(R.id.tvTitle2);
        tvTitle3 = viewDialog.findViewById(R.id.tvTitle3);
        btnDetail = viewDialog.findViewById(R.id.btnDetail);

        viewDialogWeather = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_asset_1, null);
        tvValue1 = viewDialogWeather.findViewById(R.id.value1);
        tvValue2 = viewDialogWeather.findViewById(R.id.value2);
        tvValue3 = viewDialogWeather.findViewById(R.id.value3);
        tvValue4 = viewDialogWeather.findViewById(R.id.value4);
        tvValue5 = viewDialogWeather.findViewById(R.id.value5);
        tvValue6 = viewDialogWeather.findViewById(R.id.value6);
        tvValue7 = viewDialogWeather.findViewById(R.id.value7);
        tvNameDefault = viewDialogWeather.findViewById(R.id.tvDefault);

        tvNameAsset2 = viewDialogWeather.findViewById(R.id.tvDefault);
        tvID2 = viewDialogWeather.findViewById(R.id.title1);
        tvVersion2 = viewDialogWeather.findViewById(R.id.title2);
        tvType2 = viewDialogWeather.findViewById(R.id.title3);
        tvBrightness = viewDialogWeather.findViewById(R.id.title4);
        tvCreatedOn2 = viewDialogWeather.findViewById(R.id.title5);
        tvEmail = viewDialogWeather.findViewById(R.id.title6);
        tvColorTemp = viewDialogWeather.findViewById(R.id.title7);

        btnView = viewDialogWeather.findViewById(R.id.btnView);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Add data on Map

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

                            // add data
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

                            } catch (JSONException e) {
                                Log.e("MyErrorLoadMakerMap", "" + e);
                            }

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

                                                nameMarker1 = response.getString("name");
                                                Log.e("nameMarker", "nameMarker: " + nameMarker1);

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
                                                            googleMap.addMarker(new MarkerOptions().position(locationsAssetCurrent).title(nameMarker1));
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

                            // Call API asset 2
                            asset2RequestQueue = Volley.newRequestQueue(getActivity());
                            jsonObjectAsset2Request = new JsonObjectRequest(Request.Method.GET, AccessAPI.getUrlAsset2(), null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.e("MyResponsejsonObject2", "" + response);

                                            try {
                                                jsonObjectAttributes2 = response.getJSONObject("attributes");
                                                jsonObjectLocation2 = jsonObjectAttributes2.getJSONObject("location");
                                                jsonObjectValue2 = jsonObjectLocation2.getJSONObject("value");
                                                jsonObjectCoordinates2 = jsonObjectValue2.getJSONArray("coordinates");

                                                jsonObjectBrightness2 = jsonObjectAttributes2.getJSONObject("brightness");
                                                jsonObjectColourTemperature2 = jsonObjectAttributes2.getJSONObject("colourTemperature");
                                                jsonObjectEmail2 = jsonObjectAttributes2.getJSONObject("email");

                                                ID2 = response.getString("id");
                                                version2 = response.getString("version");
                                                type2 = response.getString("type");
                                                createdOn2 = response.getString("createdOn");
                                                valueBrightness2 = jsonObjectBrightness2.getString("value");
                                                valueColourTemperature2 = jsonObjectColourTemperature2.getString("value");
                                                email2 = jsonObjectEmail2.getString("value");

                                                double lat = jsonObjectCoordinates2.getDouble(1);
                                                double lon = jsonObjectCoordinates2.getDouble(0);
                                                Log.e("MyAsset2Bounds", "" + lat + " - " + lon);

                                                nameMarker2 = response.getString("name");
                                                Log.e("nameMarker", "nameMarker: " + nameMarker2);
                                                Log.e("valueBrightness2", "nameMarker: " + valueBrightness2);



                                                if (lat > 0 && lon > 0) {
                                                    LatLng locationsAsset2 = new LatLng(lat, lon);
                                                    googleMap.addMarker(new MarkerOptions().position(locationsAsset2).title(nameMarker2));
                                                }
                                                Log.e("MyassetBounds", "" + lat + " - " + lon);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("MyErrorAsset2", "" + error);
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
                                                jsonObjectHumidity = jsonObjectAttributes3.getJSONObject("humidity");
                                                jsonObjectAQI = jsonObjectAttributes3.getJSONObject("AQI");
                                                jsonObjectAQIPredict = jsonObjectAttributes3.getJSONObject("AQI_Predict");

                                                pm10 = jsonObjectPM10.getDouble("value");
                                                pm25 = jsonObjectPM25.getDouble("value");
                                                C02 = jsonObjectCO2.getDouble("value");
                                                humidity = jsonObjectHumidity.getInt("value");
                                                aqi = jsonObjectAQI.getInt("value");
                                                aqiPredict = jsonObjectAQIPredict.getInt("value");

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

                            // Call API asset default weather
                            assetDefaultRequestQueue = Volley.newRequestQueue(getActivity());
                            jsonObjectAssetDefaultRequest = new JsonObjectRequest(Request.Method.GET, AccessAPI.getUrlAssetDefault(), null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.e("MyResponsejsonDefaultObject", "" + response);
                                            try {
                                                nameDefault = response.getString("name");
                                                jsonObjectAttributesDefault = response.getJSONObject("attributes");
                                                jsonObjectRainfall = jsonObjectAttributesDefault.getJSONObject("rainfall");
                                                jsonObjectManufacturer = jsonObjectAttributesDefault.getJSONObject("manufacturer");
                                                jsonObjectTemperature = jsonObjectAttributesDefault.getJSONObject("temperature");
                                                jsonObjectHumidityDefault = jsonObjectAttributesDefault.getJSONObject("humidity");
                                                jsonObjectPlace = jsonObjectAttributesDefault.getJSONObject("place");
                                                jsonObjectWindDirection = jsonObjectAttributesDefault.getJSONObject("windDirection");
                                                jsonObjectWindSpeed = jsonObjectAttributesDefault.getJSONObject("windSpeed");

                                                rainfall = jsonObjectRainfall.getString("value");
                                                manufacturer = jsonObjectManufacturer.getString("value");
                                                temperature = jsonObjectTemperature.getString("value");
                                                humidityDefault = jsonObjectHumidityDefault.getString("value");
                                                place = jsonObjectPlace.getString("value");
                                                windDirection = jsonObjectWindDirection.getString("value");
                                                windSpeed = jsonObjectWindSpeed.getString("value");
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

                            // Call API asset user
                            assetUserRequestQueue = Volley.newRequestQueue(getActivity());
                            jsonArrayAssetUser = new JsonArrayRequest(Request.Method.GET, AccessAPI.getUrlAssetUser(), null,
                                    new Response.Listener<JSONArray>() {
                                        @Override
                                        public void onResponse(JSONArray response) {
                                            Log.e("MyResponsejsonObjetAsserUser", "" + response);

                                            for (int i = 0; i < 4; i++) {
                                                try {
                                                    jsonObjectAssetUser = response.getJSONObject(i);
                                                    jsonObjectId = jsonObjectAssetUser.getJSONObject("id");
                                                    createdOnAssetUser = jsonObjectAssetUser.getString("createdOn");
                                                    Log.d("createdOnAssetUser", "onResponse: " + createdOn);
                                                    assetName = jsonObjectAssetUser.getString("assetName");
                                                    parentAssetName = jsonObjectAssetUser.getString("parentAssetName");
                                                    userFullName = jsonObjectAssetUser.getString("userFullName");
                                                    idAssetUser = jsonObjectId.getString("assetId");

                                                    users[i] = userFullName;

                                                    googleMap.addMarker(new MarkerOptions().position(getLatLngForUser(i)).title(userFullName));

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                                @Override
                                                public boolean onMarkerClick(Marker marker) {
                                                    String tempName = marker.getTitle();
                                                    ViewGroup parentViewGroup = (ViewGroup) viewDialog.getParent();
                                                    if (parentViewGroup != null) {
                                                        parentViewGroup.removeView(viewDialog);
                                                    }

                                                    ViewGroup parentViewGroup2 = (ViewGroup) viewDialogWeather.getParent();
                                                    if (parentViewGroup2 != null) {
                                                        parentViewGroup2.removeView(viewDialogWeather);
                                                    }
                                                    bottomSheetDialog = new BottomSheetDialog(getActivity());

                                                    FrameLayout container = new FrameLayout(getContext());
                                                    container.removeAllViews();
                                                    Log.d("tempName", "onMarkerClick: " + tempName);

                                                    if (tempName.equals(nameMarker1)) {
                                                        container.addView(viewDialogWeather);
                                                        tvNameDefault.setText(nameDefault);
                                                        tvID2.setText(getResources().getString(R.string.humidity));
                                                        tvVersion2.setText(getResources().getString(R.string.manufacturer));
                                                        tvType2.setText(getResources().getString(R.string.place));
                                                        tvBrightness.setText(getResources().getString(R.string.titleRainfall));
                                                        tvCreatedOn2.setText(getResources().getString(R.string.temperature));
                                                        tvEmail.setText(getResources().getString(R.string.windDirection));
                                                        tvColorTemp.setText(getResources().getString(R.string.titleWindSpeed));

                                                        tvValue1.setText(humidityDefault);
                                                        tvValue2.setText(manufacturer);
                                                        tvValue3.setText(place);
                                                        tvValue4.setText(rainfall);
                                                        tvValue5.setText(temperature);
                                                        tvValue6.setText(windDirection);
                                                        tvValue7.setText(windSpeed);

                                                        long l1 = Long.valueOf(createdOn);
                                                        Date date1 = new Date(l1 * 1000L);
                                                        Log.d("dataAsset1", "onMarkerClick: " + date1);

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

                                                        btnView.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                ViewAssetData1();
                                                            }
                                                        });
                                                    } else if (tempName.equals(nameMarker2)) {
                                                        container.addView(viewDialogWeather);
                                                        tvNameAsset2.setText(nameMarker2);
                                                        tvID2.setText(getResources().getString(R.string.id));
                                                        tvVersion2.setText(getResources().getString(R.string.version));
                                                        tvType2.setText(getResources().getString(R.string.type));
                                                        tvBrightness.setText(getResources().getString(R.string.brightness));
                                                        tvCreatedOn2.setText(getResources().getString(R.string.createdOn));
                                                        tvEmail.setText(getResources().getString(R.string.email));
                                                        tvColorTemp.setText(getResources().getString(R.string.colourTemperature));

                                                        long l1 = Long.valueOf(createdOn2);
                                                        Date date1 = new Date(l1 * 1000L);
                                                        Log.d("dataAsset2", "onMarkerClick: " + date1);

                                                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM-dd");
                                                        formatDays2 = simpleDateFormat1.format(date1);

                                                        tvValue1.setText(ID2);
                                                        tvValue2.setText(version2);
                                                        tvValue3.setText(type2);
                                                        tvValue4.setText(valueBrightness2);
                                                        tvValue5.setText(formatDays2);
                                                        tvValue6.setText(email2);
                                                        tvValue7.setText(valueColourTemperature2);
                                                        Log.d("dataAsset2", "onMarkerClick: " + email2 + valueBrightness2 + valueColourTemperature2);

                                                        btnView.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                ViewAssetData2();
                                                            }
                                                        });
                                                    } else {
                                                        container.addView(viewDialog);
                                                        for (int i = 0; i < 4; i++) {
                                                            try {
                                                                jsonObjectAssetUser = response.getJSONObject(i);
                                                                jsonObjectId = jsonObjectAssetUser.getJSONObject("id");
                                                                createdOnAssetUser = jsonObjectAssetUser.getString("createdOn");
                                                                Log.d("createdOnAssetUser", "onResponse: " + createdOn);
                                                                assetName = jsonObjectAssetUser.getString("assetName");
                                                                parentAssetName = jsonObjectAssetUser.getString("parentAssetName");
                                                                userFullName = jsonObjectAssetUser.getString("userFullName");
                                                                idAssetUser = jsonObjectId.getString("assetId");

                                                                if (tempName.equals(userFullName)) {
                                                                    tvNameAsset.setText(userFullName);
                                                                    tvID.setText(idAssetUser);
                                                                    tvVersion.setText(assetName);
                                                                    tvType.setText(parentAssetName);
                                                                    tvTitle2.setText("Asset Name");
                                                                    tvTitle3.setText("Parent Asset Name");

                                                                    long l1 = Long.valueOf(createdOnAssetUser);
                                                                    Date date1 = new Date(l1 * 1000L);
                                                                    Log.d("dataAsset3", "onMarkerClick: " + date1);

                                                                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM-dd");
                                                                    formatDays2 = simpleDateFormat1.format(date1);
                                                                    tvCreatedOn.setText(formatDays2);

                                                                    Log.d("user1", "onMarkerClick: " + userFullName + "formatDays2" + formatDays2);
                                                                }

                                                                btnDetail.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        ViewAssetDataUser();
                                                                    }
                                                                });

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                    bottomSheetDialog.setContentView(container);
                                                    if (!bottomSheetDialog.isShowing()) {
                                                        bottomSheetDialog.show();
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
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("accept", "application/json;");
                                    params.put("Authorization", "Bearer " + AccessAPI.getToken());
                                    return params;
                                }
                            };

                            assetDefaultRequestQueue.add(jsonObjectAssetDefaultRequest);
                            asset1RequestQueue.add(jsonObjectAssetRequest);
                            asset2RequestQueue.add(jsonObjectAsset2Request);
                            asset3RequestQueue.add(jsonObjectAsset3Request);
                            assetUserRequestQueue.add(jsonArrayAssetUser);
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

    private void ViewAssetDataUser() {
        AqiAssetFragment.arrayList = new ArrayList<>();
        AqiAssetFragment.arrayList.add(new ItemsDetail( getResources().getString(R.string.idAssetUser), idAssetUser + "", R.drawable.ic_id));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.fullName), userFullName + "", R.drawable.ic_full_name));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.assetName), assetName + "", R.drawable.ic_asset_name));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.parentAssetName), parentAssetName + "", R.drawable.ic_parent_asset_name));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.createdOn), formatDays2 + "", R.drawable.ic_created_on));

        bottomSheetDialog.dismiss();
        AqiAssetFragment weatherAssetFragment = new AqiAssetFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainActivity, weatherAssetFragment, "fragDetails");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private LatLng getLatLngForUser(int i) {
        switch (i) {
            case 0:
                return new LatLng(10.870537960015568, 106.80390988702072);
            case 1:
                return new LatLng(10.870464609989156, 106.80273973945748);
            case 2:
                return new LatLng(10.870167914043385, 106.80381878819092);
            case 3:
                return new LatLng(10.869504706653828, 106.80310165581015);
            default:
                return new LatLng(10.8698, 106.8031); // Giả sử một giá trị mặc định nếu không có trường hợp nào khớp
        }
    }

    private void ViewAssetData2() {
        AqiAssetFragment.arrayList = new ArrayList<>();
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.pm10), pm10 + "µg/m³", R.drawable.ic_pm10));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.pm25), pm25 + "µg/m³", R.drawable.ic_pm25));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.co2), C02 + "µg/m³", R.drawable.ic_co));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.humidity), humidity + "%", R.drawable.ic_humidity));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.aqi), aqi + "", R.drawable.ic_account));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.aqiPredict), aqiPredict + "", R.drawable.ic_account));

        bottomSheetDialog.dismiss();
        AqiAssetFragment weatherAssetFragment = new AqiAssetFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainActivity, weatherAssetFragment, "fragDetails");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void ViewAssetData1() {
        AqiAssetFragment.arrayList = new ArrayList<>();
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.temperature), temp + "°C", R.drawable.ic_temperature_color));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.humidity), humidity + "%", R.drawable.ic_humidity));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.pressure), pressure + "hPa", R.drawable.ic_pressure));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.titleSunrise), sunrise, R.drawable.icon_sunrise));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.titleSunset), sunset, R.drawable.ic_sunset));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.tempMax), temp_max + "°C", R.drawable.ic_temperature_high));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.tempMin), temp_min + "°C", R.drawable.ic_temperature_low));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.feelsLike), feels_like + "°C", R.drawable.ic_temperature_color));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.speed), speed + "m/s", R.drawable.ic_wind));
        AqiAssetFragment.arrayList.add(new ItemsDetail(getResources().getString(R.string.cloud), all + "%", R.drawable.ic_cloud));

        bottomSheetDialog.dismiss();
        AqiAssetFragment weatherAssetFragment = new AqiAssetFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainActivity, weatherAssetFragment, "fragDetails");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}