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
 * viewmodel/naviView/BoardActivityViewModel 에서 사용
 * 공지사항 데이터를 실제로 가져오는 모델
 * getlist는 공지사항의 제목들을 한번에 가져오며
 * getContent는 해당 공지사항의 아이디를 가지고 실제 내용을 가져온다
 */
@ActivityScope
public class BoardModel {

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
        Call<List<List<PoetryClass.BoardIdItem>>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).getNoticeBody(new PoetryClass.GetNoticeBody(id));
        call.enqueue(new Callback<List<List<PoetryClass.BoardIdItem>>>() {
            @Override
            public void onResponse(Call<List<List<PoetryClass.BoardIdItem>>> call, retrofit2.Response<List<List<PoetryClass.BoardIdItem>>> response) {
                content.set(response.body().get(0).get(0).getContent());
            }

            @Override
            public void onFailure(Call<List<List<PoetryClass.BoardIdItem>>> call, Throwable t) {

            }
        });
    }

}
