package com.example.aircareapp.View;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.aircareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {

    private Button btnPlace;
    private View view;
    private ImageView imgAvatar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        initUi();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showUserAvatar();
        initListener();
    }

    private void initListener() {
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailPlaceFragment detailPlaceFragment = new DetailPlaceFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.mainActivity, detailPlaceFragment,"fragDetailPlaceFragment");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void initUi() {
        imgAvatar = view.findViewById(R.id.imgAvatar);
        btnPlace = view.findViewById(R.id.buttonPlace);
    }

    private void showUserAvatar() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }
        Uri photoUrl = user.getPhotoUrl();
        Glide.with(this).load(photoUrl).error(R.drawable.avatar_default).into(imgAvatar);
    }
}