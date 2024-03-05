package com.pigeonchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pigeonchat.Models.DataModel;
import com.pigeonchat.databinding.ChatlistBinding;
import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MyViewHolder> {

    private List<DataModel> DataModels;
    private Context context;

    public ChatRecyclerViewAdapter(List<DataModel> DataModels, Context context) {
        this.DataModels = DataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatlistBinding bind = ChatlistBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(bind);
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ChatlistBinding bind;

        MyViewHolder(ChatlistBinding bind) {
            super(bind.getRoot());
            this.bind = bind;
        }
    }
}


