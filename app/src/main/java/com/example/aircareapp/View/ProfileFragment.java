package com.example.aircareapp.View;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aircareapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;

public class ProfileFragment extends Fragment {

    private View view;

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 101;
    private EditText profileFullName;
    private TextView profileEmail;
    private Button btnUpdateProfile;
    private ImageView profileAvatar, imgBackSetting;
    private ProgressDialog progressDialog;

    // Lấy thư viện ảnh để update avatar
    private Uri mUri;
    public void setUri(Uri mUri) {
        this.mUri = mUri;
    }
    final private ActivityResultLauncher<Intent> mIntentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (intent == null) {
                    return;
                }
                Uri uri = intent.getData();
                setUri(uri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    setBitmapImageView(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        initUi();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());

        showUserInformation();

        initListener();
    }

    private void initUi() {
        profileFullName = view.findViewById(R.id.profileFullName);
        profileEmail = view.findViewById(R.id.profileEmail);
        btnUpdateProfile = view.findViewById(R.id.buttonUpdateProfile);
        profileAvatar = view.findViewById(R.id.profileImg);
        imgBackSetting = view.findViewById(R.id.imgBackProfile);
    }

    private void initListener() {
        profileAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();
            }
        });

        imgBackSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                ProfileFragment profileFragment = (ProfileFragment) getFragmentManager().findFragmentByTag("fragProfile");
                transaction.remove(profileFragment);
                transaction.replace(R.id.mainActivity, new SettingFragment());
                transaction.commit();
            }
        });
    }

    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null) {
            return;
        }
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        String profileFullNameUpdate = profileFullName.getText().toString().trim();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(profileFullNameUpdate)
                .setPhotoUri(mUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Update profile success", Toast.LENGTH_SHORT).show();
                            showUserInformation();
                        }
                    }
                });
    }

    private void onClickRequestPermission() {
        // andorid 6 tro xuong ko can permiss
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST_CODE_READ_EXTERNAL_STORAGE);
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mIntentActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if (name == null) {
            profileFullName.setText("");
        } else {
            profileFullName.setText(name);
        }
        profileEmail.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.avatar_default).into(profileAvatar);
    }

    public void setBitmapImageView(Bitmap bitmapImageView){
        profileAvatar.setImageBitmap(bitmapImageView);
    }
}