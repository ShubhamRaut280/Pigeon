package com.pigeonchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pigeonchat.Models.DataModel;
import com.pigeonchat.databinding.ChatlistBinding;
import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MyViewHolder> {

    private List<DataModel> DataModels;
    private Context context;
    private ChatRecyclerViewAdapter.OnItemClickListener listener;
    private ChatRecyclerViewAdapter.OnItemLongClickListener longClickListener;

    public ChatRecyclerViewAdapter(List<DataModel> DataModels, Context context, ChatRecyclerViewAdapter.OnItemClickListener listener, ChatRecyclerViewAdapter.OnItemLongClickListener longClickListener) {
        this.DataModels = DataModels;
        this.context = context;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatlistBinding bind = ChatlistBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(bind, listener, longClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataModel dataModel = DataModels.get(position);
        holder.bind.tvProfileName.setText(dataModel.getName());

        Glide
                .with(context)
                .load(dataModel.getImage())
                .circleCrop()
                .into(holder.bind.ivCallProfileImg);

    }

    @Override
    public int getItemCount() {
        return DataModels.size();
    }

    public interface OnItemClickListener {

        void onProfileClick(int position);
        void onItemClick(int position);

    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ChatlistBinding bind;
        ChatRecyclerViewAdapter.OnItemClickListener listener;
        ChatRecyclerViewAdapter.OnItemLongClickListener longClickListener;

        MyViewHolder(ChatlistBinding bind, ChatRecyclerViewAdapter.OnItemClickListener listener, ChatRecyclerViewAdapter.OnItemLongClickListener longClickListener) {
            super(bind.getRoot());
            this.bind = bind;
            this.listener = listener;
            this.longClickListener = longClickListener;

            bind.ivCallProfileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onProfileClick(getAdapterPosition());
                }
            });

            bind.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getAdapterPosition());
                }
            });

            bind.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onItemLongClick(getAdapterPosition());
                    return false;
                }
            });
        }
    }
}
