package com.example.taskmanager.ui.rewards;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.taskmanager.R;
import com.example.taskmanager.database.TaskDatabase;
import com.example.taskmanager.databinding.ItemRewardBinding;
import com.example.taskmanager.ui.rewards.RewardItem;
import java.util.List;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.RewardViewHolder> {

    private List<RewardItem> rewardsList;

    public RewardsAdapter(List<RewardItem> rewardsList) {
        this.rewardsList = rewardsList;
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemRewardBinding itemBinding = ItemRewardBinding.inflate(layoutInflater, parent, false);
        return new RewardViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        RewardItem reward = rewardsList.get(position);
        holder.bind(reward);
    }

    @Override
    public int getItemCount() {
        return rewardsList.size();
    }

    public class RewardViewHolder extends RecyclerView.ViewHolder {

        private final ItemRewardBinding binding;

        public RewardViewHolder(@NonNull ItemRewardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.btnComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    RewardItem reward = rewardsList.get(position);
                    markAsComplete(reward);
                }
            });

            binding.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    RewardItem reward = rewardsList.get(position);
                    deleteReward(reward);
                }
            });
        }

        public void bind(RewardItem reward) {
            binding.rewardTitle.setText(reward.getTitle());
            binding.rewardDescription.setText(reward.getDescription());
            binding.rewardPrice.setText(String.valueOf(reward.getPrice()));
        }

        private void markAsComplete(RewardItem reward) {
            // Implement the logic to mark the reward as complete
            // For now, we just remove it from the list and notify the adapter
            rewardsList.remove(reward);
            if (!reward.isRepeatable()) {
                deleteReward(reward);}
            notifyDataSetChanged();
            // Optionally, you can update the database to mark it as complete
        }

        private void deleteReward(RewardItem reward) {
            // Remove the reward from the database
            new AsyncTask<RewardItem, Void, Void>() {
                @Override
                protected Void doInBackground(RewardItem... rewards) {
                    TaskDatabase.getInstance(itemView.getContext()).rewardDao().deleteReward(rewards[0]);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    // Remove the reward from the list and notify the adapter
                    rewardsList.remove(reward);
                    notifyDataSetChanged();
                }
            }.execute(reward);
        }
    }
}
