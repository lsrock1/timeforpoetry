package com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData;

import android.arch.lifecycle.MutableLiveData;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityScope;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sangroklee on 2018. 1. 3..
 */
@ActivityScope
public class RankModel {

    private ArrayList<PoetryClass.Poem> rankCache;

    @Inject
    public RankModel() {
    }

    public MutableLiveData<ArrayList<PoetryClass.Poem>> getRank(){
        final MutableLiveData<ArrayList<PoetryClass.Poem>> data = new MutableLiveData<>();

        if(rankCache != null){
            data.setValue(rankCache);
        }
        else {
            rankCache = new ArrayList<>();
            data.setValue(rankCache);
            Call<ArrayList<ArrayList<PoetryClass.Poem>>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).getRank();
            call.enqueue(new Callback<ArrayList<ArrayList<PoetryClass.Poem>>>() {
                @Override
                public void onResponse(Call<ArrayList<ArrayList<PoetryClass.Poem>>> call, Response<ArrayList<ArrayList<PoetryClass.Poem>>> response) {
                    rankCache = response.body().get(0);
                    data.setValue(response.body().get(0));
                }

                @Override
                public void onFailure(Call<ArrayList<ArrayList<PoetryClass.Poem>>> call, Throwable t) {

                }
            });
        }
        return data;
    }
}
