package com.example.taskmanager.ui.create;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateRewardViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CreateRewardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is create reward fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}