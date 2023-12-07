package com.example.aircareapp.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aircareapp.MainActivity;
import com.example.aircareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputEditText registerUsername, registerEmail, registerPassword, registerConfirmPassword;
    private TextInputLayout registerPasswordLayout;
    private Button btnRegister;
    private ImageView imgBackLogin;
    //    private ProgressDialog progressDialog;
    private WebView webView;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        progressDialog = new ProgressDialog(this);
        initUi();

        auth = FirebaseAuth.getInstance();

        initListener();

    }

    private void initListener() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUsername()) {

                } else if (!validateEmail()) {

                } else if (!validatePassword()) {

                } else if (!validateConfirmPassword()) {

                } else {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    String username = registerUsername.getText().toString().trim();
                    String email = registerEmail.getText().toString().trim();
                    String pass = registerPassword.getText().toString().trim();
                    String confirmpass = registerConfirmPassword.getText().toString().trim();
//                    onClickSignUp();
                    loadWebView(username, email, pass, confirmpass);
                }
            }
        });

        // dùng addTextChangListener xem video https://www.youtube.com/watch?v=q-DnUKbGsgA

        registerPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pass = s.toString();
                if (pass.length() > 6) {
                    Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher matcher = pattern.matcher(pass);
                    boolean isPassContainsSpeChar = matcher.find();
                    if (isPassContainsSpeChar) {
                        registerPasswordLayout.setHelperText(getResources().getString(R.string.strongPass));
                        registerPasswordLayout.setHelperTextColor(ColorStateList.valueOf(Color.GREEN));
                        registerPasswordLayout.setError("");
                    } else {
                        registerPasswordLayout.setHelperText("");
                        registerPasswordLayout.setHelperTextColor(ColorStateList.valueOf(Color.YELLOW));
                        registerPasswordLayout.setError(getResources().getString(R.string.weakPass));
                    }
                } else {
                    registerPasswordLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                    registerPasswordLayout.setHelperText(getResources().getString(R.string.invalidPass));
                    registerPasswordLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void onClickSignUp() {
        String email = registerEmail.getText().toString();
        String pass = registerPassword.getText().toString();

//        progressDialog.setTitle("SignUp");
//        progressDialog.setMessage("Please wait...");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                progressDialog.dismiss();
                loadingProgressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.singupSuccess), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finishAffinity();
                } else {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.signupFailed) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebView(String user, String email, String pass, String repass) {
        String myUrl ="https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/auth?client_id=openremote&redirect_uri=https%3A%2F%2Fuiot.ixxc.dev%2Fmanager%2F&state=c19f9e53-887b-46fb-829a-13e3413ac12b&response_mode=fragment&response_type=code&scope=openid&nonce=0147389d-31a1-447b-9083-1f5b447e8d41";
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(null);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(myUrl);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d("Url1", request.getUrl().toString());
                if (request.getUrl().toString().contains("manager/#state")) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                return null;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setVisibility(View.GONE);
                if (url.contains("openid-connect/auth")) {
                    String redirect = "document.getElementsByClassName('btn waves-effect waves-light')[1].click();";
                    view.evaluateJavascript(redirect, null);
                } else if (url.contains("login-actions/registration")) {
//                    boolean shouldStopEvaluation = false;
                    String helperText = "document.getElementsByClassName('helper-text')[0].getAttribute('data-error');";
                    String redText = "document.getElementsByClassName('red-text')[1].innerText;";
                    view.evaluateJavascript(helperText, s -> {
                        Log.d("JavaScriptResult 1", "Result: " + s);
                        if (s.equals("null")) {
                            view.evaluateJavascript(redText, s1 -> {
                                Log.d("JavaScriptResult 1", "Result: " + s);
                                if (s1.equals("null")) {
                                    String userField = "document.getElementById('username').value='" + user + "';";
                                    String emailField = "document.getElementById('email').value='" + email + "';";
                                    String passField = "document.getElementById('password').value='" + pass + "';";
                                    String repassField = "document.getElementById('password-confirm').value='" + repass + "';";
                                    view.evaluateJavascript(userField, null);
                                    view.evaluateJavascript(emailField, null);
                                    view.evaluateJavascript(passField, null);
                                    view.evaluateJavascript(repassField, null);
                                    view.evaluateJavascript("document.getElementsByTagName('form')[0].submit();", null);
                                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.singupSuccess), Toast.LENGTH_SHORT).show();
                                } else {
                                    loadingProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.signupFailed) + s1, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            loadingProgressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.signupFailed) + s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
    }

    public Boolean validateUsername() {
        String valUsername = registerUsername.getText().toString().trim();
        if (valUsername.isEmpty()) {
            registerUsername.setError(getResources().getString(R.string.emptyUsername));
            registerUsername.requestFocus();
            return false;
        } else {
            registerUsername.setError(null);
            return true;
        }
    }

    public Boolean validateEmail() {
        String valEmail = registerEmail.getText().toString().trim();
        if (valEmail.isEmpty()) {
            registerEmail.setError(getResources().getString(R.string.emptyEmail));
            registerEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(valEmail).matches()) {
            registerEmail.setError(getResources().getString(R.string.invalidEmail));
            registerEmail.requestFocus();
            return false;
        } else {
            registerEmail.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String valPassword = registerPassword.getText().toString().trim();
        if (valPassword.isEmpty()) {
            registerPassword.setError(getResources().getString(R.string.emptyPass));
            registerPassword.requestFocus();
            return false;
        } else {
            registerPassword.setError(null);
            return true;
        }
    }

    private Boolean validateConfirmPassword() {
        String valPassword = registerPassword.getText().toString().trim();
        String valConfirmPassword = registerConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(valConfirmPassword)) {
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.notiConfirmPass), Toast.LENGTH_SHORT).show();
            registerConfirmPassword.setError(getResources().getString(R.string.notiRequiredPass));
            registerConfirmPassword.requestFocus();
            return false;
        } else if (!valConfirmPassword.equals(valPassword)) {
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.notiCorrectPass), Toast.LENGTH_SHORT).show();
            registerConfirmPassword.setError(getResources().getString(R.string.notiIncorrectPass));
            registerConfirmPassword.requestFocus();
            return false;
        } else {
            registerConfirmPassword.setError(null);
            return true;
        }
    }

    private void initUi() {
        registerUsername = findViewById(R.id.registerUsername);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfirmPassword = findViewById(R.id.registerConfirmPassword);
        btnRegister = findViewById(R.id.buttonRegister);
        imgBackLogin = findViewById(R.id.imageViewBackLogin);
        registerPasswordLayout = findViewById(R.id.registerPasswordLayout);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        webView = findViewById(R.id.webView);
    }
}