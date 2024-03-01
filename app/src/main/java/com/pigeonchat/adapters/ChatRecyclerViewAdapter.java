package com.pigeonchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pigeonchat.Models.DataModel;
import com.pigeonchat.databinding.ChatlistBinding;
import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MyViewHolder> {

    private List<DataModel> arr;
    private Context context;

    public ChatRecyclerViewAdapter(List<DataModel> arr, Context context) {
        this.arr = arr;
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ChatlistBinding bind;

        MyViewHolder(ChatlistBinding bind) {
            super(bind.getRoot());
            this.bind = bind;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatlistBinding bind = ChatlistBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataModel dataModel = arr.get(position);
        holder.bind.tvProfileName.setText(dataModel.getName());
        holder.bind.tvLastMsg.setText(dataModel.getMsg());
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }
}
