package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.PlayBackStateModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign.SignCheckModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sangroklee on 2017. 11. 22..
 */

public class MainActivityViewModel extends ViewModel {

    private SignCheckModel mSignCheckModel;
    private ActionBar mActionBar;

    public ObservableBoolean login = new ObservableBoolean();
    public ObservableBoolean isMainShow = new ObservableBoolean(true);
    public ObservableBoolean isListFragShow = new ObservableBoolean(false);
    public ObservableBoolean isPlayerShow = new ObservableBoolean(false);
    public ObservableBoolean isLyricShow = new ObservableBoolean(false);
    public String id;

    public MainActivityViewModel(SignCheckModel model) {
        mSignCheckModel = model;
        login.set(mSignCheckModel.getIsLogin().getValue());
    }

    public void setActionBar(ActionBar actionBar){
        mActionBar = actionBar;
    }

    public void logOut(){
        mSignCheckModel.setLogin(false);
    }

    public LiveData<Boolean> getIsLogin(){
        return mSignCheckModel.getIsLogin();
    }

    public void setLogin(Boolean bool){
        if(bool){
            id = mSignCheckModel.getUserId();
        }
        this.login.set(bool);
    }

    public Boolean onBackPressed(){
        if(isListFragShow.get()){
            isListFragShow.set(false);
            isMainShow.set(true);
            mActionBar.show();
            return true;
        }
        else if(isPlayerShow.get()){
            isPlayerShow.set(false);
            isMainShow.set(true);
            mActionBar.show();
            return true;
        }
        else if(isLyricShow.get()){
            isPlayerShow.set(true);
            isLyricShow.set(false);
            return true;
        }
        return false;
    }

    public void onListClick(){

        if(isListFragShow.get()){
            isListFragShow.set(false);
            isMainShow.set(true);
            mActionBar.show();
        }
        else if(isPlayerShow.get())
        {
            isListFragShow.set(true);
            isPlayerShow.set(false);
        }
        else if(isLyricShow.get()){
            isListFragShow.set(true);
            isLyricShow.set(false);
        }
        else{
            isListFragShow.set(true);
            isMainShow.set(false);
            mActionBar.hide();
        }
    }

    public void onPlayInfoClick(){
        if(isListFragShow.get()){
            isListFragShow.set(false);
            isPlayerShow.set(true);
        }
        else{
            isPlayerShow.set(true);
            isMainShow.set(false);
            mActionBar.hide();
        }
    }

    public void showLyrics(){
        isPlayerShow.set(false);
        isLyricShow.set(true);
    }

    @ActivityScope
    public static class MainActivityViewModelFactory implements ViewModelProvider.Factory{

        SignCheckModel mSignCheckModel;

        @Inject
        public MainActivityViewModelFactory(SignCheckModel signCheckModel) {
            mSignCheckModel = signCheckModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MainActivityViewModel(mSignCheckModel);
        }
    }
}
