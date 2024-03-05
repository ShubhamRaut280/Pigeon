package com.pigeonchat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pigeonchat.Models.AllData;

import java.util.Map;

public class FetchDataService extends Service {

    FirebaseAuth auth;
    FirebaseUser user;

    public int onStartCommand(Intent intent, int flags, int startId){

        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();

        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();

        fetchDataFromDatabase();

        return START_NOT_STICKY;
    }

    private void fetchDataFromDatabase(){


        DatabaseReference db = FirebaseDatabase.getInstance("https://pigeon-98944-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users");

        db.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Map<String, Object> dataMap = (Map<String, Object>) snapshot.getValue();

                    String name = dataMap.get("name").toString();

                    Toast.makeText(FetchDataService.this, name, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AllData data;

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
