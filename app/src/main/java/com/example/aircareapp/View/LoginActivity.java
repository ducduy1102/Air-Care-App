package com.example.aircareapp.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aircareapp.MainActivity;
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

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
//    FirebaseDatabase database;
//    DatabaseReference reference;
    private static final int REQUEST_CODE_GOOGLE = 100;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();
        progressDialog = new ProgressDialog(this);

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
    }

    private void initListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogIn();
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

    private void onClickLogIn() {
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        String email = loginUsername.getText().toString().trim();
        String pass = loginPassword.getText().toString().trim();

        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (!pass.isEmpty()) {
                auth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finishAffinity();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                progressDialog.dismiss();
                loginPassword.setError("Password cannot empty");
            }
        } else if (email.isEmpty()) {
            progressDialog.dismiss();
            loginUsername.setError("Email cannot empty");
        } else {
            progressDialog.dismiss();
            loginUsername.setError("Please enter valid email");
        }
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

    public Boolean validateUserName(){
        String valEmail = loginUsername.getText().toString().trim();
        if(valEmail.isEmpty()){
            loginUsername.setError("Email cannot be empty");
            loginUsername.requestFocus();
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(valEmail).matches()){
            loginUsername.setError("Email is invalid");
            loginUsername.requestFocus();
            return false;
        }
        else {
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
    }
}