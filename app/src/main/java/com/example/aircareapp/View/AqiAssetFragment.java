package com.example.aircareapp.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.aircareapp.Adapter.DetailAssetAdapter;
import com.example.aircareapp.Model.ItemsDetail;
import com.example.aircareapp.R;

import java.util.ArrayList;

public class AqiAssetFragment extends Fragment {

    ListView listView;
    static ArrayList<ItemsDetail> arrayList;
    DetailAssetAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_aqi_asset, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.lvItemDetail);


        adapter = new DetailAssetAdapter(getActivity(), R.layout.custom_list_asset_current, arrayList);
        listView.setAdapter(adapter);
    }

}