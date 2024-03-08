package com.pigeonchat.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pigeonchat.FetchDataService;
import com.pigeonchat.Models.MessageModel;
import com.pigeonchat.R;
import com.pigeonchat.Utility.Service;
import com.pigeonchat.activities.VideocallActivity;
import com.pigeonchat.adapters.ChatAdapter;
import com.pigeonchat.databinding.ActivityChatScreenBinding;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;
import java.util.Date;

public class ChatScreen extends AppCompatActivity {
    ActivityChatScreenBinding binding;
    final DatabaseReference db = FirebaseDatabase.getInstance("https://pigeon-98944-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    final String senderId = auth.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.myToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        auth = FirebaseAuth.getInstance();
        Service.startServiceIfNotRunning(this, FetchDataService.class);


       init();

        monitorVideocallRequests();
    }

    private void init(){

        String about = getIntent().getStringExtra("about");
        String emailId = getIntent().getStringExtra("emailId");
        String profilePic = getIntent().getStringExtra("profilePic");
        String userName = getIntent().getStringExtra("name");
        String receiveId = getIntent().getStringExtra("userId");
        String created_at = getIntent().getStringExtra("created_at");
        String is_online = getIntent().getStringExtra("is_online");


        binding.userName.setText(userName);

        Glide
                .with(getApplicationContext())
                .load(profilePic)
                .circleCrop()
                .into(binding.profile);


        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageModels, this, receiveId);
        binding.chatRecycler.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecycler.setLayoutManager(layoutManager);

        EmojiPopup popup = EmojiPopup.Builder.fromRootView(binding.rootView).build(binding.msgContent);

        binding.emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.toggle();
            }
        });

        final String senderRoom = senderId + "+" + receiveId;
        final String receiverRoom = receiveId + "+" + senderId;

        db.child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for(DataSnapshot snapshot1: snapshot.getChildren()){
                            MessageModel model = snapshot1.getValue(MessageModel.class);

                            Log.e("stat", String.valueOf(model.isSeen()));

                            if(!snapshot1.child("uId").getValue().equals(auth.getUid())){
                                model.setSeen(true);
                                db.child("chats")
                                        .child(senderRoom)
                                        .child(snapshot1.getKey())
                                        .child("seen").setValue(true);

                                db.child("chats")
                                        .child(receiverRoom)
                                        .child(snapshot1.getKey())
                                        .child("seen").setValue(true);


                                Log.e("time", snapshot1.getKey());
                                Log.e("chatting", (String) snapshot1.child("message").getValue());
                            }

                            messageModels.add(model);


                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.msgContent.getText().toString();
                final MessageModel model = new MessageModel(senderId, message);
                model.setTimestamp(new Date().getTime());
                binding.msgContent.setText("");

                EmojiTextView emojiTextView = (EmojiTextView) LayoutInflater
                        .from(v.getContext())
                        .inflate(R.layout.emoji_text_view, binding.bottom,false);

                //emojiTextView.setText(binding.msgContent.toString());
                binding.bottom.addView(emojiTextView);
                binding.msgContent.getText().clear();


                db.child("users")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot snapshot1: snapshot.getChildren()){
                                    if(!snapshot1.hasChild(senderRoom)){
                                        db.child("users")
                                                .child(receiveId)
                                                .child("contacts")
                                                .child(auth.getUid()).setValue(true);

                                        db.child("chats")
                                                .child(senderRoom)
                                                .child(String.valueOf(model.getTimestamp()))
                                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        db.child("chats")
                                                                .child(receiverRoom)
                                                                .child(String.valueOf(model.getTimestamp()))
                                                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {

                                                                    }
                                                                });
                                                    }
                                                });
                                    }else{
                                        db.child("chats")
                                                .child(senderRoom)
                                                .child(String.valueOf(model.getTimestamp()))
                                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        db.child("chats")
                                                                .child(receiverRoom)
                                                                .child(String.valueOf(model.getTimestamp()))
                                                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {

                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });


        binding.myToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.myToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.voiceCall){
                    Toast.makeText(ChatScreen.this, "voice click", Toast.LENGTH_SHORT).show();
                    return true;
                }else if(item.getItemId() == R.id.videoCall){

                    Log.d("videocall", "videocall  clicked ");
                    startVideocall();

                    Toast.makeText(ChatScreen.this, "calling", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });


    }
    public void StartService(View v){
        startService(new Intent(getBaseContext(), FetchDataService.class));
    }

    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), FetchDataService.class));
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

    private void startVideocall() {

        String receiver  = getIntent().getStringExtra("userId");
        String sender = senderId;

        Intent i = new Intent(ChatScreen.this, VideocallActivity.class);
        i.putExtra("sender", sender);
        i.putExtra("receiver", receiver);

        startActivity(i);
    }


    private void monitorVideocallRequests()
    {

        Log.d("videocall", "monitoring");

        String user = senderId;
        Log.d("videocall", "monitoring for"+user);

        DatabaseReference ref =db.child("callRequests");
        ref.orderByChild(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    Log.d("videocall", "there are changes in doc");

                    DataSnapshot snap = snapshot.child(user);
                        String connId = snap.child("connectionId").getValue(String.class);
                        String sender = snap.child("sender").getValue(String.class);
                        boolean callAccepted = Boolean.TRUE.equals(snap.child("callAccepted").getValue(boolean.class));
                        boolean isHangout = Boolean.TRUE.equals(snap.child("isHangout").getValue(boolean.class));
                        boolean callRejected = Boolean.TRUE.equals(snap.child("callRejected").getValue(boolean.class));

                    Log.d("videocall", "onCreate: from videocall connectin id "+connId+ " sender : " +sender );


                    if (!isHangout && !callRejected )
                        {
                            initVideocallReq(sender, user,  connId);
                        }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showVideocallRequest(String sender,String user, String connId) {
        String receiver  = getIntent().getStringExtra("userId");

        Intent i = new Intent(ChatScreen.this, VideocallActivity.class);
        i.putExtra("sender", sender);
        i.putExtra("receiver", user);
        i.putExtra("connId", connId);

        startActivity(i);
    }


    public  void initVideocallReq(String sender,String user, String connId){


            Dialog dialogBox = new Dialog(this);
            dialogBox.setContentView(R.layout.dialoguebox_for_video_call_req_alert);


            dialogBox.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);

            dialogBox.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            MaterialButton confirm = dialogBox.findViewById(R.id.confirmButton);



            if (confirm != null ) {
                confirm.setOnClickListener(v -> {
                    showVideocallRequest(sender, user,  connId);
                    dialogBox.dismiss();

                });


            } else {
                dialogBox.dismiss();
            }

            if ((this instanceof AppCompatActivity && !this.isFinishing()) && !dialogBox.isShowing())
                dialogBox.show();

        }

}