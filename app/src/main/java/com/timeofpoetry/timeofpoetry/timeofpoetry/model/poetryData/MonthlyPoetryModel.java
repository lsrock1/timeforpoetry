package com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityScope;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@ActivityScope
public class MonthlyPoetryModel extends PoetryModel{

    private PoetryModelData cache;

    @Inject
    public MonthlyPoetryModel() {
    }

    @Override
    public LiveData<PoetryModelData> getPoetry(){
        final MutableLiveData<PoetryModelData> poetry = new MutableLiveData<>();

        Calendar cal = Calendar.getInstance();
        String yearMonth[] = new SimpleDateFormat("yyyy/MM", Locale.KOREA).format(cal.getTime()).split("/");

        if(cache != null){
            poetry.setValue(cache);
        }
        else {
            PoetryClass.ServerService getMonthly = PoetryClass.retrofit.create(PoetryClass.ServerService.class);
            Call<List<List<PoetryClass.Poem>>> call = getMonthly.getMonthlyPoetry(new PoetryClass.GetMonthlyPoetry(yearMonth));

            call.enqueue(new Callback<List<List<PoetryClass.Poem>>>() {
                @Override
                public void onResponse(Call<List<List<PoetryClass.Poem>>> call,
                                       Response<List<List<PoetryClass.Poem>>> response) {
                    cache = new PoetryModelData(response.body().get(0));
                    poetry.setValue(cache);
                }

                @Override
                public void onFailure(Call<List<List<PoetryClass.Poem>>> call, Throwable t) {
                }
            });
        }
        return poetry;
    }
}
