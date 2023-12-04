package com.example.aircareapp.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.aircareapp.Adapter.ViewPagerAdapterStatistic;
import com.example.aircareapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class StatisticFragment extends Fragment {

    private BottomNavigationView navigationView;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.viewPagerStatistic);
        navigationView = view.findViewById(R.id.bottomNavStatistic);

        setUpViewPager();

//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        navigationView = view.findViewById(R.id.bottomNavigationView);
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity, new HomeFragment()).commit();
//        navigationView.setSelectedItemId(R.id.action_temperature);

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//                switch(id){
//                    case R.id.action_temperature:
//                        viewPager.setCurrentItem(0);
//                        break;
//                    case R.id.action_humidity:
//                        viewPager.setCurrentItem(1);
//                        break;
//                    case R.id.action_windy:
//                        viewPager.setCurrentItem(2);
//                        break;
//                }
//                return true;

                int id = item.getItemId();
                Fragment fragment = null;
                if (id == R.id.action_temperature) {
                    fragment = new HomeFragment();
                } else if (id == R.id.action_humidity) {
                    fragment = new MapFragment();
                } else if (id == R.id.action_windy) {
                    fragment = new SettingFragment();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity, fragment).commit();
                return true;
            }
        });
    }

    private void setUpViewPager() {
        ViewPagerAdapterStatistic viewPagerAdapter = new ViewPagerAdapterStatistic(getActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // set  các fragment vào đúng các con số menu để hiệu ứng chuyển đổi diễn ra đúng
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navigationView.getMenu().findItem(R.id.action_temperature).setChecked(true);
                        break;
                    case 1:
                        navigationView.getMenu().findItem(R.id.action_humidity).setChecked(true);
                        break;
                    case 2:
                        navigationView.getMenu().findItem(R.id.action_windy).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}