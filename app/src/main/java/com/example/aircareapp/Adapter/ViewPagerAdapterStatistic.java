package com.example.aircareapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.aircareapp.View.HumidityStatisticFragment;
import com.example.aircareapp.View.Pm10StatisticFragment;
import com.example.aircareapp.View.Pm25StatisticFragment;
import com.example.aircareapp.View.TempStatisticFragment;
import com.example.aircareapp.View.WindStatisticFragment;

public class ViewPagerAdapterStatistic extends FragmentStatePagerAdapter {
    public ViewPagerAdapterStatistic(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    // trả về
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TempStatisticFragment();
            case 1:
                return new HumidityStatisticFragment();
            case 2:
                return new WindStatisticFragment();
            case 3:
                return new Pm10StatisticFragment();
            case 4:
                return new Pm25StatisticFragment();
            default:
                return new TempStatisticFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
