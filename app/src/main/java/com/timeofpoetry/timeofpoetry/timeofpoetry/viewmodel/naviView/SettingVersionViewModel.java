package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.naviView;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.UserInfoModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.concerns.SharedPreferenceController;

import javax.inject.Inject;

/**
 * Created by sangroklee on 2018. 2. 7..
 */

public class SettingVersionViewModel extends ViewModel {

    private UserInfoModel mUserInfoModel;
    private SharedPreferenceController mSharedPreferenceController;

    public ObservableField<String> likePoem = new ObservableField<>("");
    public ObservableField<String> likePoet = new ObservableField<>("");
    public ObservableField<String> likeSeason = new ObservableField<>("");
    public ObservableField<String> userId = new ObservableField<>();

    public SettingVersionViewModel(UserInfoModel userInfoModel, SharedPreferenceController sharedPreferenceController) {
        this.mUserInfoModel = userInfoModel;
        this.mSharedPreferenceController = sharedPreferenceController;
        userId.set(sharedPreferenceController.getUserId());
    }

    public LiveData<Integer> getData(){
        return mUserInfoModel.getData();
    }

    public LiveData<PoetryClass.GetAddInfo> getUserInitData(){
        return mUserInfoModel.infoLoad(mSharedPreferenceController.getUserId(), mSharedPreferenceController.getUserPwd());
    }

    public void setUserInitData(PoetryClass.GetAddInfo getAddInfo){
        likePoet.set(!getAddInfo.getPoet().equals("false") ? getAddInfo.getPoet() : "");
        likePoem.set(!getAddInfo.getPoetry().equals("false") ? getAddInfo.getPoetry() : "");
        likeSeason.set(!getAddInfo.getSeason().equals("false") ? getAddInfo.getSeason() : "");
    }

    public void logOut(){
        mSharedPreferenceController.setLogin(false);
    }

    public boolean getIsLogin(){
        return mSharedPreferenceController.isLogin();
    }

    public void userInfoUpdate(){
        mUserInfoModel.infoUpdate(mSharedPreferenceController.getUserId(), mSharedPreferenceController.getUserPwd(), likePoet.get(), likePoem.get(), likeSeason.get());
    }

    @ActivityScope
    public static class SettingVersionViewModelFactory implements ViewModelProvider.Factory{

        private UserInfoModel mUserInfoModel;
        private SharedPreferenceController mSharedPreferenceController;

        @Inject
        public SettingVersionViewModelFactory(UserInfoModel userInfoModel, SharedPreferenceController sharedPreferenceController) {
            mUserInfoModel = userInfoModel;
            mSharedPreferenceController = sharedPreferenceController;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new SettingVersionViewModel(mUserInfoModel, mSharedPreferenceController);
        }
    }
}
