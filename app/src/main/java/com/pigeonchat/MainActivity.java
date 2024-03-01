package com.pigeonchat;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.pigeonchat.adapters.MainViewPagerAdapter;
import com.pigeonchat.databinding.ActivityMainBinding;
import com.pigeonchat.fragments.Chats;
import com.pigeonchat.fragments.CallLogs;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding bind;
    private MainViewPagerAdapter mainViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        mainViewPagerAdapter = new MainViewPagerAdapter(this);

        mainViewPagerAdapter.addFragment(new Chats(), "Chats");
        mainViewPagerAdapter.addFragment(new CallLogs(), "Calls");

        bind.vPgr2.setAdapter(mainViewPagerAdapter);

        new TabLayoutMediator(bind.tbLayout, bind.vPgr2, (tab, position) -> {
            tab.setText(mainViewPagerAdapter.getPageTitle(position));
        }).attach();
    }
}
