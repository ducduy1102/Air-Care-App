package com.example.aircareapp.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.aircareapp.R;

public class DetailPlaceFragment extends Fragment {
    private View view;

    private ImageView imgBackHome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail_place, container, false);

        initUi();
        initListener();

        return view;
    }

    private void initListener() {
        imgBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                DetailPlaceFragment detailPlaceFragment = (DetailPlaceFragment) getFragmentManager().findFragmentByTag("fragDetailPlaceFragment");
                transaction.remove(detailPlaceFragment);
                transaction.replace(R.id.mainActivity, new HomeFragment());
                transaction.commit();
            }
        });
    }

    private void initUi() {
        imgBackHome = view.findViewById(R.id.icBackDetail);
    }
}