package com.maxte.taskmanager.ui.rewards;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RewardsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RewardsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is rewards fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}