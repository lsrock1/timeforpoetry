package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign.SignCheckModel;

import javax.inject.Inject;

/**
 * Created by sangroklee on 2018. 1. 11..
 */

public class SplashViewModel extends ViewModel {

    private SignCheckModel mSignCheckModel;

    public SplashViewModel(SignCheckModel signCheckModel) {
        mSignCheckModel = signCheckModel;
    }

    public boolean isLogin(){
        return mSignCheckModel.getIsLogin().getValue();
    }

    @ActivityScope
    public static class SplashViewModelFactory implements ViewModelProvider.Factory{

        private SignCheckModel mSignCheckModel;

        @Inject
        public SplashViewModelFactory(SignCheckModel signCheckModel) {
            mSignCheckModel = signCheckModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new SplashViewModel(mSignCheckModel);
        }
    }
}
