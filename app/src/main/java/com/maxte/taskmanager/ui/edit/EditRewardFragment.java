package com.maxte.taskmanager.ui.edit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.maxte.taskmanager.R;
import com.maxte.taskmanager.database.TaskDatabase;
import com.maxte.taskmanager.databinding.FragmentEditRewardBinding;
import com.maxte.taskmanager.ui.rewards.RewardItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditRewardFragment extends Fragment {

    private FragmentEditRewardBinding binding;
    private RewardItem rewardToEdit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditRewardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Hide FAB
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        // Get reward_id from arguments
        if (getArguments() != null) {
            int rewardId = getArguments().getInt("reward_id", -1);
            if (rewardId != -1) {
                loadReward(rewardId);
            }
        }

        // Save button logic
        Button buttonConfirm = binding.btnSaveReward;
        buttonConfirm.setOnClickListener(v -> updateReward());

        return root;
    }

    private void loadReward(int rewardId) {
        new AsyncTask<Integer, Void, RewardItem>() {
            @Override
            protected RewardItem doInBackground(Integer... ids) {
                TaskDatabase db = TaskDatabase.getInstance(requireContext());
                return db.rewardDao().getRewardById(ids[0]);
            }

            @Override
            protected void onPostExecute(RewardItem reward) {
                if (reward != null) {
                    rewardToEdit = reward;
                    populateFields(reward);
                } else {
                    // Handle reward not found
                }
            }
        }.execute(rewardId);
    }

    private void populateFields(RewardItem reward) {
        binding.editRewardTitle.setText(reward.getTitle());
        binding.editRewardDescription.setText(reward.getDescription());
        binding.editRewardPrice.setText(String.valueOf(reward.getPrice()));
    }

    private void updateReward() {
        String title = binding.editRewardTitle.getText().toString();
        String description = binding.editRewardDescription.getText().toString();
        String priceStr = binding.editRewardPrice.getText().toString();

        // Input validation
        if (title.isEmpty()) {
            binding.editRewardTitle.setError("Title cannot be empty");
            return;
        }

        if (description.isEmpty()) {
            binding.editRewardDescription.setError("Description cannot be empty");
            return;
        }

        if (priceStr.isEmpty()) {
            binding.editRewardPrice.setError("Price cannot be empty");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) {
                binding.editRewardPrice.setError("Price must be a positive number");
                return;
            }
        } catch (NumberFormatException e) {
            binding.editRewardPrice.setError("Invalid price");
            return;
        }

        // Update reward details
        rewardToEdit.setTitle(title);
        rewardToEdit.setDescription(description);
        rewardToEdit.setPrice(price);

        // Save to database
        new AsyncTask<RewardItem, Void, Void>() {
            @Override
            protected Void doInBackground(RewardItem... rewards) {
                TaskDatabase.getInstance(requireContext()).rewardDao().updateReward(rewards[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                // Navigate back
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.popBackStack();
            }
        }.execute(rewardToEdit);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

        // Show FAB again
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
    }
}
