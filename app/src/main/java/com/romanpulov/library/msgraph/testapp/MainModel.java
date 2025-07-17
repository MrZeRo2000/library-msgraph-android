package com.romanpulov.library.msgraph.testapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainModel extends ViewModel {
    public enum Status {
        STATUS_SUCCESS,
        STATUS_FAILURE
    }
    public record ResultState(Status status, String text) {}

    private final MutableLiveData<ResultState> resultState = new MutableLiveData<>();

    public LiveData<ResultState> getResultState() {
        return resultState;
    }

    public void setResultState(ResultState resultState) {
        this.resultState.postValue(resultState);
    }
}
