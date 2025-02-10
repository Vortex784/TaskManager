package com.example.taskmanager.ui.create;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.taskmanager.R;
import com.example.taskmanager.database.TaskDatabase;
import com.example.taskmanager.databinding.FragmentCreateRewardBinding;
import com.example.taskmanager.databinding.FragmentCreateRewardBinding;
import com.example.taskmanager.ui.rewards.RewardItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateRewardFragment extends Fragment {

    private FragmentCreateRewardBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CreateRewardViewModel createRewardViewModel =
                new ViewModelProvider(this).get(CreateRewardViewModel.class);

        binding = FragmentCreateRewardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);


        binding.createRewardRepeatable.setText("Repeatable?");

        Button buttonConfirm = binding.createRewardConfirm;

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReward();

            }
            });

        return root;
    }

    private void saveReward() {
        String title = binding.createRewardTitle.getText().toString();
        String description = binding.createRewardDesc.getText().toString();
        String price = binding.createRewardPrice.getText().toString();
        boolean isRepeatable = binding.createRewardRepeatable.isChecked();

        RewardItem reward = new RewardItem(title, description, Double.parseDouble(price), isRepeatable);

        // Save to database in background
        new AsyncTask<RewardItem, Void, Void>() {
            @Override
            protected Void doInBackground(RewardItem... rewards) {
                TaskDatabase.getInstance(getContext()).rewardDao().insertReward(rewards[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Go back to the previous screen
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.popBackStack();
            }
        }.execute(reward);
    }

    private void navigateBack(View view) {
        NavController navController = Navigation.findNavController(view);
        navController.navigateUp();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Clear any additional resources or data
    }
}