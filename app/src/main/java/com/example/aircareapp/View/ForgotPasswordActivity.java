package com.example.aircareapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.aircareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText emailVerify;
    private Button btnSendEmail, btnForgotBackLogin;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailVerify = findViewById(R.id.emailVerify);
        btnSendEmail = findViewById(R.id.buttonSend);
        btnForgotBackLogin = findViewById(R.id.buttonForgotBackLogin);

//        progressbarforgetPassword = findViewById(R.id.progressbarforgetPassword);
//        progressbarforgetPassword.setVisibility(View.GONE);
        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressbarforgetPassword.setVisibility(View.VISIBLE);
                if(!validateEmail()){

                } else {
                    ResetPassword();
                }
            }
        });

        btnForgotBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void ResetPassword() {
        progressDialog.setTitle("Reset password");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        String userEmail = emailVerify.getText().toString().trim();
        auth.sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    public Boolean validateEmail(){
        String valEmail = emailVerify.getText().toString();
        if(valEmail.isEmpty()){
            Toast.makeText(ForgotPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            emailVerify.setError("Email cannot be empty");
            emailVerify.requestFocus();
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(valEmail).matches()) {
            Toast.makeText(ForgotPasswordActivity.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
            emailVerify.setError("Email is invalid");
            emailVerify.requestFocus();
            return false;
        } else {
            emailVerify.setError(null);
            return true;
        }
    }
}