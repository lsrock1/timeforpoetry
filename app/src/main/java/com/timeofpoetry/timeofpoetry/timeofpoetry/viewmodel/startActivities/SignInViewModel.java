package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign.SignModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sangroklee on 2017. 12. 15..
 */

public class SignInViewModel extends ViewModel {

    private SignModel model;
    public ObservableField<String> id = new ObservableField<>("");
    public ObservableField<String> pwd = new ObservableField<>("");

    SignInViewModel(SignModel signModel) {
        model = signModel;
    }

    public void onSignInBtn(){
        model.signIn(id.get(), pwd.get(), false, -1);
    }

    public static class SignInViewModelFactory implements ViewModelProvider.Factory{

        private SignModel signModel;

        public SignInViewModelFactory(SignModel signModel) {
            this.signModel = signModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new SignInViewModel(signModel);
        }
    }
}
