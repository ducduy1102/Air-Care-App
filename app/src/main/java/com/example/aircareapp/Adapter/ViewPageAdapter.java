package com.example.aircareapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.aircareapp.View.HistoryFragment;
import com.example.aircareapp.View.HomeFragment;
import com.example.aircareapp.View.MapFragment;
import com.example.aircareapp.View.SettingFragment;

public class ViewPageAdapter extends FragmentStatePagerAdapter {


    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new MapFragment();
            case 2:
                return new HistoryFragment();
            case 3:
                return new SettingFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
