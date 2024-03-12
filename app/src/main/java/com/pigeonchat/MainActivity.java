package com.pigeonchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pigeonchat.Login.LoginActivity;
import com.pigeonchat.adapters.MainViewPagerAdapter;
import com.pigeonchat.databinding.ActivityMainBinding;
import com.pigeonchat.databinding.CustomDeleteUserDialogboxBinding;
import com.pigeonchat.fragments.CallLogs;
import com.pigeonchat.fragments.Chats;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding bind;
    private MainViewPagerAdapter mainViewPagerAdapter;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        bind.progressBar.setVisibility(View.VISIBLE);

        checkAuthenticationState();

        //Toolbar
        setSupportActionBar(bind.tbMainToolBar);

        databaseReference = FirebaseDatabase.getInstance("https://pigeon-98944-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        //viewPager
        mainViewPagerAdapter = new MainViewPagerAdapter(this);
        mainViewPagerAdapter.addFragment(new Chats(), "Chats");
        mainViewPagerAdapter.addFragment(new CallLogs(), "Calls");
        bind.vPgr2.setAdapter(mainViewPagerAdapter);

        TabLayoutMediator mediator = new TabLayoutMediator(bind.tbLayout, bind.vPgr2,
                (tab, position) -> tab.setText(mainViewPagerAdapter.getPageTitle(position)));
        mediator.attach();

        databaseReference.child("users").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                bind.progressBar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public static final int ITM_PROFILE = R.id.itmProfile;
    public static final int ITM_SIGNOUT = R.id.itmSignout;
    public static final int ITM_DELETE = R.id.itmDelete;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.itmProfile){
            startActivity(new Intent(this, Profile.class));
            return true;
        }else if(id == R.id.itmSignout){
            auth.signOut();
            return true;
        }else if(id == R.id.itmDelete){
            deleteAlertBox();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }


//        switch (id) {
//            case R.id.itmProfile:
//                startActivity(new Intent(this, Profile.class));
//                return true;
//            case ITM_SIGNOUT:
//                auth.signOut();
//                return true;
//            case ITM_DELETE:
//                deleteAlertBox();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
    }

    private void checkAuthenticationState() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(auth -> {
            if (auth.getCurrentUser() == null) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });
    }

    private void deleteAlertBox() {
        CustomDeleteUserDialogboxBinding bindAlert = CustomDeleteUserDialogboxBinding.inflate(getLayoutInflater());

        AlertDialog.Builder customDialogBuilder = new AlertDialog.Builder(this);
        customDialogBuilder.setView(bindAlert.getRoot());

        AlertDialog alertDialog = customDialogBuilder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(R.drawable.shape_round);
        alertDialog.show();

        bindAlert.confirm.setOnClickListener(v -> {
            String email = Objects.requireNonNull(bindAlert.tvUserEmail.getText()).toString();

            databaseReference.child("users").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child("emailId").get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    if (!Objects.equals(snapshot.getValue(), email)) {
                        Toast.makeText(MainActivity.this, "Incorrect email ID", Toast.LENGTH_LONG).show();
                    } else {
                        databaseReference.child("users").child(auth.getCurrentUser().getUid()).removeValue().addOnSuccessListener(aVoid -> {
                            Toast.makeText(MainActivity.this, "User Deleted", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }).addOnFailureListener(e -> Log.e("Delete User", Objects.requireNonNull(e.getMessage())));
                    }
                }
            }).addOnFailureListener(e -> Log.e("Delete User", Objects.requireNonNull(e.getMessage())));
        });

        bindAlert.Cancel.setOnClickListener(v -> alertDialog.dismiss());
    }
}
