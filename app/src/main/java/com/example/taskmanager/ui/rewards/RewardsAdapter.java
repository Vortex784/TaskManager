package com.example.taskmanager.ui.rewards;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.taskmanager.database.Points;
import com.example.taskmanager.database.PointsDao;
import com.example.taskmanager.database.TaskDatabase;
import com.example.taskmanager.databinding.ItemRewardBinding;
import com.example.taskmanager.ui.rewards.RewardItem;
import com.example.taskmanager.ui.rewards.RewardItem;
import com.example.taskmanager.database.TaskDatabase;
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
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
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
                            deleteReward(reward);
                        }
                        Snackbar.make(binding.getRoot().getRootView(), "Reward claimed", Snackbar.LENGTH_LONG).show();
                        return null;
                    }
                    Snackbar.make(binding.getRoot().getRootView(), "Not enough points", Snackbar.LENGTH_LONG).show();
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    notifyDataSetChanged();

                }
            }.execute();
        }

        private void deleteReward(RewardItem reward) {
            // Remove the reward from the database
            new AsyncTask<RewardItem, Void, Void>() {
                @Override
                protected Void doInBackground(RewardItem... rewards) {
                    TaskDatabase.getInstance(itemView.getContext()).rewardDao().deleteReward(rewards[0]);
                    Snackbar.make(binding.getRoot().getRootView(), "Reward Deleted", Snackbar.LENGTH_LONG).show();
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
