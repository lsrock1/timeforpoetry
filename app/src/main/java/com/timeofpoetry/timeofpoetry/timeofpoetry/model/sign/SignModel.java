package com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.concerns.SharedPreferenceController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by sangroklee on 2017. 11. 23..
 */
@ActivityScope
public class SignModel{

    private SharedPreferenceController sharedPreferenceController;
    public static final int UP_SUCCESS = 1;
    public static final int IN_SUCCESS = 4;
    public static final int NETWORK_FAIL = -1;
    public static final int REQUEST_FAIL = 0;
    public static final int DUP_ID = 2;
    public static final int REQUEST_READY = 3;
    private MutableLiveData<Integer> signIn = new MutableLiveData<>();
    private MutableLiveData<Integer> signUp = new MutableLiveData<>();

    @Inject
    public SignModel(SharedPreferenceController sharedPreferenceController) {
        this.sharedPreferenceController = sharedPreferenceController;
        signIn.setValue(REQUEST_READY);
        signUp.setValue(REQUEST_READY);
    }

    public LiveData<Integer> getSignInData(){
        return signIn;
    }

    public LiveData<Integer> getSignUpData(){
        return signUp;
    }

    public void signIn(final String id, final String pwd, final boolean isSocial, final int kind){
        signIn.setValue(REQUEST_READY);

        Call<List<PoetryClass.Response>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).signIn(new PoetryClass.SignIn(id, pwd));
        call.enqueue(new Callback<List<PoetryClass.Response>>() {
            @Override
            public void onResponse(Call<List<PoetryClass.Response>> call,
                                   Response<List<PoetryClass.Response>> response) {
                if(PoetryClass.checkStatus(response.body()) && sharedPreferenceController.setUserId(id) && sharedPreferenceController.setUserPwd(pwd) && sharedPreferenceController.setLogin(true)){
                    signIn.setValue(IN_SUCCESS);
                }
                else {
                    if (isSocial) {
                        signUp(id, pwd, true, kind);
                    }
                    else {
                        signIn.setValue(REQUEST_FAIL);
                    }
                }
                signIn.setValue(REQUEST_READY);
            }

            @Override
            public void onFailure(Call<List<PoetryClass.Response>> call, Throwable t) {
                signIn.setValue(NETWORK_FAIL);
                signIn.setValue(REQUEST_READY);
            }
        });
    }

    public void signUp(final String id, final String pwd, final boolean isSocial, final int kind){
        signUp.setValue(REQUEST_READY);
        Call<List<PoetryClass.Response>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).signUp(new PoetryClass.SignUp(id, pwd));
        call.enqueue(new Callback<List<PoetryClass.Response>>() {
            @Override
            public void onResponse(Call<List<PoetryClass.Response>> call, Response<List<PoetryClass.Response>> response) {
                int status = PoetryClass.checkRegister(response.body());
                if (status == 1 && sharedPreferenceController.setUserId(id) && sharedPreferenceController.setUserPwd(pwd) && sharedPreferenceController.setLogin(true)){
                    signUp.setValue(UP_SUCCESS);
                    if(isSocial)
                        setSns(id, pwd, kind);
                }
                else if(status == 2){
                    signUp.setValue(DUP_ID);
                }
                else {
                    signUp.setValue(REQUEST_FAIL);
                }
                signIn.setValue(REQUEST_READY);
            }

            @Override
            public void onFailure(Call<List<PoetryClass.Response>> call, Throwable t) {
                signUp.setValue(NETWORK_FAIL);
                signIn.setValue(REQUEST_READY);
            }
        });
    }

    public void setSns(final String id, final String pwd, int kind){
        Call<List<PoetryClass.SetSocial>> call = PoetryClass.retrofit.create(PoetryClass.ServerService.class).setSns(new PoetryClass.SetSns(id, pwd, kind));
        call.enqueue(new Callback<List<PoetryClass.SetSocial>>() {
            @Override
            public void onResponse(Call<List<PoetryClass.SetSocial>> call, Response<List<PoetryClass.SetSocial>> response) {
            }

            @Override
            public void onFailure(Call<List<PoetryClass.SetSocial>> call, Throwable t) {
            }
        });
    }
}
