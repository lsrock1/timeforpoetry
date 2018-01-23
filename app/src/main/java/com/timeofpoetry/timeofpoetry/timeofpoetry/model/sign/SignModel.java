package com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.concerns.SharedPreferenceController;

import java.util.ArrayList;

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
        Call<ArrayList<PoetryClass.Response>> call = PoetryClass.retrofit.create(SignService.class).signIn(new SignIn(id, pwd));
        call.enqueue(new Callback<ArrayList<PoetryClass.Response>>() {
            @Override
            public void onResponse(Call<ArrayList<PoetryClass.Response>> call,
                                   Response<ArrayList<PoetryClass.Response>> response) {
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
            public void onFailure(Call<ArrayList<PoetryClass.Response>> call, Throwable t) {
                signIn.setValue(NETWORK_FAIL);
                signIn.setValue(REQUEST_READY);
            }
        });
    }

    public void signUp(final String id, final String pwd, final boolean isSocial, final int kind){
        signUp.setValue(REQUEST_READY);
        Call<ArrayList<PoetryClass.Response>> call = PoetryClass.retrofit.create(SignService.class).signUp(new SignUp(id, pwd));
        call.enqueue(new Callback<ArrayList<PoetryClass.Response>>() {
            @Override
            public void onResponse(Call<ArrayList<PoetryClass.Response>> call, Response<ArrayList<PoetryClass.Response>> response) {
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
            public void onFailure(Call<ArrayList<PoetryClass.Response>> call, Throwable t) {
                signUp.setValue(NETWORK_FAIL);
                signIn.setValue(REQUEST_READY);
            }
        });
    }

    public void setSns(final String id, final String pwd, int kind){
        Call<ArrayList<PoetryClass.SetSocial>> call = PoetryClass.retrofit.create(SignService.class).setSns(new SetSns(id, pwd, kind));
        call.enqueue(new Callback<ArrayList<PoetryClass.SetSocial>>() {
            @Override
            public void onResponse(Call<ArrayList<PoetryClass.SetSocial>> call, Response<ArrayList<PoetryClass.SetSocial>> response) {
            }

            @Override
            public void onFailure(Call<ArrayList<PoetryClass.SetSocial>> call, Throwable t) {
            }
        });
    }

    interface SignService{
        @Headers({"Accept: application/json"})
        @POST("/tfp_regist")
        Call<ArrayList<PoetryClass.Response>> signUp(@Body SignUp body);

        @Headers({"Accept: application/json"})
        @POST("/tfp_login")
        Call<ArrayList<PoetryClass.Response>> signIn(@Body SignIn body);

        @Headers({"Accept: application/json"})
        @POST("/get_sns")
        Call<ArrayList<PoetryClass.GetSocial>> getSns(@Body GetSns body);

        @Headers({"Accept: application/json"})
        @POST("/set_sns")
        Call<ArrayList<PoetryClass.SetSocial>> setSns(@Body SetSns body);
    }

    static class SignUp{
        final String request = PoetryClass.REQUEST_MEMBER;
        ArrayList<PoetryClass.KeyValue> regist = new ArrayList<PoetryClass.KeyValue>();

        SignUp(String email, String pwd){
            this.regist.add(new PoetryClass.KeyValue(PoetryClass.E_MAIL_KEY, email));
            this.regist.add(new PoetryClass.KeyValue(PoetryClass.PASSWORD_KEY, pwd));
        }
    }

    static class SignIn{
        final String request = PoetryClass.REQUEST_MEMBER;
        ArrayList<PoetryClass.KeyValue> login = new ArrayList<>();

        SignIn(String email, String pwd){
            this.login.add(new PoetryClass.KeyValue(PoetryClass.E_MAIL_KEY, email));
            this.login.add(new PoetryClass.KeyValue(PoetryClass.PASSWORD_KEY, pwd));
        }
    }

    static class SetSns{
        final String request = "SNS";
        ArrayList<PoetryClass.KeyValue> create = new ArrayList<>();

        SetSns(String email, String pwd, int kind){
            this.create.add(new PoetryClass.KeyValue(PoetryClass.E_MAIL_KEY, email));
            this.create.add(new PoetryClass.KeyValue(PoetryClass.PASSWORD_KEY, pwd));
            this.create.add(new PoetryClass.KeyValue("r_social_kind", kind == 0 ? "KAKAO/TALK" : "facebook"));
            this.create.add(new PoetryClass.KeyValue("r_auth_token", ""));
            this.create.add(new PoetryClass.KeyValue("r_access_token", ""));
            this.create.add(new PoetryClass.KeyValue("r_refresh_token", ""));
        }
    }

    static class GetSns{
        final String request = "SNS";
        ArrayList<PoetryClass.KeyValue> search = new ArrayList<>();

        GetSns(String email, String pwd, int kind){
            this.search.add(new PoetryClass.KeyValue(PoetryClass.E_MAIL_KEY, email));
            this.search.add(new PoetryClass.KeyValue(PoetryClass.PASSWORD_KEY, pwd));
            this.search.add(new PoetryClass.KeyValue("r_social_kind", kind == 0 ? "KAKAO/TALK" : "FACEBOOK"));
        }
    }
}
