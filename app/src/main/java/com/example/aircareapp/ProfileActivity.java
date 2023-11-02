package com.example.aircareapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 101;
    private EditText profileFullName, profileEmail, profilePassword;
    private Button btnUpdateProfile, btnLogout, btnUpdateEmail, btnChangePassword;
    private ImageView profileAvatar;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private SharedPreferences sharedPreferences;
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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    setBitmapImageView(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initUi();

        progressDialog = new ProgressDialog(this);

        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        profilePassword.setText(sharedPreferences.getString("prefPassword", ""));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        showUserInformation();

        initListener();
    }

    private void initUi() {
        profileFullName = findViewById(R.id.profileFullName);
        profileEmail = findViewById(R.id.profileEmail);
        profilePassword = findViewById(R.id.profilePassword);
        btnUpdateProfile = findViewById(R.id.buttonUpdateProfile);
        btnUpdateEmail = findViewById(R.id.buttonUpdateEmail);
        btnLogout = findViewById(R.id.logoutButton);
        btnChangePassword = findViewById(R.id.buttonChangePassword);
        profileAvatar = findViewById(R.id.profileImg);
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

        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateEmail();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChangePassword();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();
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
                            Toast.makeText(ProfileActivity.this, "Update profile success", Toast.LENGTH_SHORT).show();
                            showUserInformation();
                        }
                    }
                });
    }

    // bỏ update email vì nó là key để login
    // cho trường email enable ko dc update
    private void onClickUpdateEmail(){
        String newEmail = profileEmail.getText().toString().trim();
        progressDialog.setTitle("Update email");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "User email address updated.", Toast.LENGTH_SHORT).show();
                            showUserInformation();
                        } else {
                            Toast.makeText(ProfileActivity.this, "User email address failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void onClickChangePassword() {
        String newPassword = profilePassword.getText().toString().trim();

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
                            Toast.makeText(ProfileActivity.this, "User password updated.", Toast.LENGTH_SHORT).show();
                        } else {
                            // show dialog re-Authenticate
                            Toast.makeText(ProfileActivity.this, "User password failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void reAuthenticate() {
        // show 1 dialog login lai r lay email pass gan o duoi
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), profilePassword.getText().toString());

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            onClickUpdateEmail();
                            onClickChangePassword();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Please enter your email, password", Toast.LENGTH_SHORT).show();
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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{
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
//            profileUserName.setVisibility(View.GONE);
            profileFullName.setText("");
        } else {
//            profileUserName.setVisibility(View.VISIBLE);
            profileFullName.setText(name);
        }
        profileEmail.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.avatar_default).into(profileAvatar);
    }

    public void setBitmapImageView(Bitmap bitmapImageView){
        profileAvatar.setImageBitmap(bitmapImageView);
    }

    private void LogOut() {
        // logout GoogleAccount
        gsc.signOut();

        // Logout UserAccount
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Toast.makeText(ProfileActivity.this, "Log out Successfull", Toast.LENGTH_SHORT).show();
        finish();
    }
}