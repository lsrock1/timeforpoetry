package com.timeofpoetry.timeofpoetry.timeofpoetry;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.concerns.SharedPreferenceController;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviViews.BoardActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviViews.SettingVersionActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkTasks {

    static public class Setting{
        SettingVersionActivity callback;
        SharedPreferenceController msp;

        public Setting(SettingVersionActivity c){
            callback = c;
            msp = new SharedPreferenceController(c.getApplicationContext());
        }

        public void infoUpdate(){
            String [] data = callback.getData();
            String poet = data[0];
            String poem = data[1];
            String season = data[2];
            Call<ArrayList<PoetryClass.Response>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).addInfo(new PoetryClass.AddInfo(msp.getUserId(), msp.getUserPwd(), poet, poem, season));
            call.enqueue(new Callback<ArrayList<PoetryClass.Response>>() {
                @Override
                public void onResponse(Call<ArrayList<PoetryClass.Response>> call, Response<ArrayList<PoetryClass.Response>> response) {
                    if(PoetryClass.checkStatus(response.body())){
                        callback.successUpdate();
                    }
                    else{
                        callback.failUpdate();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<PoetryClass.Response>> call, Throwable t) {
                    callback.networkFail();
                }
            });
        }
    }
}
