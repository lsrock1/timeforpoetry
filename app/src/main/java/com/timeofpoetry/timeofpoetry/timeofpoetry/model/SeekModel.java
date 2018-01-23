package com.timeofpoetry.timeofpoetry.timeofpoetry.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;

import com.timeofpoetry.timeofpoetry.timeofpoetry.model.concerns.SharedPreferenceController;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sangroklee on 2018. 1. 3..
 */
public class SeekModel{

    private SharedPreferenceController sharedPreferenceController;
    private MutableLiveData<Integer> seek = new MutableLiveData<>();

    public SeekModel(SharedPreferenceController sharedPreferenceController) {
        this.sharedPreferenceController = sharedPreferenceController;
        seek.setValue(sharedPreferenceController.getLastSeek());
    }

    public void saveSeek(){
        sharedPreferenceController.setLastSeek(seek.getValue());
    }

    public void setSeek(int pos){
        seek.setValue(pos);
    }

    public LiveData<Integer> getSeek(){
        return seek;
    }
}
