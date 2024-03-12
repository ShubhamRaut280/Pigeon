package com.pigeonchat;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pigeonchat.Login.LoginActivity;
import com.pigeonchat.databinding.ProfileBinding;

import java.util.HashMap;
import java.util.Objects;

public class Profile extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private ActivityResultLauncher<Intent> launcher;
    private ProfileBinding profileBinding;
    private FirebaseStorage firebaseStorage;
    private Uri imgUri;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        checkAuthenticationState();

        profileBinding = ProfileBinding.inflate(getLayoutInflater());
        setContentView(profileBinding.getRoot());

        progressBar = profileBinding.progressBar;
        progressBar.setVisibility(View.GONE);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        firebaseStorage = FirebaseStorage.getInstance();

                        assert result.getData() != null;
                        imgUri = result.getData().getData();
                        profileBinding.ivProfileImg.setImageURI(imgUri);

                        StorageReference reference = firebaseStorage.getReference()
                                .child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                                .child("Profile-Pictures");

                        reference.listAll().addOnSuccessListener(listResult -> {
                            progressBar.setVisibility(View.VISIBLE);

                            if (listResult.getItems().isEmpty()) {
                                reference.child(auth.getCurrentUser().getUid())
                                        .putFile(imgUri)
                                        .addOnSuccessListener(taskSnapshot -> Log.e("UserData", "Image Uploaded to DataStorage"))
                                        .addOnFailureListener(exception -> Log.e("UserData", Objects.requireNonNull(exception.getMessage())));
                            } else {
                                listResult.getItems().get(0).delete().addOnSuccessListener(aVoid -> {
                                    Log.e("User Data Profile", "Past Profile Pic deleted from DataStorage");
                                    reference.child(auth.getCurrentUser().getUid()).putFile(imgUri)
                                            .addOnSuccessListener(taskSnapshot -> {
                                                Log.e("User Data Profile", "Image Uploaded to DataStorage");
                                                progressBar.setVisibility(View.GONE);
                                            })
                                            .addOnFailureListener(exception -> Log.e("User Data Profile", Objects.requireNonNull(exception.getMessage())));
                                });
                            }
                        });

                        Log.e("ImgUri", imgUri.toString());
                    }
                });

        databaseReference = FirebaseDatabase.getInstance("https://pigeon-98944-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        databaseReference.child("users").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).get().addOnSuccessListener(dataSnapshot -> {
            profileBinding.etUserName.setText(dataSnapshot.child("name").getValue(String.class));
            profileBinding.etAbout.setText(dataSnapshot.child("about").getValue(String.class));
            Glide.with(this).load(dataSnapshot.child("image").getValue(String.class))
                    .into(profileBinding.ivProfileImg);
        });

        profileBinding.btnUpdateUser.setOnClickListener(view -> {
            String userName = Objects.requireNonNull(profileBinding.etUserName.getText()).toString();
            String about = Objects.requireNonNull(profileBinding.etAbout.getText()).toString();

            progressBar.setVisibility(View.VISIBLE);

            firebaseStorage = FirebaseStorage.getInstance();

            firebaseStorage.getReference().child(auth.getCurrentUser().getUid())
                    .child("Profile-Pictures")
                    .child(auth.getCurrentUser().getUid())
                    .getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        HashMap<String, Object> userData = new HashMap<>();
                        userData.put("name", userName);
                        userData.put("about", about);
                        userData.put("image", uri.toString());

                        databaseReference.child("users").child(auth.getCurrentUser().getUid()).updateChildren(userData)
                                .addOnSuccessListener(aVoid -> {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(this, "Information Updated Successfully", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(this, MainActivity.class));
                                })
                                .addOnCanceledListener(() -> Toast.makeText(this, "Information not Added", Toast.LENGTH_LONG).show());
                    })
                    .addOnFailureListener(exception -> {
                        Log.e("UserData", "Failed to retrieve download URL: " + exception.getMessage());
                        Toast.makeText(this, "Failed to retrieve download URL", Toast.LENGTH_LONG).show();
                    });
        });

        profileBinding.btnSelectImage.setOnClickListener(view -> launcher.launch(new Intent(Intent.ACTION_PICK).setType("image/*")));
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAuthenticationState();
    }

    private void checkAuthenticationState() {
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
