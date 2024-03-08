package com.pigeonchat.adapters;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pigeonchat.Models.MessageModel;
import com.pigeonchat.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<MessageModel> messageModels;
    Context context;
    String receiverId;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String receiverId) {
        this.messageModels = messageModels;
        this.context = context;
        this.receiverId = receiverId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // To check messages whether they sent by sender or receiver
        if(messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }else{
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = messageModels.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongPress(holder, holder.itemView, position);
                return false;
            }
        });

        if(holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMsg.setText(messageModel.getMessage());

            Log.e("msgStatus", String.valueOf(messageModel.isSeen()));
            if(messageModel.isSeen()){
                ((SenderViewHolder)holder).seen.setColorFilter(ContextCompat.getColor(context, R.color.blue), PorterDuff.Mode.MULTIPLY);
            }else{
                ((SenderViewHolder)holder).seen.clearColorFilter();
            }

            ((SenderViewHolder)holder).senderMsg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onLongPress(holder, holder.itemView.findViewById(R.id.senderText), position);
                    return false;
                }
            });

            if(((SenderViewHolder)holder).senderMsg.getText().toString().equals("🚫 You deleted this message")){
                ((SenderViewHolder)holder).senderMsg.setTypeface(null, Typeface.BOLD_ITALIC);
            }

            Date date = new Date(messageModel.getTimestamp());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
            String strDate = simpleDateFormat.format(date);
            ((SenderViewHolder)holder).senderTime.setText(strDate.toString());
        }else{

            ((ReceiverViewHolder)holder).receiverMsg.setText(messageModel.getMessage());
            ((ReceiverViewHolder)holder).receiverMsg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onLongPress(holder, holder.itemView.findViewById(R.id.receiverText), position);
                    return false;
                }
            });

            if(((ReceiverViewHolder)holder).receiverMsg.getText().toString().equals("🚫 This message was deleted")){
                ((ReceiverViewHolder)holder).receiverMsg.setTypeface(null, Typeface.BOLD_ITALIC);
            }
            Date date = new Date(messageModel.getTimestamp());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
            String strDate = simpleDateFormat.format(date);
            ((ReceiverViewHolder)holder).receiverTime.setText(strDate.toString());
        }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public void onLongPress(RecyclerView.ViewHolder holder, View view, int position){
        MessageModel messageModel = messageModels.get(position);

        final Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet);

        LinearLayout layoutDelete = dialog.findViewById(R.id.layoutDelete);
        LinearLayout layoutCopy = dialog.findViewById(R.id.layoutCopy);


        if(!messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
           layoutDelete.setVisibility(View.GONE);
        }

        layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference db = FirebaseDatabase.getInstance("https://pigeon-98944-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
                String senderRoom = FirebaseAuth.getInstance().getUid() + "+" + receiverId;
                String receiverRoom = receiverId + "+" + FirebaseAuth.getInstance().getUid();
                db.child("chats").child(senderRoom)
                        .child(messageModel.getTimestamp().toString())
                        .child("message")
                        .setValue("🚫 You deleted this message")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        db.child("chats").child(receiverRoom)
                                                .child(messageModel.getTimestamp().toString())
                                                .child("message")
                                                .setValue("🚫 This message was deleted");
                                    }
                                });
                dialog.cancel();
            }
        });

        layoutCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.getClass() == SenderViewHolder.class){
                    SpannableStringBuilder text = (SpannableStringBuilder) ((SenderViewHolder)holder).senderMsg.getText();
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", text);
                    clipboard.setPrimaryClip(clip);
                    dialog.cancel();
                }else{
                    SpannableStringBuilder text =  (SpannableStringBuilder)((ReceiverViewHolder)holder).receiverMsg.getText();
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", text);
                    clipboard.setPrimaryClip(clip);
                    dialog.cancel();
                }
            }
        });



        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    // Receiver
    public class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView receiverMsg, receiverTime;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverMsg = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receiverTime);

        }
    }

    // Sender
    public class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView senderMsg, senderTime;
        ImageView seen;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsg = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);
            seen = itemView.findViewById(R.id.msgStatus);
        }
    }
}
