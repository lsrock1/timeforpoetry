package com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.ArrayMap;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sangroklee on 2017. 12. 23..
 */
public class LikeModel {

    private ArrayMap<String, Integer> cache = new ArrayMap<>();

    public LikeModel() {
    }

    public LiveData<Integer> updateTxt(final PoetryClass.Poem poem){
        //캐시가 있으면 livedata에 캐시값을 넣어서 바로 리턴하고
        //캐시가 없으면 요청하되 livedata nullexception이 일어나지 않게 0으로 일단 초기화
        //서버에서 like 값 요청이 성공하면
        //미리 눌러둔 like 추가 값이 있을 경우 찾아서 더하고 없애주고
        //livedata에 set해서 알린다
        final MutableLiveData<Integer> data = new MutableLiveData<>();
        if(cache.get(poem.getPoem()) != null){
            data.setValue(cache.get(poem.getPoem()));
        }
        else {

            data.setValue(0);
            Call<ArrayList<ArrayList<PoetryClass.Poem>>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).getPlayByList(new PoetryClass.GetPlayByList(poem.getPoet(), poem.getPoem(), poem.getVoice()));
            call.enqueue(new Callback<ArrayList<ArrayList<PoetryClass.Poem>>>() {
                @Override
                public void onResponse(Call<ArrayList<ArrayList<PoetryClass.Poem>>> call, Response<ArrayList<ArrayList<PoetryClass.Poem>>> response) {
                    try {
                        int tmpInt = 0;
                        if(cache.get("tmp") != null){
                            tmpInt = cache.get("tmp");
                            cache.remove("tmp");
                        }
                        cache.put(poem.getPoem(), response.body().get(0).get(0).getLike() + tmpInt);
                        data.setValue(response.body().get(0).get(0).getLike() + tmpInt);
                    } catch (NullPointerException e) {
                        data.setValue(0);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ArrayList<PoetryClass.Poem>>> call, Throwable t) {
                }
            });
        }
        return data;
    }

    public void like(PoetryClass.Poem poem){
        //캐시 데이터에 있는 like 값을 1 증가시키고
        //서버에 like 요청을 합니다
        //만약 시의 like 값이 초기화 되기 전에 성격 급한 유저가 like를 누르면 캐시 데이터가 없기 때문에
        //일단 tmp라는 키로 like값을 1 증가시킵니다(혹시 like를 많이 증가시킬 수도 있어서 1 이상에도 안전하게 만듬)
        if(cache.get(poem.getPoem()) == null){
            cache.put("tmp", cache.get("tmp") == null ? 0 : cache.get("tmp") + 1);
        }
        else {
            cache.put(poem.getPoem(), cache.get(poem.getPoem()) + 1);
        }
        Call<ArrayList<PoetryClass.Response>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).like(new PoetryClass.Like(poem.getPoet(), poem.getPoem(), poem.getVoice()));
        call.enqueue(new Callback<ArrayList<PoetryClass.Response>>() {
            @Override
            public void onResponse(Call<ArrayList<PoetryClass.Response>> call,
                                   Response<ArrayList<PoetryClass.Response>> response) {
            }

            @Override
            public void onFailure(Call<ArrayList<PoetryClass.Response>> call, Throwable t) {
            }
        });

    }

    public void unlike(PoetryClass.Poem poem){
        Call<ArrayList<PoetryClass.Response>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).like(new PoetryClass.Like(poem.getPoet(), poem.getPoem(), poem.getVoice()));
        call.enqueue(new Callback<ArrayList<PoetryClass.Response>>() {
            @Override
            public void onResponse(Call<ArrayList<PoetryClass.Response>> call,
                                   Response<ArrayList<PoetryClass.Response>> response) {
            }

            @Override
            public void onFailure(Call<ArrayList<PoetryClass.Response>> call, Throwable t) {
            }
        });
    }
}
