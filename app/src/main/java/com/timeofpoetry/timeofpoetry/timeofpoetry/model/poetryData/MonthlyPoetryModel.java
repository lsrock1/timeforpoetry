package com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class MonthlyPoetryModel {

    private ArrayList<PoetryClass.Poem> cache;

    public MonthlyPoetryModel() {
    }

    public MutableLiveData<ArrayList<PoetryClass.Poem>> getMonthlyPoetry(){
        final MutableLiveData<ArrayList<PoetryClass.Poem>> poetry = new MutableLiveData<>();

        Calendar cal = Calendar.getInstance();
        String yearMonth[] = new SimpleDateFormat("yyyy/MM", Locale.KOREA).format(cal.getTime()).split("/");

        if(cache != null){
            poetry.setValue(cache);
        }
        else {
            poetry.setValue(new ArrayList<PoetryClass.Poem>());
            ServerService getMonthly = PoetryClass.retrofit.create(ServerService.class);
            Call<ArrayList<ArrayList<PoetryClass.Poem>>> call = getMonthly.getMonthlyPoetry(new PoetryClass.GetMonthlyPoetry(yearMonth));

            call.enqueue(new Callback<ArrayList<ArrayList<PoetryClass.Poem>>>() {
                @Override
                public void onResponse(Call<ArrayList<ArrayList<PoetryClass.Poem>>> call,
                                       Response<ArrayList<ArrayList<PoetryClass.Poem>>> response) {
                    cache = response.body().get(0);
                    poetry.setValue(response.body().get(0));
                }

                @Override
                public void onFailure(Call<ArrayList<ArrayList<PoetryClass.Poem>>> call, Throwable t) {
                }
            });
        }
        return poetry;
    }

    public ArrayList<PoetryClass.Poem> getSelectedItems(){
        ArrayList<PoetryClass.Poem> tmp = new ArrayList<>();
        for(PoetryClass.Poem poem : cache){
            if(poem.getIsSelected().get()) tmp.add(poem.clone());
        }

        return tmp;
    }

    public void setSelectAll(boolean bool){
        for(PoetryClass.Poem poem : cache){
            poem.setIsSelect(bool);
        }
    }

    public interface ServerService {
        @Headers({"content-type: application/json"})
        @POST("/tfp_getmonthly")
        Call<ArrayList<ArrayList<PoetryClass.Poem>>> getMonthlyPoetry(@Body PoetryClass.GetMonthlyPoetry body);
    }
}
