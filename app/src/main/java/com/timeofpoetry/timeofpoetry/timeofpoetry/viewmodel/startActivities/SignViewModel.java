package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign.SignModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

public class SignViewModel extends ViewModel implements LifecycleObserver{

    public ObservableBoolean isUp = new ObservableBoolean(false);
    private boolean isStack = false;
    private static int KAKAO_CODE = 0;
    private static int FACEBOOK_CODE = 1;
    private SessionCallback callback;
    private CallbackManager callbackManager;
    private SignModel model;

    SignViewModel(SignModel signModel) {
        model = signModel;
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        callbackManager = CallbackManager.Factory.create();
    }

    public LiveData<Integer> getSignInStatus(){
        return model.getSignInData();
    }

    public LiveData<Integer> getSignUpStatus(){
        return model.getSignUpData();
    }

    public void setMode(boolean mode){
        isUp.set(mode);
    }

    public boolean getStack(){
        return isStack;
    }

    public void onBackPressed(){
        isStack = false;
        changeMode();
    }

    public void toSignUp(){
        changeMode();
        isStack = true;
    }

    public void socialLogOut(){
        if (AccessToken.getCurrentAccessToken() != null)
            LoginManager.getInstance().logOut();
        if (Session.getCurrentSession().isOpened())
            UserManagement.requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                    return;
                }
            });
    }

    private void changeMode(){
        isUp.set(!isUp.get());
    }

    private void requestMe() {
        List<String> propertyKeys = new ArrayList<String>();
        propertyKeys.add("kaccount_email");

        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                model.signIn(userProfile.getEmail(), Long.toString(userProfile.getId()), true, KAKAO_CODE);
            }

            @Override
            public void onNotSignedUp() {
                showSignup();
            }
        }, propertyKeys, false);
    }

    private void showSignup() {

        UserManagement.requestSignup(new ApiResponseCallback<Long>() {
            @Override
            public void onNotSignedUp() {
            }

            @Override
            public void onSuccess(Long result) {
                requestMe();
            }

            @Override
            public void onSessionClosed(final ErrorResult errorResult) {
            }

            @Override
            public void onFailure(final ErrorResult errorResult) {
            }
        }, null);
    }

    public void facebookInfoRequest(){
        AccessToken token = getFacebookAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try{
                            model.signIn(object.getString("email"), object.getString("id"), true, FACEBOOK_CODE);
                        }
                        catch (JSONException e){

                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        else if(callbackManager.onActivityResult(requestCode, resultCode, data)){
            return;
        }
    }

    public CallbackManager getFacebookCallBackManager(){
        return callbackManager;
    }

    public FacebookCallback<LoginResult> getFacebookCallback(){
        return new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookInfoRequest();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };
    }

    public AccessToken getFacebookAccessToken(){
        return AccessToken.getCurrentAccessToken();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
        }
    }

    @ActivityScope
    public static class SignViewModelFactory implements ViewModelProvider.Factory{

        SignModel signModel;

        @Inject
        public SignViewModelFactory(SignModel signModel) {
            this.signModel = signModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new SignViewModel(signModel);
        }
    }
}
