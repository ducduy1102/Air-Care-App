package com.example.aircareapp.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.aircareapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class SettingFragment extends Fragment {

    SwitchCompat switchMode;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private View view;
    private TextView tvProfile, tvPassword, tvSignout, tvLanguage, tvMode, tvNoti, tvAbout, tvHelp;
    private ImageView icArrowProfile, icArrowPassword, icArrowSignout, icArrowLanguage, icToggleMode, icArrowNoti, icArrowAbout, icArrowHelp;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        initUi();

        sharedPreferences = getActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("nightMode", false);
        switchMode.setChecked(false);

        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nightMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightMode", false);
                    switchMode.setChecked(true);
                    nightMode = false;
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightMode", true);
                    switchMode.setChecked(false);
                    nightMode = true;
                }
                editor.apply();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(getActivity(), gso);

        sharedPreferences = getActivity().getSharedPreferences("dataLogin", Context.MODE_PRIVATE);

        initListener();

    }

    private void initListener() {
        icArrowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.mainActivity, profileFragment, "fragProfile");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        icArrowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.mainActivity, changePasswordFragment, "fragChangePassword");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        tvSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // logout GoogleAccount
                gsc.signOut();

                // Logout UserAccount
                FirebaseAuth.getInstance().signOut();

//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.remove("token");
//                editor.commit();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getActivity(), getResources().getString(R.string.logoutSuccess), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUi() {

        icArrowProfile = view.findViewById(R.id.ic_arrow_forward_profile);
        icArrowPassword = view.findViewById(R.id.ic_arrow_forward_password);
        icArrowLanguage = view.findViewById(R.id.ic_arrow_forward_language);
        switchMode = view.findViewById(R.id.switchMode);

        tvProfile = view.findViewById(R.id.tv_profile);
        tvPassword = view.findViewById(R.id.tv_password);
        tvSignout = view.findViewById(R.id.tv_signout);
        tvLanguage = view.findViewById(R.id.tv_language);
        tvMode = view.findViewById(R.id.tv_mode);
    }
}