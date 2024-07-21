package com.example.taskmanager.ui.rewards;

import java.util.ArrayList;
import java.util.List;

public class RewardList {
    private List<RewardItem> rewards;

    public RewardList() {
        rewards = new ArrayList<>();
    }

    public void addReward(RewardItem reward) {
        rewards.add(reward);}

    public List<RewardItem> getRewards() {
        return rewards;
    }


}
