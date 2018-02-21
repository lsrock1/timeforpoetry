package com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.DiffCallback;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.concerns.SharedPreferenceController;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sangroklee on 2017. 12. 19..
 */
@ActivityScope
public class MyPoetryModel {

    private SharedPreferenceController sharedPreferenceController;
    public final static int NETWORK_ERROR = -1;
    public final static int ALREADY = 2;
    public final static int SUCCESS = 1;
    public final static int ERROR = 0;
    public final static int CONNECTING = 3;
    private PoetryModelData cache = new PoetryModelData(new ArrayList<PoetryClass.Poem>());
    private MutableLiveData<PoetryModelData> liveData = new MutableLiveData<>();
    private String userId;

    @Inject
    public MyPoetryModel(SharedPreferenceController sharedPreferenceController) {
        this.sharedPreferenceController = sharedPreferenceController;
        liveData.setValue(cache);
    }

    public LiveData<PoetryModelData> getMyPoetryList(){
        if(sharedPreferenceController.isLogin() && userId == null || userId != null && !userId.equals(sharedPreferenceController.getUserId())){
            userId = sharedPreferenceController.getUserId();
            PoetryClass.ServerService getMyPoetry = PoetryClass.retrofit.create(PoetryClass.ServerService.class);
            Call<ArrayList<ArrayList<PoetryClass.Poem>>> call = getMyPoetry.getMyPoetry(new PoetryClass.GetMyPoetry(sharedPreferenceController.getUserId(), sharedPreferenceController.getUserPwd()));
            call.enqueue(new Callback<ArrayList<ArrayList<PoetryClass.Poem>>>() {
                @Override
                public void onResponse(Call<ArrayList<ArrayList<PoetryClass.Poem>>> call,
                                       Response<ArrayList<ArrayList<PoetryClass.Poem>>> response) {
                    ArrayList<PoetryClass.Poem> mValues = response.body().get(0);

                    if (mValues.get(0).getVoice() != null || mValues.get(0).getPoet() != null) {
                        cache.setNewArray(mValues, true);
                        liveData.setValue(cache);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ArrayList<PoetryClass.Poem>>> call, Throwable t) {
                }
            });
        }
        return liveData;
    }

    public void removeRequest(PoetryClass.Poem poem){
        PoetryClass.ServerService delMyPoetry = PoetryClass.retrofit.create(PoetryClass.ServerService.class);
        Call<ArrayList<PoetryClass.Response>> call = delMyPoetry.setMyPoetry(new PoetryClass.SetMyPoetry("del", sharedPreferenceController.getUserId(), sharedPreferenceController.getUserPwd(), poem.getPoet(), poem.getPoem(), poem.getVoice()));
        call.enqueue(new Callback<ArrayList<PoetryClass.Response>>() {
            @Override
            public void onResponse(Call<ArrayList<PoetryClass.Response>> call,
                                   Response<ArrayList<PoetryClass.Response>> response) {
//                if(PoetryClass.checkStatus(response.body())){
//                    isRemoved.setValue(isRemoved.getValue() + 1);
//                    removeList.remove(0);
//                }
//                if(removeList.size() != 0){
//                    removeRequest(removeList, isRemoved);
//                }
//                else{
//                    removeEnd(isRemoved);
//                }
            }

            @Override
            public void onFailure(Call<ArrayList<PoetryClass.Response>> call, Throwable t) {
            }
        });
    }

    private void bookMark(final PoetryClass.Poem poem, final MutableLiveData<Integer> data){
        Call<ArrayList<PoetryClass.Response>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).setMyPoetry(new PoetryClass.SetMyPoetry("insert", sharedPreferenceController.getUserId(), sharedPreferenceController.getUserPwd(), poem.getPoet(), poem.getPoem(), poem.getVoice()));
        call.enqueue(new Callback<ArrayList<PoetryClass.Response>>() {
            @Override
            public void onResponse(Call<ArrayList<PoetryClass.Response>> call,
                                   Response<ArrayList<PoetryClass.Response>> response) {
                if(PoetryClass.checkStatus(response.body())){
                    data.setValue(SUCCESS);
                    cache.addOnePoem(poem);
                    liveData.setValue(cache);
                }
                else{
                    data.setValue(ERROR);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PoetryClass.Response>> call, Throwable t) {
                data.setValue(NETWORK_ERROR);
            }
        });
    }

    public LiveData<Integer> checkInBookMark(final PoetryClass.Poem poem){
        final MutableLiveData<Integer> data = new MutableLiveData<>();
        data.setValue(CONNECTING);
        Call<ArrayList<PoetryClass.Response>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).isMyPoetry(new PoetryClass.IsMyPoetry(sharedPreferenceController.getUserId(), poem.getPoet(), poem.getPoem(), poem.getVoice()));
        call.enqueue(new Callback<ArrayList<PoetryClass.Response>>() {
            @Override
            public void onResponse(Call<ArrayList<PoetryClass.Response>> call,
                                   Response<ArrayList<PoetryClass.Response>> response) {
                if (response.body().get(0).getStatus().equals("True")) {
                    data.setValue(ALREADY);
                } else {
                    bookMark(poem, data);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PoetryClass.Response>> call, Throwable t) {
                data.setValue(NETWORK_ERROR);
            }
        });
        return data;
    }

    public void setSelectAll(boolean bool){
        for(PoetryClass.Poem poem : cache.getPoetry()){
            poem.setIsSelect(bool);
        }
    }

    public ArrayList<PoetryClass.Poem> getSelectedItems(){
        ArrayList<PoetryClass.Poem> tmp = new ArrayList<>();
        for(PoetryClass.Poem poem : cache.getPoetry()){
            if(poem.getIsSelected().get()) tmp.add(poem.clone());
        }

        return tmp;
    }

    public void selectedItemsRemove(){
        int start = 0;
        ArrayList<PoetryClass.Poem> data = new ArrayList<>(cache.getPoetry());
        while(start < data.size()){
            PoetryClass.Poem poem = data.get(start);
            if(poem.getIsSelected().get()){
                data.remove(start);
                removeRequest(poem);
            }
            else{
                start++;
            }
        }
        cache.setNewArray(data, false);
        liveData.setValue(cache);
    }
}
