package com.example.aircareapp.View;

import static com.example.aircareapp.SSLHandle.SSLHandle.handleSSLHandshake;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.aircareapp.APIService.APIService;
import com.example.aircareapp.AccessAPI.AccessAPI;
import com.example.aircareapp.MainActivity;
import com.example.aircareapp.Model.LoginResponse;
import com.example.aircareapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_GOOGLE = 100;
    private FirebaseAuth auth;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private TextInputLayout loginUsernameInput, loginUPasswordInput;
    private TextInputEditText loginUsername, loginPassword;
    private Button btnLogin, buttonForgotPassword, buttonLoginGoogle;
    private ImageButton imgLoginWithGoogle;
    private CheckBox cbRemember;
    private TextView registerRedirectText, textViewForgotPassword;

    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;

    private ProgressBar loadingProgressBar;

    private RequestQueue mRequestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private JSONArray jsonArrayWeather;
    private JSONObject weatherDescription, jsonObjectAttributes, jsonObjectRequestQueryParameters, jsonObjectValue, jsonObjectMain, jsonObjectWind, jsonObjectSys,
            jsonObjectCloud, jsonObjectWeatherData;
    private int humidity, pressure;
    private long sunriseTimestamp, sunsetTimestamp;
    private double temp, temp_max, temp_min, feels_like, speed, all;

    NotificationManagerCompat notificationManagerCompat;
    Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();
//        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        // Lấy giá trị sharedPreferences
        loginUsername.setText(sharedPreferences.getString("prefUsername", ""));
        loginPassword.setText(sharedPreferences.getString("prefPassword", ""));
        cbRemember.setChecked(sharedPreferences.getBoolean("prefChecked", false));

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        initListener();

        // Bắt đầu code get api
        handleSSLHandshake();
        mRequestQueue = Volley.newRequestQueue(LoginActivity.this);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AccessAPI.getUrlAsset1(), null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("notification", "onResponse: " + response);
                        try {
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

                            temp = jsonObjectMain.getDouble("temp");
                            humidity = jsonObjectMain.getInt("humidity");
                            feels_like = jsonObjectMain.getDouble("feels_like");
                            speed = jsonObjectWind.getDouble("speed");
                            all = jsonObjectCloud.getDouble("all");
                            sunriseTimestamp = jsonObjectSys.getLong("sunrise");
                            sunsetTimestamp = jsonObjectSys.getLong("sunset");
                            weatherDescription = new JSONObject(String.valueOf(jsonArrayWeather.get(0)));

                            String description = weatherDescription.getString("description");
                            // khúc này là để t viết hoa chữ cái đầu cho cái description
                            String firstLetter = description.substring(0, 1);
                            String remainingLetters = description.substring(1, description.length());
                            firstLetter = firstLetter.toUpperCase();
                            description = firstLetter + remainingLetters;

                            NotificationChannel channel = new NotificationChannel("MyAirApp", "MyAirApp", NotificationManager.IMPORTANCE_DEFAULT);
                            NotificationManager manager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                            manager.createNotificationChannel(channel);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(LoginActivity.this, "MyAirApp")
                                    .setSmallIcon(R.drawable.green_bg)
                                    .setContentTitle(temp + "° in UIT")
                                    .setContentText("Temp " + temp + "° • " + description);
                            manager.notify(1, builder.build());
                            Log.d("notificationDEs", "onResponse: " + description);

                            notification = builder.build();
                            notificationManagerCompat = NotificationManagerCompat.from(LoginActivity.this);
                            // Kết thúc code gửi thông báo

                        } catch (JSONException e) {
                            Log.e("MyError", "" + e);
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MyError", "" + error);
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
        mRequestQueue.add(jsonObjectRequest);
    }

    private void initListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onClickLogIn();
                loadingProgressBar.setVisibility(View.VISIBLE);
                login();
                RememberAccount();
            }
        });

        registerRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        buttonLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInWithGoogle();
            }
        });
    }

    private void RememberAccount() {
        String username = loginUsername.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        //Nếu có check
        if (cbRemember.isChecked()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("prefUsername", username);
            editor.putString("prefPassword", password);
            editor.putBoolean("prefChecked", true);
            editor.commit();
        } else {
            // TH ko muốn nhớ nữa
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("prefUsername");
            editor.remove("prefPassword");
            editor.remove("prefChecked");
            editor.commit();
        }
    }

    private void login() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/") // Replace with the base URL of your API
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService authService = retrofit.create(APIService.class);
        String username = loginUsername.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();
        Call<LoginResponse> call = authService.login("openremote", username, password, "password");
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    String accessToken = loginResponse.getAccess_token();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", accessToken);
//                    editor.commit();
                    editor.apply();
                    Log.d("tokenLogin", accessToken + username);
                    loadingProgressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Authentication failed
                    loadingProgressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Handle network or API errors
                Log.d("API CALL", t.getMessage().toString());
                Toast.makeText(LoginActivity.this, "Login Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void LogInWithGoogle() {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, REQUEST_CODE_GOOGLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public Boolean validateUserName() {
        String valEmail = loginUsername.getText().toString().trim();
        if (valEmail.isEmpty()) {
            loginUsername.setError("Email cannot be empty");
            loginUsername.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(valEmail).matches()) {
            loginUsername.setError("Email is invalid");
            loginUsername.requestFocus();
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    private void initUi() {
        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);
        cbRemember = findViewById(R.id.checkBoxRemember);
        loginUsernameInput = findViewById(R.id.loginUsernameInput);
        loginUPasswordInput = findViewById(R.id.loginPasswordInput);
        registerRedirectText = findViewById(R.id.registerRedirectText);
        buttonForgotPassword = findViewById(R.id.buttonForgotPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        buttonLoginGoogle = findViewById(R.id.buttonLoginGoogle);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
    }
}