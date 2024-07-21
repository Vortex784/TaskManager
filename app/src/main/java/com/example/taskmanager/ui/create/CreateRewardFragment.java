package com.example.taskmanager.ui.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.taskmanager.databinding.FragmentCreateRewardBinding;
import com.example.taskmanager.databinding.FragmentCreateRewardBinding;

public class CreateRewardFragment extends Fragment {

    private FragmentCreateRewardBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CreateRewardViewModel createRewardViewModel =
                new ViewModelProvider(this).get(CreateRewardViewModel.class);

        binding = FragmentCreateRewardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.createRewardTitle.setText("Reward title");
        binding.createRewardDesc.setText("Description");
        binding.createRewardPrice.setText("Reward price");
        binding.createRewardRepeatable.setText("Repeatable?");


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