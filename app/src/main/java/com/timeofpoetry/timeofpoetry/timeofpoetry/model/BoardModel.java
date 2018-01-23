package com.timeofpoetry.timeofpoetry.timeofpoetry.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.util.ArrayMap;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sangroklee on 2018. 1. 16..
 */

public class BoardModel {

    private ArrayMap<Integer, String> cache = new ArrayMap<>();

    public LiveData<ArrayList<PoetryClass.BoardIdItem>> getList(){
        final MutableLiveData<ArrayList<PoetryClass.BoardIdItem>> data = new MutableLiveData<>();
        Call<ArrayList<ArrayList<PoetryClass.BoardIdItem>>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).getNoticeId(new PoetryClass.GetNoticeId());
        call.enqueue(new Callback<ArrayList<ArrayList<PoetryClass.BoardIdItem>>>() {
            @Override
            public void onResponse(Call<ArrayList<ArrayList<PoetryClass.BoardIdItem>>> call, Response<ArrayList<ArrayList<PoetryClass.BoardIdItem>>> response) {
                try{
                    data.setValue(response.body().get(0));
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ArrayList<PoetryClass.BoardIdItem>>> call, Throwable t) {
            }
        });
        return data;
    }

    public void getContent(int id, ObservableField<String> content){
        if(cache.get(id) != null){
            content.set(cache.get(id));
        }
        else {
            Call<ArrayList<ArrayList<PoetryClass.BoardIdItem>>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).getNoticeBody(new PoetryClass.GetNoticeBody(id));
            call.enqueue(new Callback<ArrayList<ArrayList<PoetryClass.BoardIdItem>>>() {
                @Override
                public void onResponse(Call<ArrayList<ArrayList<PoetryClass.BoardIdItem>>> call, retrofit2.Response<ArrayList<ArrayList<PoetryClass.BoardIdItem>>> response) {
                    cache.put(id, response.body().get(0).get(0).getContent());
                    content.set(response.body().get(0).get(0).getContent());
                }

                @Override
                public void onFailure(Call<ArrayList<ArrayList<PoetryClass.BoardIdItem>>> call, Throwable t) {

                }
            });
        }
    }

}
