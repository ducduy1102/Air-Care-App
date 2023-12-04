package com.example.aircareapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.aircareapp.View.HumidityStatictisFragment;
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
                return new HumidityStatictisFragment();
            case 2:
                return new WindStatisticFragment();
            default:
                return new TempStatisticFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
