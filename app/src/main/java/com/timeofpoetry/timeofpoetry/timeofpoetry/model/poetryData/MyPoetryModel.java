package com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.concerns.SharedPreferenceController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sangroklee on 2017. 12. 19..
 */
@Singleton
public class MyPoetryModel extends PoetryModel{

    private SharedPreferenceController sharedPreferenceController;
    public final static int NETWORK_ERROR = -1;
    public final static int ALREADY = 2;
    public final static int SUCCESS = 1;
    public final static int ERROR = 0;
    public final static int CONNECTING = 3;

    private PoetryModelData cache = new PoetryModelData(new ArrayList<>());
    private MutableLiveData<PoetryModelData> liveData = new MutableLiveData<>();
    private String userId;

    @Inject
    public MyPoetryModel(SharedPreferenceController sharedPreferenceController) {
        this.sharedPreferenceController = sharedPreferenceController;
        liveData.setValue(cache);
    }

    @Override
    public LiveData<PoetryModelData> getPoetry(){
        if(sharedPreferenceController.isLogin() && userId == null || userId != null && !userId.equals(sharedPreferenceController.getUserId())){
            userId = sharedPreferenceController.getUserId();
            PoetryClass.ServerService getMyPoetry = PoetryClass.retrofit.create(PoetryClass.ServerService.class);
            Call<List<List<PoetryClass.Poem>>> call = getMyPoetry.getMyPoetry(new PoetryClass.GetMyPoetry(sharedPreferenceController.getUserId(), sharedPreferenceController.getUserPwd()));
            call.enqueue(new Callback<List<List<PoetryClass.Poem>>>() {
                @Override
                public void onResponse(Call<List<List<PoetryClass.Poem>>> call,
                                       Response<List<List<PoetryClass.Poem>>> response) {
                    List<PoetryClass.Poem> mValues = response.body().get(0);

                    if (mValues.get(0).getVoice() != null || mValues.get(0).getPoet() != null) {
                        cache.setNewArray(mValues, true);
                        liveData.setValue(cache);
                    }
                }

                @Override
                public void onFailure(Call<List<List<PoetryClass.Poem>>> call, Throwable t) {
                }
            });
        }
        return liveData;
    }

    private void removeRequest(PoetryClass.Poem poem){
        PoetryClass.ServerService delMyPoetry = PoetryClass.retrofit.create(PoetryClass.ServerService.class);
        Call<List<PoetryClass.Response>> call = delMyPoetry.setMyPoetry(new PoetryClass.SetMyPoetry("del", sharedPreferenceController.getUserId(), sharedPreferenceController.getUserPwd(), poem.getPoet(), poem.getPoem(), poem.getVoice()));
        call.enqueue(new Callback<List<PoetryClass.Response>>() {
            @Override
            public void onResponse(Call<List<PoetryClass.Response>> call,
                                   Response<List<PoetryClass.Response>> response) {
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
            public void onFailure(Call<List<PoetryClass.Response>> call, Throwable t) {
            }
        });
    }

    private void bookMark(final PoetryClass.Poem poem, final MutableLiveData<Integer> data){
        Call<List<PoetryClass.Response>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).setMyPoetry(new PoetryClass.SetMyPoetry("insert", sharedPreferenceController.getUserId(), sharedPreferenceController.getUserPwd(), poem.getPoet(), poem.getPoem(), poem.getVoice()));
        call.enqueue(new Callback<List<PoetryClass.Response>>() {
            @Override
            public void onResponse(Call<List<PoetryClass.Response>> call,
                                   Response<List<PoetryClass.Response>> response) {
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
            public void onFailure(Call<List<PoetryClass.Response>> call, Throwable t) {
                data.setValue(NETWORK_ERROR);
            }
        });
    }

    public LiveData<Integer> checkInBookMark(final PoetryClass.Poem poem){
        final MutableLiveData<Integer> data = new MutableLiveData<>();
        data.setValue(CONNECTING);
        Call<List<PoetryClass.Response>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).isMyPoetry(new PoetryClass.IsMyPoetry(sharedPreferenceController.getUserId(), poem.getPoet(), poem.getPoem(), poem.getVoice()));
        call.enqueue(new Callback<List<PoetryClass.Response>>() {
            @Override
            public void onResponse(Call<List<PoetryClass.Response>> call,
                                   Response<List<PoetryClass.Response>> response) {
                if (response.body().get(0).getStatus().equals("True")) {
                    data.setValue(ALREADY);
                } else {
                    bookMark(poem, data);
                }
            }

            @Override
            public void onFailure(Call<List<PoetryClass.Response>> call, Throwable t) {
                data.setValue(NETWORK_ERROR);
            }
        });
        return data;
    }

    @Override
    public void removePoetry(List<PoetryClass.Poem> poetry) {
        ListIterator<PoetryClass.Poem> itr = poetry.listIterator();

        while(itr.hasNext()){
            PoetryClass.Poem poem = itr.next();
            if(poem.getIsSelected().get()){
                removeRequest(poem);
                itr.remove();
            }
        }

        cache.setNewArray(poetry, false);
        liveData.setValue(cache);
    }
}
