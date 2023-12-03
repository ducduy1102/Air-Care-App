package com.example.aircareapp.View;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.aircareapp.APICLient.APIClient;
import com.example.aircareapp.APIService.APIService;
import com.example.aircareapp.Model.User;
import com.example.aircareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 101;
    private View view;
    private EditText profileEmail, profileFirstName, profileLastName;
    private TextView profileUserName;
    private Button btnUpdateProfile;
    private ImageView profileAvatar, imgBackSetting;
    private ProgressDialog progressDialog;

    private ProgressBar loadingProgressBar;

    private APIService apiService;

    private WebView webView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        initUi();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dataLogin", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token", "");
            Log.d("tokenProfile", token);


            apiService = APIClient.getClient("https://uiot.ixxc.dev/api/master/user/", token).create(APIService.class);
            // Make the API call
            Call<User> call = apiService.getUser();
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User data = response.body();
//                        profileFullName.setText(data.getFirstName()+data.getLastName());
                        String firstname = data.getFirstName();
                        String lastname = data.getLastName();

                        profileUserName.setText(data.getUsername());
                        profileEmail.setText(data.getEmail());

                        if (firstname == null) {
                            profileFirstName.setText("First Name");
                        } else {
                            profileFirstName.setText(firstname);
                        }
                        if (lastname == null) {
                            profileLastName.setText("Last Name");
                        } else {
                            profileLastName.setText(lastname);
                        }

                    } else {
                        Toast.makeText(getContext(), "Error" + response.errorBody(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        } else {
            showUserInformation();
        }

        initListener();


    }

    private void initListener() {

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();
            }
        });

        imgBackSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                ProfileFragment profileFragment = (ProfileFragment) getFragmentManager().findFragmentByTag("fragProfile");
                transaction.remove(profileFragment);
                transaction.replace(R.id.mainActivity, new SettingFragment());
                transaction.commit();
            }
        });
    }

    private void onClickUpdateProfile() {
        if (!validateEmail()) {

        } else {
            loadingProgressBar.setVisibility(View.VISIBLE);
            String username = profileUserName.getText().toString().trim();
            String email = profileEmail.getText().toString().trim();
            String firstname = profileFirstName.getText().toString().trim();
            String lastname = profileLastName.getText().toString().trim();
//                    onClickSignUp();
            loadWebView(username, email, firstname, lastname);
        }
    }

//    private void onClickUpdateProfile() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        if(user == null) {
//            return;
//        }
//        progressDialog.setTitle("Update Profile");
//        progressDialog.setMessage("Please wait...");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//        String profileFullNameUpdate = profileUserName.getText().toString().trim();
//
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(profileFullNameUpdate)
//                .build();
//
//        user.updateProfile(profileUpdates)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        progressDialog.dismiss();
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getContext(), "Update profile success", Toast.LENGTH_SHORT).show();
//                            showUserInformation();
//                        }
//                    }
//                });
//    }

    private void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if (name == null) {
            profileUserName.setText("");
        } else {
            profileUserName.setText(name);
        }
        profileEmail.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.avatar_default).into(profileAvatar);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebView(String user, String email, String firstname, String lastname) {
        String myUrl = "https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/auth?client_id=openremote&redirect_uri=https%3A%2F%2Fuiot.ixxc.dev%2Fmanager%2F&state=c19f9e53-887b-46fb-829a-13e3413ac12b&response_mode=fragment&response_type=code&scope=openid&nonce=0147389d-31a1-447b-9083-1f5b447e8d41";
//        String myUrl ="https://uiot.ixxc.dev/auth/realms/master/account/";
//        String myUrl ="https://uiot.ixxc.dev/manager/#/account";
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.removeAllCookies(null);

        if (webView == null) {
            // Khởi tạo WebView và gán giá trị
            webView = new WebView(getContext());
        }

        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setJavaScriptEnabled(true);
        if (webSettings != null) {
            webView.loadUrl(myUrl);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    Log.d("Url1", request.getUrl().toString());
                    if (request.getUrl().toString().contains("manager/#account")) {
//                        Log.d("Url2", request.getUrl().toString());
//                            // Thực hiện JavaScript để chuyển hướng đến "/manager#account"
//                            String redirect = "window.location.href = 'https://uiot.ixxc.dev/manager/#/account';";
//                            view.evaluateJavascript(redirect, null);
////                            webView.getSettings().setJavaScriptEnabled(true);
                        return true;
                    }
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
//                    super.onPageFinished(view, "https://uiot.ixxc.dev/auth/realms/master/account/");
//                    super.onPageFinished(view, "https://uiot.ixxc.dev/manager/#/account");
                    super.onPageFinished(view, url);
//                    if (url.contains("/manager/#state")) {
//                        // Chuyển hướng đến "/manager#/account"
//                        String newUrl = "https://uiot.ixxc.dev/manager/#/account";
//                        view.loadUrl(newUrl);
//                    } else {
//
//                    }
//                    Log.d("onPageFinished", "Page finished loading: " + url);
//                    webView.setVisibility(View.GONE);
//                    String helperText = "document.getElementsByClassName('helper-text')[0].getAttribute('data-error');";
//                    String redText = "document.getElementsByClassName('red-text')[1].innerText;";
//                    view.evaluateJavascript(helperText, s -> {
//                        Log.d("JavaScriptResult 1", "Result: " + s);
//                        if (s.equals("null")) {
//                            view.evaluateJavascript(redText, s1 -> {
//                                Log.d("JavaScriptResult1", "Result: " + s1);
//                                if (s1.equals("null")) {
//                                    String userField = "document.getElementById('username').value='" + user + "';";
//                                    String emailField = "document.getElementById('email').value='" + email + "';";
//                                    String firstNameField = "document.getElementById('firstName').value='" + firstname + "';";
//                                    String lastNameField = "document.getElementById('lastName').value='" + lastname + "';";
//                                    view.evaluateJavascript(userField, null);
//                                    view.evaluateJavascript(emailField, null);
//                                    view.evaluateJavascript(firstNameField, null);
//                                    view.evaluateJavascript(lastNameField, null);
////                                    view.evaluateJavascript("document.getElementById('stateChecker').value", value -> {
////                                        // Xử lý giá trị stateChecker ở đây
////                                        if (value != null && !value.isEmpty()) {
////                                            String stateChecker = value.replaceAll("\"", ""); // Loại bỏ dấu ngoặc kép nếu có
////                                            // Sử dụng giá trị stateChecker khi gửi dữ liệu
////                                            String postData = null;
////                                            try {
////                                                postData = "username=" + URLEncoder.encode("user88", "UTF-8") +
////                                                        "&email=" + URLEncoder.encode("user88@gmail.com", "UTF-8") +
////                                                        "&firstName=" + URLEncoder.encode("John", "UTF-8") +
////                                                        "&lastName=" + URLEncoder.encode("Doe", "UTF-8") +
////                                                        "&stateChecker=" + URLEncoder.encode(stateChecker, "UTF-8");
////
////                                                view.evaluateJavascript("document.getElementsByTagName('submitAction')[0].submit();", null);
////                                                view.postUrl("https://uiot.ixxc.dev/auth/realms/master/#/account/", postData.getBytes());
////                                            } catch (UnsupportedEncodingException e) {
////                                                throw new RuntimeException(e);
////                                            }} else {
////                                            // Xử lý khi không thể lấy giá trị stateChecker
////                                        }
////                                    });
//
//                                    view.evaluateJavascript("document.getElementsByTagName('submitAction')[0].submit();", null);
//                                    loadingProgressBar.setVisibility(View.GONE);
//                                    Toast.makeText(getActivity(), "Update Successfully", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    loadingProgressBar.setVisibility(View.GONE);
//                                    Toast.makeText(getActivity(), "Update Failed" + s1, Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        } else {
//                            loadingProgressBar.setVisibility(View.GONE);
//                            Toast.makeText(getActivity(), "Singup Failed" + s, Toast.LENGTH_SHORT).show();
//                        }
//                    });


                    if (url.contains("openid-connect/auth")) {
                        String redirect = "document.getElementsByClassName('btn waves-effect waves-light')[1].click();";
                        view.evaluateJavascript(redirect, null);
                    } else if (url.contains("/manager/#state")) {
                        // Chuyển hướng đến "/manager#/account"
                        String newUrl = "https://uiot.ixxc.dev/manager/#/account";
                        view.loadUrl(newUrl);
//                    boolean shouldStopEvaluation = false;
                        String helperText = "document.getElementsByClassName('helper-text')[0].getAttribute('data-error');";
                        String redText = "document.getElementsByClassName('red-text')[1].innerText;";
                        view.evaluateJavascript(helperText, s -> {
                            Log.d("JavaScriptResult", "Result: " + s);
                            if (s.equals("null")) {
                                view.evaluateJavascript(redText, s1 -> {
                                    Log.d("JavaScriptResult1", "Result: " + s1);
                                    if (s1.equals("null")) {
                                        String userField = "document.getElementById('username').value='" + user + "';";
                                        String emailField = "document.getElementById('email').value='" + email + "';";
                                        String firstNameField = "document.getElementById('firstName').value='" + firstname + "';";
                                        String lastNameField = "document.getElementById('lastName').value='" + lastname + "';";
                                        view.evaluateJavascript(userField, null);
                                        view.evaluateJavascript(emailField, null);
                                        view.evaluateJavascript(firstNameField, null);
                                        view.evaluateJavascript(lastNameField, null);
//                                    view.evaluateJavascript("document.getElementsByTagName('submitAction')[0].submit();", null);
                                        view.evaluateJavascript("document.getElementsByTagName('form')[0].submit();", null);
                                        loadingProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "Update Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        loadingProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "Update Failed" + s1, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                loadingProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Singup Failed" + s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
            webView.setWebChromeClient(new WebChromeClient());
        } else {
            // Xử lý trường hợp webSettings là null (có thể xảy ra trong một số tình huống)
            Toast.makeText(getActivity(), "Failed to load WebView. Please try again.", Toast.LENGTH_SHORT).show();
        }
//        webView.stopLoading();
    }

    public Boolean validateEmail() {
        String valEmail = profileEmail.getText().toString().trim();
        if (valEmail.isEmpty()) {
            profileEmail.setError("Email cannot be empty");
            profileEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(valEmail).matches()) {
            profileEmail.setError("Email is invalid");
            profileEmail.requestFocus();
            return false;
        } else {
            profileEmail.setError(null);
            return true;
        }
    }

    private void initUi() {
        profileUserName = view.findViewById(R.id.profileUserName);
        profileFirstName = view.findViewById(R.id.profileFirstName);
        profileLastName = view.findViewById(R.id.profileLastName);
        profileEmail = view.findViewById(R.id.profileEmail);
        btnUpdateProfile = view.findViewById(R.id.buttonUpdateProfile);
        profileAvatar = view.findViewById(R.id.profileImg);
        imgBackSetting = view.findViewById(R.id.imgBackProfile);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
    }
}