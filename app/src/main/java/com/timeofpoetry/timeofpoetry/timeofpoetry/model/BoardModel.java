package com.timeofpoetry.timeofpoetry.timeofpoetry.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.util.ArrayMap;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sangroklee on 2018. 1. 16..
 */
@ActivityScope
public class BoardModel {

    private ArrayMap<Integer, String> cache = new ArrayMap<>();

    @Inject
    public BoardModel() {
    }

    public LiveData<List<PoetryClass.BoardIdItem>> getList(){
        final MutableLiveData<List<PoetryClass.BoardIdItem>> data = new MutableLiveData<>();
        Call<List<List<PoetryClass.BoardIdItem>>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).getNoticeId(new PoetryClass.GetNoticeId());
        call.enqueue(new Callback<List<List<PoetryClass.BoardIdItem>>>() {
            @Override
            public void onResponse(Call<List<List<PoetryClass.BoardIdItem>>> call, Response<List<List<PoetryClass.BoardIdItem>>> response) {
                try{
                    data.setValue(response.body().get(0));
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<List<PoetryClass.BoardIdItem>>> call, Throwable t) {
            }
        });
        return data;
    }

    public void getContent(int id, ObservableField<String> content){
        if(cache.get(id) != null){
            content.set(cache.get(id));
        }
        else {
            Call<List<List<PoetryClass.BoardIdItem>>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).getNoticeBody(new PoetryClass.GetNoticeBody(id));
            call.enqueue(new Callback<List<List<PoetryClass.BoardIdItem>>>() {
                @Override
                public void onResponse(Call<List<List<PoetryClass.BoardIdItem>>> call, retrofit2.Response<List<List<PoetryClass.BoardIdItem>>> response) {
                    cache.put(id, response.body().get(0).get(0).getContent());
                    content.set(response.body().get(0).get(0).getContent());
                }

                @Override
                public void onFailure(Call<List<List<PoetryClass.BoardIdItem>>> call, Throwable t) {

                }
            });
        }
    }

}
