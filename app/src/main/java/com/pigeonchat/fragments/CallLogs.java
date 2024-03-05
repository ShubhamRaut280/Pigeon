package com.pigeonchat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pigeonchat.Models.DataModel;
import com.pigeonchat.R;
import com.pigeonchat.adapters.CallRecyclerViewAdapter;
import com.pigeonchat.databinding.FragmentCallLogsBinding;

import java.util.ArrayList;
import java.util.List;

public class CallLogs extends Fragment {

    private List<DataModel> arr;
    private CallRecyclerViewAdapter callAdapter;
    private FragmentCallLogsBinding fragmentBind;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBind = FragmentCallLogsBinding.inflate(inflater, container, false);
        return fragmentBind.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arr = new ArrayList<>();
        //arr.add(new DataModel("R.drawable.broly", "fqefhqfewsdqf[ew[....1"));
//        arr.add(new DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Yash Jadhav"));
//        arr.add(new DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Yash Surendra Jadhav"));
        // Add more items as needed...

        callAdapter = new CallRecyclerViewAdapter(arr, requireContext());

        fragmentBind.rvCallLogRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        fragmentBind.rvCallLogRecyclerView.setAdapter(callAdapter);
    }
}
