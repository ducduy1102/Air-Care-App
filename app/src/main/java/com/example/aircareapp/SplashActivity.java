package com.example.aircareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    ImageView imgIconApp;
    TextView txtFirstLetter, txtSecondLetter, txtThirdLetter, txtFourthLetter, txtFifthLetter, txtSixthLetter, txtSeventhLetter;
    Animation animTop, animFirstLetter, animSecondLetter, animThirdLetter, animFourthLetter, animFifthLetter, animSixthLetter, animSeventhLetter;

    private static int SPLASH_SCREEN = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        imgIconApp = findViewById(R.id.imgIconApp);
        txtFirstLetter = findViewById(R.id.txtFirstLetter);
        txtSecondLetter = findViewById(R.id.txtSecondLetter);
        txtThirdLetter = findViewById(R.id.txtThirdLetter);
        txtFourthLetter = findViewById(R.id.txtFourthLetter);
        txtFifthLetter = findViewById(R.id.txtFifthLetter);
        txtSixthLetter = findViewById(R.id.txtSixthLetter);
        txtSeventhLetter = findViewById(R.id.txtSeventhLetter);

        animTop = AnimationUtils.loadAnimation(this, R.anim.top);
        animFirstLetter = AnimationUtils.loadAnimation(this, R.anim.slide_first_letter);
        animSecondLetter = AnimationUtils.loadAnimation(this, R.anim.slide_second_letter);
        animThirdLetter = AnimationUtils.loadAnimation(this, R.anim.slide_third_letter);
        animFourthLetter = AnimationUtils.loadAnimation(this, R.anim.slide_fourth_letter);
        animFifthLetter = AnimationUtils.loadAnimation(this, R.anim.slide_fifth_letter);
        animSixthLetter = AnimationUtils.loadAnimation(this, R.anim.slide_sixth_letter);
        animSeventhLetter = AnimationUtils.loadAnimation(this, R.anim.slide_seventh_letter);


        imgIconApp.setAnimation(animTop);
        txtFirstLetter.setAnimation(animFirstLetter);
        txtSecondLetter.setAnimation(animSecondLetter);
        txtThirdLetter.setAnimation(animThirdLetter);
        txtFourthLetter.setAnimation(animFourthLetter);
        txtFifthLetter.setAnimation(animFifthLetter);
        txtSixthLetter.setAnimation(animSixthLetter);
        txtSeventhLetter.setAnimation(animSeventhLetter);

        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                nextActivity();
//                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
            }
        }, SPLASH_SCREEN);
    }

    private void nextActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // check user da login chua
        if(user == null) {
            // chua login
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        } else {
            // da login
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
}