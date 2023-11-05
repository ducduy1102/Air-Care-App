package com.example.aircareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.aircareapp.Adapter.ViewPageAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private ViewPager mViewPager;
    private BroadcastReceiver broadcastReceiver;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        broadcastReceiver = new BroadcastReceiver();

        mViewPager = findViewById(R.id.viewPager);
        bottomNav = findViewById(R.id.bottomNavigationView);

        setUpViewPager();

        //set các lựa chọn fragment vô các vị trí
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.action_home){
                    mViewPager.setCurrentItem(0);
//                    getSupportActionBar().setTitle("Home");
                } else if(id == R.id.action_map){
                    mViewPager.setCurrentItem(1);
//                    getSupportActionBar().setTitle("Map");
                } else if(id == R.id.action_history){
                    mViewPager.setCurrentItem(2);
//                    getSupportActionBar().setTitle("Profile");
                } else if(id == R.id.action_setting){
                    mViewPager.setCurrentItem(3);
//                    getSupportActionBar().setTitle("Profile");
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(broadcastReceiver);
    }

//     setup fragment vô các lựa chọn bằng Adapter
    private void setUpViewPager () {
        ViewPageAdapter viewPagerAdapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerAdapter);

        // hiệu ứng kéo các fragment để các menu bên dưới có thể chuyển đổi theo
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // set  các fragment vào đúng các con số menu để hiệu ứng chuyển đổi diễn ra đúng
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNav.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                    case 1:
                        bottomNav.getMenu().findItem(R.id.action_map).setChecked(true);
                        break;
                    case 2:
                        bottomNav.getMenu().findItem(R.id.action_history).setChecked(true);
                        break;
                    case 3:
                        bottomNav.getMenu().findItem(R.id.action_setting).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }
}