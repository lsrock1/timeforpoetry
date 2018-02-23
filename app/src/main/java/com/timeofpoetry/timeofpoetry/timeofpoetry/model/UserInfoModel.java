package com.timeofpoetry.timeofpoetry.timeofpoetry.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by sangroklee on 2018. 1. 17..
 */
@ActivityScope
public class UserInfoModel {

    public static final int SUCCESS = 1;
    public static final int UPDATING = 2;
    public static final int FAIL = -1;
    public static final int NETWORK_ERROR = 0;
    public static final int READY = -2;

    private MutableLiveData<Integer> data = new MutableLiveData<>();

    @Inject
    public UserInfoModel() {
        data.setValue(READY);
    }

    public LiveData<Integer> getData(){
        return data;
    }

    public void infoUpdate(String id, String pwd, String poet, String poem, String season){
        if(data.getValue() != UPDATING) {
            data.setValue(UPDATING);
            Call<List<PoetryClass.Response>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).addInfo(new PoetryClass.AddInfo(id, pwd, poet, poem, season));
            call.enqueue(new Callback<List<PoetryClass.Response>>() {
                @Override
                public void onResponse(Call<List<PoetryClass.Response>> call, Response<List<PoetryClass.Response>> response) {
                    if (PoetryClass.checkStatus(response.body())) {
                        data.setValue(SUCCESS);
                    } else {
                        data.setValue(FAIL);
                    }
                }

                @Override
                public void onFailure(Call<List<PoetryClass.Response>> call, Throwable t) {
                    data.setValue(NETWORK_ERROR);
                }
            });
        }
    }

    public LiveData<PoetryClass.GetAddInfo> infoLoad(String id, String pwd){
        MutableLiveData<PoetryClass.GetAddInfo> data = new MutableLiveData<>();
        Call<List<List<PoetryClass.GetAddInfo>>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).getAddInfo(new PoetryClass.RequestAddInfo(id, pwd));
        call.enqueue(new Callback<List<List<PoetryClass.GetAddInfo>>>() {
            @Override
            public void onResponse(Call<List<List<PoetryClass.GetAddInfo>>> call, Response<List<List<PoetryClass.GetAddInfo>>> response) {
                data.setValue(response.body().get(0).get(0));
            }

            @Override
            public void onFailure(Call<List<List<PoetryClass.GetAddInfo>>> call, Throwable t) {

            }
        });

        return data;
    }
}
