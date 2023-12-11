package com.example.aircareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.aircareapp.Service.BroadcastReceiver;
import com.example.aircareapp.View.StatisticFragment;
import com.example.aircareapp.View.HomeFragment;
import com.example.aircareapp.View.MapFragment;
import com.example.aircareapp.View.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        broadcastReceiver = new BroadcastReceiver();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bottomNav = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity, new HomeFragment()).commit();
        bottomNav.setSelectedItemId(R.id.action_home);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment fragment = null;
                if(id == R.id.action_home){
                    fragment = new HomeFragment();
                } else if(id == R.id.action_map){
                    fragment = new MapFragment();
                } else if(id == R.id.action_statistic){
                    fragment = new StatisticFragment();
                } else if(id == R.id.action_setting){
                    fragment = new SettingFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity, fragment).commit();
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}