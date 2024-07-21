package com.example.taskmanager.ui.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.taskmanager.databinding.FragmentCreateBinding;
import com.example.taskmanager.databinding.FragmentHomeBinding;

public class CreateFragment extends Fragment {

    private FragmentCreateBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CreateViewModel createViewModel =
                new ViewModelProvider(this).get(CreateViewModel.class);

        binding = FragmentCreateBinding.inflate(inflater, container, false);
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
}