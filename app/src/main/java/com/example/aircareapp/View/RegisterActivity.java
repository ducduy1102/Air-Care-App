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
                        registerPasswordLayout.setHelperText("Strong Password");
                        registerPasswordLayout.setHelperTextColor(ColorStateList.valueOf(Color.GREEN));
                        registerPasswordLayout.setError("");
                    } else {
                        registerPasswordLayout.setHelperText("");
                        registerPasswordLayout.setHelperTextColor(ColorStateList.valueOf(Color.YELLOW));
                        registerPasswordLayout.setError("Weak Password. Include minimum 1 special char");
                    }
                } else {
                    registerPasswordLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                    registerPasswordLayout.setHelperText("Enter minimum 6 characters");
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
                    Toast.makeText(RegisterActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finishAffinity();
                } else {
                    Toast.makeText(RegisterActivity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebView(String user, String email, String pass, String repass) {
        String myURL = "https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/auth?client_id=openremote&redirect_uri=https%3A%2F%2Fuiot.ixxc.dev%2Fmanager%2F&state=7b7ef2b3-64c3-4693-ba35-33e412d3c277&response_mode=fragment&response_type=code&scope=openid&nonce=c6011dc3-ac6e-46c3-9378-33fe07ab9bec";
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(null);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(myURL);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d("Url1", request.getUrl().toString());
                if (request.getUrl().toString().contains("manager/#state")) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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
                        if (s.equals("null")) {
                            view.evaluateJavascript(redText, s1 -> {
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
                                } else {
                                    loadingProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(RegisterActivity.this, "Singup Failed" + s1, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            loadingProgressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Singup Failed" + s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                webView.setVisibility(View.GONE);
//                if (url.contains("openid-connect/registrations")) {
//
//                    Log.d("ahihi", "onPageFinished: ");
//
//                    String userField = "document.getElementById('username').value='" + user + "';";
//                    String emailField = "document.getElementById('email').value='" + email + "';";
//                    String passField = "document.getElementById('password').value='" + pass + "';";
//                    String repassField = "document.getElementById('password-confirm').value='" + repass + "';";
//                    view.evaluateJavascript(userField, null);
//                    view.evaluateJavascript(emailField, null);
//                    view.evaluateJavascript(passField, null);
//                    view.evaluateJavascript(repassField, null);
//                    view.evaluateJavascript("document.getElementsByTagName('form')[0].submit();", null);
//                }
//            }
        });
        webView.setWebChromeClient(new WebChromeClient());
    }

    public Boolean validateUsername() {
        String valUsername = registerUsername.getText().toString().trim();
        if (valUsername.isEmpty()) {
            registerUsername.setError("Username cannot be empty");
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
            registerEmail.setError("Email cannot be empty");
            registerEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(valEmail).matches()) {
            registerEmail.setError("Email is invalid");
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
            registerPassword.setError("Password cannot be empty");
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
            Toast.makeText(RegisterActivity.this, "Confirm your password", Toast.LENGTH_SHORT).show();
            registerConfirmPassword.setError("Passsword confirmation is required");
            registerConfirmPassword.requestFocus();
            return false;
        } else if (!valConfirmPassword.equals(valPassword)) {
            Toast.makeText(RegisterActivity.this, "Please same same password", Toast.LENGTH_SHORT).show();
            registerConfirmPassword.setError("Passsword confirmation is incorrect");
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