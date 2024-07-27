package com.example.taskmanager.ui.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.taskmanager.databinding.FragmentCreateTaskBinding;

public class CreateTaskFragment extends Fragment {

    private FragmentCreateTaskBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CreateTaskViewModel createTaskViewModel =
                new ViewModelProvider(this).get(CreateTaskViewModel.class);

        binding = FragmentCreateTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.createTaskTitle.setText("Task title");
        binding.createTaskDesc.setText("Description");
        binding.createTaskReward.setText("Task reward");
        binding.createTaskRepeatable.setText("Repeatable?");

        Button buttonConfirm = binding.createTaskConfirm;

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle confirm button click
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Clear any additional resources or data
    }
}