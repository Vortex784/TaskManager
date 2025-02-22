package com.example.taskmanager.ui.rewards;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.database.Points;
import com.example.taskmanager.database.PointsDao;
import com.example.taskmanager.database.TaskDatabase;
import com.example.taskmanager.databinding.ItemRewardBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.RewardViewHolder> {

    private List<RewardItem> rewardsList;
    private Context context;

    public RewardsAdapter(Context context, List<RewardItem> rewardsList) {
        this.rewardsList = rewardsList;
        this.context = context;
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

            // Complete Reward
            binding.btnComplete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    RewardItem reward = rewardsList.get(position);
                    markAsComplete(reward);
                }
            });

            // Delete Reward
            binding.btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    RewardItem reward = rewardsList.get(position);
                    deleteReward(reward);
                }
            });

            // Edit Reward
            binding.btnEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    RewardItem reward = rewardsList.get(position);
                    editReward(reward);
                }
            });
        }

        public void bind(RewardItem reward) {
            binding.rewardTitle.setText(reward.getTitle());
            binding.rewardDescription.setText(reward.getDescription());
            binding.rewardPrice.setText(String.valueOf(reward.getPrice()));
        }

        private void markAsComplete(RewardItem reward) {
            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... voids) {
                    TaskDatabase db = TaskDatabase.getInstance(context.getApplicationContext());
                    PointsDao pointsDao = db.pointsDao();
                    Points points = pointsDao.getPoints();

                    if (points == null) {
                        points = new Points(0);
                        pointsDao.insert(points);
                    }

                    if (points.getPoints() >= reward.getPrice()) {
                        points.setPoints(points.getPoints() - reward.getPrice());
                        pointsDao.update(points);

                        if (!reward.isRepeatable()) {
                            db.rewardDao().deleteReward(reward);
                        }
                        return true;
                    } else {
                        return false;
                    }
                }

                @Override
                protected void onPostExecute(Boolean success) {
                    if (success) {
                        Snackbar.make(binding.getRoot(), "Reward claimed", Snackbar.LENGTH_LONG).show();
                        notifyDataSetChanged();
                    } else {
                        Snackbar.make(binding.getRoot(), "Not enough points", Snackbar.LENGTH_LONG).show();
                    }
                }
            }.execute();
        }

        private void editReward(RewardItem reward) {
            Bundle bundle = new Bundle();
            bundle.putInt("reward_id", reward.getId()); // Pass reward ID

            NavController navController = Navigation.findNavController((View) binding.getRoot().getParent());
            navController.navigate(R.id.nav_edit_reward, bundle); // Navigate to EditRewardFragment
        }

        private void deleteReward(RewardItem reward) {
            new AsyncTask<RewardItem, Void, Void>() {
                @Override
                protected Void doInBackground(RewardItem... rewards) {
                    TaskDatabase.getInstance(context.getApplicationContext()).rewardDao().deleteReward(rewards[0]);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    rewardsList.remove(reward);
                    notifyDataSetChanged();
                    Snackbar.make(binding.getRoot(), "Reward Deleted", Snackbar.LENGTH_LONG).show();
                }
            }.execute(reward);
        }
    }
}
