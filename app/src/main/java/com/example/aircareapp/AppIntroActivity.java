package com.example.aircareapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.aircareapp.View.LoginActivity;
import com.github.appintro.AppIntroCustomLayoutFragment;

public class AppIntroActivity extends com.github.appintro.AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.fragment_slide1));
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.fragment_slide2));
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.fragment_slide3));
    }

    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDonePressed(@Nullable Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}