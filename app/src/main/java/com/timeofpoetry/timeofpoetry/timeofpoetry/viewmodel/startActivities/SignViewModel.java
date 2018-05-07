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
    private boolean mIsStack = false;
    private static int sKakaoCode = 0;
    private static int sFacebookCode = 1;
    private SessionCallback mCallback;
    private CallbackManager mCallbackManager;
    private SignModel mSignMdel;

    SignViewModel(SignModel signModel) {
        mSignMdel = signModel;
        mCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(mCallback);
        mCallbackManager = CallbackManager.Factory.create();
    }

    public LiveData<Integer> getSignInStatus(){
        return mSignMdel.getSignInData();
    }

    public LiveData<Integer> getSignUpStatus(){
        return mSignMdel.getSignUpData();
    }

    public void setMode(boolean mode){
        isUp.set(mode);
    }

    public boolean getStack(){
        return mIsStack;
    }

    public void onBackPressed(){
        mIsStack = false;
        changeMode();
    }

    public void toSignUp(){
        changeMode();
        mIsStack = true;
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
                mSignMdel.signIn(userProfile.getEmail(), Long.toString(userProfile.getId()), true, sKakaoCode);
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
                            mSignMdel.signIn(object.getString("email"), object.getString("id"), true, sFacebookCode);
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
        else if(mCallbackManager.onActivityResult(requestCode, resultCode, data)){
            return;
        }
    }

    public CallbackManager getFacebookCallBackManager(){
        return mCallbackManager;
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
        Session.getCurrentSession().removeCallback(mCallback);
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

        SignModel mSignModel;

        @Inject
        public SignViewModelFactory(SignModel signModel) {
            mSignModel = signModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new SignViewModel(mSignModel);
        }
    }
}
