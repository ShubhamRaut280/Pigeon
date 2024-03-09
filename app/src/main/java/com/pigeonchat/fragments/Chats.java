package com.pigeonchat.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pigeonchat.UI.ChatScreen;
import com.pigeonchat.adapters.ChatRecyclerViewAdapter;
import com.pigeonchat.Models.DataModel;
import com.pigeonchat.R;
import com.pigeonchat.databinding.FragmentChatsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chats extends Fragment implements ChatRecyclerViewAdapter.OnItemClickListener, ChatRecyclerViewAdapter.OnItemLongClickListener {

    FirebaseAuth auth;
    FirebaseUser user;
    private List<DataModel> DataModels;
    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    private FragmentChatsBinding fragmentBind;
    DatabaseReference db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentBind = FragmentChatsBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseDatabase.getInstance("https://pigeon-98944-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        db.child("users")
                .child(user.getUid())
                .child("contacts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //refresh();

                        DataModels = new ArrayList<>();
                        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(DataModels, requireContext(), Chats.this, Chats.this);
                        fragmentBind.rvChatRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                        fragmentBind.rvChatRecyclerView.setAdapter(chatRecyclerViewAdapter);

                        db.child("users")
                                .child(user.getUid())
                                .child("contacts")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for(DataSnapshot snap: snapshot.getChildren()){

                                            db.child("users")
                                                    .child(snap.getKey())
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {

                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                            DataModel modal = snapshot.getValue(DataModel.class);
                                                            DataModels.add(modal);
                                                            chatRecyclerViewAdapter.notifyDataSetChanged();

                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

//                                .addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        Log.e("Called", "meow");
//                                        for(DataSnapshot snap: snapshot.getChildren()){
//                                            Log.e("call: ", "Palko");
//                                            db.child("users")
//                                                    .child(snap.getKey())
//
//                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
//
//                                                        @Override
//                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                                            DataModel modal = snapshot.getValue(DataModel.class);
//                                                            DataModels.add(modal);
//                                                            chatRecyclerViewAdapter.notifyDataSetChanged();
//
//                                                        }
//                                                        @Override
//                                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                                        }
//                                                    });
//                                        }
//                                    }
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        fragmentBind.addContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return fragmentBind.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        //Log.e("working", DataModels.get(0).getAbout());
    }

    public void refresh(){
        fragmentBind.rvChatRecyclerView.setAdapter(null);
        DataModels = new ArrayList<>();
        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(DataModels, requireContext(), this, this);

        fragmentBind.rvChatRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        fragmentBind.rvChatRecyclerView.setAdapter(chatRecyclerViewAdapter);

        db.child("users")
                .child(user.getUid())
                .child("contacts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){

                            db.child("users")
                                    .child(snap.getKey())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            DataModel modal = snapshot.getValue(DataModel.class);
                                            DataModels.add(modal);
                                            chatRecyclerViewAdapter.notifyDataSetChanged();
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void showDialog(){

        Dialog dialogBox = new Dialog(getContext());
        dialogBox.setContentView(R.layout.input_dialog);
        dialogBox.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
        dialogBox.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        MaterialButton addUser = dialogBox.findViewById(R.id.addUserButton);
        EditText email = dialogBox.findViewById(R.id.inputText);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("") || email.getText().toString().contains(" ")){
                    Toast.makeText(getContext(), "Please enter valid email ID", Toast.LENGTH_SHORT).show();
                }else{
                    db.child("users")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    boolean exist = true;
                                    for(DataSnapshot snapshot1: snapshot.getChildren()){

                                        if(email.getText().toString().equals(snapshot1.child("emailId").getValue())){
                                            exist = true;
                                            db.child("users")
                                                    .child(user.getUid())
                                                    .child("contacts")
                                                    .child(snapshot1.getKey()).setValue(true);
                                            dialogBox.dismiss();
                                            break;

                                        }else{
                                            exist = false;
                                        }
                                    }
                                    if(!exist){
                                        Snackbar.make(fragmentBind.getRoot(), "User doesn't exist!", Snackbar.LENGTH_SHORT).show();
                                        dialogBox.dismiss();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }
        });

        dialogBox.show();

    }

    @Override
    public void onProfileClick(int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.profile_view_dialog, null);

        ImageView profile = v.findViewById(R.id.profileImage);
        ImageButton info = v.findViewById(R.id.aboutButton);
        TextView contactName = v.findViewById(R.id.contactName);

        Glide
                .with(getContext())
                .load(DataModels.get(position).getImage())
                .circleCrop()
                .into(profile);

        contactName.setText(DataModels.get(position).getName());

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "info", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setView(v);
        Objects.requireNonNull(alert.create().getWindow()).setBackgroundDrawableResource(R.drawable.shape_rounded);
        alert.show();
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(getContext(), ChatScreen.class);
        intent.putExtra("about", DataModels.get(position).getAbout());
        intent.putExtra("emailId", DataModels.get(position).getEmailId());
        intent.putExtra("profilePic", DataModels.get(position).getImage());
        intent.putExtra("name", DataModels.get(position).getName());
        intent.putExtra("userId", DataModels.get(position).getUserId());
        intent.putExtra("created_at", DataModels.get(position).getCreated_at());
        intent.putExtra("is_online", DataModels.get(position).is_online());
        intent.putExtra("token", DataModels.get(position).getToken());

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {
        Toast.makeText(getContext(), "longClick", Toast.LENGTH_SHORT).show();
    }
}
