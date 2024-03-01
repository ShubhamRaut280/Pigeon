package com.pigeonchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pigeonchat.DataModel;
import com.pigeonchat.databinding.CalllistBinding;
import java.util.List;

public class CallRecyclerViewAdapter extends RecyclerView.Adapter<CallRecyclerViewAdapter.CallViewHolder> {

    private List<DataModel> arr;
    private Context context;
    private CalllistBinding callBinding;

    public CallRecyclerViewAdapter(List<DataModel> arr, Context context) {
        this.arr = arr;
        this.context = context;
    }

    class CallViewHolder extends RecyclerView.ViewHolder {
        CalllistBinding callBinding;

        CallViewHolder(CalllistBinding callBinding) {
            super(callBinding.getRoot());
            this.callBinding = callBinding;
        }
    }

    @NonNull
    @Override
    public CallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        callBinding = CalllistBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CallViewHolder(callBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CallViewHolder holder, int position) {
        DataModel dataModel = arr.get(position);
        holder.callBinding.tvProfileName.setText(dataModel.getName());
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }
}
