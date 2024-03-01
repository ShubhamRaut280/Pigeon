package com.pigeonchat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pigeonchat.adapters.ChatRecyclerViewAdapter;
import com.pigeonchat.DataModel;
import com.pigeonchat.R;
import com.pigeonchat.databinding.FragmentChatsBinding;

import java.util.ArrayList;
import java.util.List;

public class Chats extends Fragment {

    private List<DataModel> arr;
    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    private FragmentChatsBinding fragmentBind;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentBind = FragmentChatsBinding.inflate(inflater, container, false);
        return fragmentBind.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arr = new ArrayList<>();
        arr.add(new DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[....1", "Yash"));
        arr.add(new DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Yash Jadhav"));
        arr.add(new DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Yash Surendra Jadhav"));
        arr.add(new DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Manisha"));
        arr.add(new DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Manisha Jadhav"));
        arr.add(new DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Manisha Surendra Jadhav"));
        arr.add(new DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Surendra"));
        arr.add(new DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Surendra Jadhav"));
        arr.add(new DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Surendra GorakhNath Jadhav"));
        arr.add(new DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Karan"));
        arr.add(new DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Karan Jadhav"));
        arr.add(new DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Karan Surendra Jadhav"));
        arr.add(new DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[", "Saurav"));
        arr.add(new DataModel(R.drawable.broly, "fqefhqfewsdqf[ew[......n", "Shubham"));

        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(arr, requireContext());

        fragmentBind.rvChatRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        fragmentBind.rvChatRecyclerView.setAdapter(chatRecyclerViewAdapter);
    }
}
