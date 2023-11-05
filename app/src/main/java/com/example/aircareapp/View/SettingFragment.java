package com.example.aircareapp.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aircareapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class SettingFragment extends Fragment {

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(getActivity(), gso);

        initListener();

        return view;
    }

    private void initListener() {
        icArrowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.settingFragment, profileFragment);
                transaction.addToBackStack(null);
                transaction.commit();
//                ((MainActivity) getActivity()).setActionBarTitle("Profile");
            }
        });

        tvSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // logout GoogleAccount
                gsc.signOut();

                // Logout UserAccount
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getActivity(), "Log out Successfull", Toast.LENGTH_SHORT).show();
            }
        });

        icArrowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.settingFragment, changePasswordFragment);
                transaction.addToBackStack(null);
                transaction.commit();
//                if(R.id.settingFragment != null) {
//                    fragmentTransaction.remove(fragmentA);
//                    fragmentTransaction.commit();
//                } else {
//                    Toast.makeText(this, "Fragment A khong ton tai!", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    private void initUi() {

        icArrowProfile = view.findViewById(R.id.ic_arrow_forward_profile);
        icArrowPassword = view.findViewById(R.id.ic_arrow_forward_password);
        icArrowLanguage = view.findViewById(R.id.ic_arrow_forward_language);
        icToggleMode = view.findViewById(R.id.ic_arrow_forward_dark_mode);
        icArrowNoti = view.findViewById(R.id.ic_arrow_forward_noti);
        icArrowAbout = view.findViewById(R.id.ic_arrow_forward_about_us);
        icArrowHelp = view.findViewById(R.id.ic_arrow_forward_help);

        tvProfile = view.findViewById(R.id.tv_profile);
        tvPassword = view.findViewById(R.id.tv_password);
        tvSignout = view.findViewById(R.id.tv_signout);
        tvLanguage = view.findViewById(R.id.tv_language);
        tvMode = view.findViewById(R.id.tv_mode);
        tvNoti = view.findViewById(R.id.tv_notification);
        tvAbout = view.findViewById(R.id.tv_support);
        tvHelp = view.findViewById(R.id.tv_help);

    }
}