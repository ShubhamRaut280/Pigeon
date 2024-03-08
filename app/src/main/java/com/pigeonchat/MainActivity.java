package com.pigeonchat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pigeonchat.adapters.MainViewPagerAdapter;
import com.pigeonchat.databinding.ActivityMainBinding;
import com.pigeonchat.fragments.Chats;
import com.pigeonchat.fragments.CallLogs;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    boolean online;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference db;
    private ActivityMainBinding bind;
    private MainViewPagerAdapter mainViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        online = true;

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        db = FirebaseDatabase.getInstance("https://pigeon-98944-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        db.child("users")
                                    .child(user.getUid())
                                    .child("is_online").setValue(true);


        mainViewPagerAdapter = new MainViewPagerAdapter(this);

        mainViewPagerAdapter.addFragment(new Chats(), "Chats");
        mainViewPagerAdapter.addFragment(new CallLogs(), "Calls");

        bind.vPgr2.setAdapter(mainViewPagerAdapter);

        new TabLayoutMediator(bind.tbLayout, bind.vPgr2, (tab, position) -> {
            tab.setText(mainViewPagerAdapter.getPageTitle(position));
        }).attach();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        online = false;

        db.child("users")
                .child(user.getUid())
                .child("is_online").setValue(false);
    }
}
