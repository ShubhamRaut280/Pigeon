package com.pigeonchat.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pigeonchat.R;
import com.pigeonchat.databinding.ActivityChatScreenBinding;

public class ChatScreen extends AppCompatActivity {
    ActivityChatScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.myToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.myToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatScreen.this, "on Back", Toast.LENGTH_SHORT).show();
            }
        });

        binding.myToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.voiceCall){
                    Toast.makeText(ChatScreen.this, "voice click", Toast.LENGTH_SHORT).show();
                    return true;
                }else if(item.getItemId() == R.id.videoCall){
                    Toast.makeText(ChatScreen.this, "video click", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }
}