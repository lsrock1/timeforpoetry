package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign.SignCheckModel;

/**
 * Created by sangroklee on 2018. 1. 11..
 */

public class SplashViewModel extends ViewModel {

    private SignCheckModel signCheckModel;

    public SplashViewModel(SignCheckModel signCheckModel) {
        this.signCheckModel = signCheckModel;
    }

    public boolean isLogin(){
        return signCheckModel.getIsLogin().getValue();
    }

    public static class SplashViewModelFactory implements ViewModelProvider.Factory{

        private SignCheckModel signCheckModel;

        public SplashViewModelFactory(SignCheckModel signCheckModel) {
            this.signCheckModel = signCheckModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new SplashViewModel(signCheckModel);
        }
    }
}
