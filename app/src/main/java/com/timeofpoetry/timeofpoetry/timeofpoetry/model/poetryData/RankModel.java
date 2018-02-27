package com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sangroklee on 2018. 1. 3..
 */
@ActivityScope
public class RankModel extends PoetryModel{

    private PoetryModelData rankCache;

    @Inject
    public RankModel() {
    }

    @Override
    public LiveData<PoetryModelData> getPoetry(){
        final MutableLiveData<PoetryModelData> data = new MutableLiveData<>();

        if(rankCache != null){
            data.setValue(rankCache);
        }
        else {
            Call<List<List<PoetryClass.Poem>>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).getRank();
            call.enqueue(new Callback<List<List<PoetryClass.Poem>>>() {
                @Override
                public void onResponse(Call<List<List<PoetryClass.Poem>>> call, Response<List<List<PoetryClass.Poem>>> response) {
                    rankCache = new PoetryModelData(response.body().get(0));
                    data.setValue(rankCache);
                }

                @Override
                public void onFailure(Call<List<List<PoetryClass.Poem>>> call, Throwable t) {

                }
            });
        }
        return data;
    }
}
