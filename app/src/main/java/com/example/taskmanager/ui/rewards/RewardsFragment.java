package com.example.taskmanager.ui.rewards;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.database.TaskDatabase;
import com.example.taskmanager.databinding.FragmentRewardsBinding;

import java.util.List;

public class RewardsFragment extends Fragment {

    private FragmentRewardsBinding binding;
    private RewardsAdapter rewardsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RewardsViewModel rewardsViewModel =
                new ViewModelProvider(this).get(RewardsViewModel.class);

        binding = FragmentRewardsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerViewRewards;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadRewards();






        return root;
    }

    private void loadRewards() {
        new AsyncTask<Void, Void, List<RewardItem>>() {
            @Override
            protected List<RewardItem> doInBackground(Void... voids) {
                TaskDatabase db = TaskDatabase.getInstance(getContext());
                return db.rewardDao().getAllRewards();
            }
            @Override
            protected void onPostExecute(List<RewardItem> rewards) {
                rewardsAdapter = new RewardsAdapter(getContext(),rewards);
                binding.recyclerViewRewards.setAdapter(rewardsAdapter);
            }
        }.execute();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}