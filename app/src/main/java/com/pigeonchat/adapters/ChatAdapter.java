package com.pigeonchat.adapters;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "sender", Toast.LENGTH_SHORT).show();
            }
        });



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.bottom_sheet);

                Toast.makeText(context.getApplicationContext(), "sender", Toast.LENGTH_SHORT).show();

                LinearLayout layoutDelete = dialog.findViewById(R.id.layoutDelete);
                LinearLayout layoutCopy = dialog.findViewById(R.id.layoutCopy);

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
                return false;
            }
        });

        if(holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMsg.setText(messageModel.getMessage());

            Date date = new Date(messageModel.getTimestamp());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
            String strDate = simpleDateFormat.format(date);
            ((SenderViewHolder)holder).senderTime.setText(strDate.toString());
        }else{
            ((ReceiverViewHolder)holder).receiverMsg.setText(messageModel.getMessage());

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
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsg = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);
        }
    }
}
