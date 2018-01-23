package com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData;

import android.arch.lifecycle.MutableLiveData;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sangroklee on 2018. 1. 3..
 */
public class RecommendModel {

    private ArrayList<PoetryClass.Poem> recommendCache;

    public RecommendModel() {
    }

    public MutableLiveData<ArrayList<PoetryClass.Poem>> getRecommend() {
        final MutableLiveData<ArrayList<PoetryClass.Poem>> data = new MutableLiveData<>();
        if(recommendCache != null){
            data.setValue(recommendCache);
        }
        else {
            recommendCache = new ArrayList<>();
            data.setValue(recommendCache);
            Call<ArrayList<ArrayList<PoetryClass.Poem>>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).nowPoetry(new PoetryClass.NowPoetry());
            call.enqueue(new Callback<ArrayList<ArrayList<PoetryClass.Poem>>>() {
                @Override
                public void onResponse(Call<ArrayList<ArrayList<PoetryClass.Poem>>> call, Response<ArrayList<ArrayList<PoetryClass.Poem>>> response) {
                    recommendCache = response.body().get(0);
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
