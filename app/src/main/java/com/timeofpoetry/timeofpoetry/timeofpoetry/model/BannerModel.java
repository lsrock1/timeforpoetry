package com.timeofpoetry.timeofpoetry.timeofpoetry.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sangroklee on 2017. 12. 23..
 */
public class BannerModel {

    private PoetryClass.Banner cache;

    public BannerModel() {
    }

    public LiveData<PoetryClass.Banner> getBanner(){
        final MutableLiveData<PoetryClass.Banner> data = new MutableLiveData<>();

        if(cache != null){
            data.setValue(cache);
        }
        else {
            data.setValue(new PoetryClass.Banner());
            Call<ArrayList<PoetryClass.Banner>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).getBanner();
            call.enqueue(new Callback<ArrayList<PoetryClass.Banner>>() {
                @Override
                public void onResponse(Call<ArrayList<PoetryClass.Banner>> call, Response<ArrayList<PoetryClass.Banner>> response) {
                    try {
                        cache = response.body().get(0);
                        data.setValue(response.body().get(0));
                    }
                    catch (NullPointerException e){

                    }
                }

                @Override
                public void onFailure(Call<ArrayList<PoetryClass.Banner>> call, Throwable t) {

                }
            });
        }
        return data;
    }
}
