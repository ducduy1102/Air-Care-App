package com.example.aircareapp.View;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aircareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePasswordFragment extends Fragment {

    private View view;
    private TextInputEditText oldPassword, newPassword, confirmNewPassword;
    private TextInputLayout oldPasswordLayout, newPasswordLayout, confirmNewPasswordLayout;
    private TextView profileName;
    private ImageView imgAvatar, imgBackSetting;
    private Button buttonChangePassword;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_password, container, false);

        initUi();
        showUserInformation();
        progressDialog = new ProgressDialog(getContext());

        sharedPreferences = getActivity().getSharedPreferences("dataLogin", MODE_PRIVATE);

        initListener();
        return view;
    }

    private void initListener() {
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateOldPassword()){

                } else if(!validateNewPassword()) {

                }else if (!validateConfirmNewPassword()){

                } else {
                    onClickChangePassword();
                }
            }
        });

        // Check password weak / strong
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pass = s.toString();
                if(pass.length() > 0) {
                    Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher matcher = pattern.matcher(pass);
                    boolean isPassContainsSpeChar = matcher.find();
                    if(isPassContainsSpeChar){
                        newPasswordLayout.setHelperText("Strong Password");
                        newPasswordLayout.setHelperTextColor(ColorStateList.valueOf(Color.GREEN));
                        newPasswordLayout.setError("");
                    } else {
                        newPasswordLayout.setHelperText("");
                        newPasswordLayout.setHelperTextColor(ColorStateList.valueOf(Color.YELLOW));
                        newPasswordLayout.setError("Weak Password. Include minimum 1 special char");
                    }
                } else {
                    newPasswordLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                    newPasswordLayout.setHelperText("Enter minimum 8 characters");
                    newPasswordLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgBackSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingFragment settingFragment = new SettingFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.changePasswordFragment, settingFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }


    private void onClickChangePassword() {
        String newPassword = confirmNewPassword.getText().toString().trim();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog.setTitle("Update Password");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "User password updated.", Toast.LENGTH_SHORT).show();
                        } else {
                            // show dialog re-Authenticate
                            Toast.makeText(getContext(), "User password failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public Boolean validateOldPassword(){
        String valOldPassword = oldPassword.getText().toString().trim();
        if(valOldPassword.isEmpty()){
            oldPassword.setError("Password cannot be empty");
            oldPassword.requestFocus();
            return false;
        } else if(!valOldPassword.equals(sharedPreferences.getString("prefPassword", ""))) {
            Toast.makeText(getContext(), "Please enter your old password", Toast.LENGTH_SHORT).show();
            oldPassword.setError("Old password is incorrect");
            oldPassword.requestFocus();
            return false;}
        else {
            oldPassword.setError(null);
            return true;
        }
    }

    public Boolean validateNewPassword(){
        String valNewPassword = newPassword.getText().toString().trim();
        if(valNewPassword.isEmpty()){
            newPassword.setError("Password cannot be empty");
            newPassword.requestFocus();
            return false;
        } else {
            newPassword.setError(null);
            return true;
        }
    }

    private Boolean validateConfirmNewPassword(){
        String valNewPassword = newPassword.getText().toString().trim();
        String valConfirmNewPassword = confirmNewPassword.getText().toString().trim();
        if(TextUtils.isEmpty(valConfirmNewPassword)) {
            Toast.makeText(getContext(), "Confirm your password", Toast.LENGTH_SHORT).show();
            confirmNewPassword.setError("Passsword confirmation is required");
            confirmNewPassword.requestFocus();
            return false;
        }else if(!valConfirmNewPassword.equals(valNewPassword)) {
            Toast.makeText(getContext(), "Please same same password", Toast.LENGTH_SHORT).show();
            confirmNewPassword.setError("Passsword confirmation is incorrect");
            confirmNewPassword.requestFocus();
            return false;
        } else {
            confirmNewPassword.setError(null);
            return true;
        }
    }

    private void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }
        String name = user.getDisplayName();
        Uri photoUrl = user.getPhotoUrl();

        if (name == null) {
            profileName.setText("");
        } else {
            profileName.setText(name);
        }
        Glide.with(this).load(photoUrl).error(R.drawable.avatar_default).into(imgAvatar);
    }

    private void initUi() {
        oldPassword = view.findViewById(R.id.oldPassword);
        newPassword = view.findViewById(R.id.newPassword);
        confirmNewPassword = view.findViewById(R.id.confirmNewPassword);
        oldPasswordLayout = view.findViewById(R.id.oldPasswordLayout);
        newPasswordLayout = view.findViewById(R.id.newPasswordLayout);
        confirmNewPasswordLayout = view.findViewById(R.id.confirmNewPasswordLayout);
        buttonChangePassword = view.findViewById(R.id.buttonChangePassword);
        imgAvatar = view.findViewById(R.id.imgAvatarPass);
        profileName = view.findViewById(R.id.profileName);
        imgBackSetting = view.findViewById(R.id.imgBackChangePass);
    }
}