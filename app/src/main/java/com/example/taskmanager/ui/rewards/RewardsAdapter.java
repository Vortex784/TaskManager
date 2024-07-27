package com.example.taskmanager.ui.rewards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.taskmanager.R;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reward, parent, false);
        return new RewardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        RewardItem reward = rewardsList.get(position);
        holder.titleTextView.setText(reward.getTitle());
        holder.descriptionTextView.setText(reward.getDescription());
        holder.priceTextView.setText(String.valueOf(reward.getPrice()));
        //holder.isRepeatable.setText(reward.isRepeatable() ? "Yes" : "No");
    }

    @Override
    public int getItemCount() {
        return rewardsList.size();
    }

    public static class RewardViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, priceTextView;

        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.reward_title);
            descriptionTextView = itemView.findViewById(R.id.reward_description);
            priceTextView = itemView.findViewById(R.id.reward_price);
        }
    }
}
