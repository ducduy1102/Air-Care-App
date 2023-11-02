package com.example.aircareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.Toast;

import com.example.aircareapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputEditText registerEmail, registerPassword, registerConfirmPassword;
    private TextInputLayout registerPasswordLayout;
    private Button btnRegister, btnBackLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        initUi();

        auth = FirebaseAuth.getInstance();

        initListener();

    }

    private void initListener() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateEmail()){

                } else if(!validatePassword()) {

                }else if (!validateConfirmPassword()){

                } else {
                    onClickSignUp();
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
                if(pass.length() > 0) {
                    Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher matcher = pattern.matcher(pass);
                    boolean isPassContainsSpeChar = matcher.find();
                    if(isPassContainsSpeChar){
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
                    registerPasswordLayout.setHelperText("Enter minimum 8 characters");
                    registerPasswordLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnBackLogin.setOnClickListener(new View.OnClickListener() {
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

        progressDialog.setTitle("SignUp");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finishAffinity();
                } else {
                    Toast.makeText(RegisterActivity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public Boolean validateEmail(){
        String valEmail = registerEmail.getText().toString().trim();
        if(valEmail.isEmpty()){
            registerEmail.setError("Email cannot be empty");
            registerEmail.requestFocus();
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(valEmail).matches()){
            registerEmail.setError("Email is invalid");
            registerEmail.requestFocus();
            return false;
        }
        else {
            registerEmail.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String valPassword = registerPassword.getText().toString().trim();
        if(valPassword.isEmpty()){
            registerPassword.setError("Password cannot be empty");
            registerPassword.requestFocus();
            return false;
        } else {
            registerPassword.setError(null);
            return true;
        }
    }

    private Boolean validateConfirmPassword(){
        String valPassword = registerPassword.getText().toString().trim();
        String valConfirmPassword = registerConfirmPassword.getText().toString().trim();
        if(TextUtils.isEmpty(valConfirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Confirm your password", Toast.LENGTH_SHORT).show();
            registerConfirmPassword.setError("Passsword confirmation is required");
            registerConfirmPassword.requestFocus();
            return false;
        }else if(!valConfirmPassword.equals(valPassword)) {
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
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfirmPassword = findViewById(R.id.registerConfirmPassword);
        btnRegister = findViewById(R.id.buttonRegister);
        btnBackLogin = findViewById(R.id.buttonBackLogin);
        registerPasswordLayout = findViewById(R.id.registerPasswordLayout);
    }
}