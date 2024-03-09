package com.pigeonchat;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pigeonchat.Login.LoginActivity;
import com.pigeonchat.UI.ChatScreen;
import com.pigeonchat.databinding.ActivitySplashScreenBinding;

public class SplashScreen extends AppCompatActivity {
    private ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final DatabaseReference db = FirebaseDatabase.getInstance("https://pigeon-98944-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        if(getIntent().getExtras()!=null){
            // from notification
            String userId = getIntent().getStringExtra("userId");
            String about = getIntent().getStringExtra("about");
            String emailId = getIntent().getStringExtra("emailId");
            String profilePic = getIntent().getStringExtra("profilePic");
            long created_at = getIntent().getLongExtra("created_at", 123);
            String userName = getIntent().getStringExtra("name");

            Log.e("Splash screen userID", userId);

            Intent mainIntent = new Intent(this, LoginActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(mainIntent);

            Intent intent = new Intent(SplashScreen.this, ChatScreen.class);

            intent.putExtra("about", about);
            intent.putExtra("emailId", emailId);
            intent.putExtra("profilePic", profilePic);
            intent.putExtra("name", userName);
            intent.putExtra("userId", userId);
            intent.putExtra("created_at", created_at);

            startActivity(intent);
            finish();

        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 500);
        }
    }

}