package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign.SignModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sangroklee on 2017. 12. 15..
 */

public class SignUpViewModel extends ViewModel {

    public static final int PASSWORD_TOO_SHORT = 0;
    public static final int WRONG_EMAIL = 1;
    public static final int AGREE_CONTRACT = 2;

    private SignModel mModel;
    private MutableLiveData<Integer> mValidation = new MutableLiveData<>();
    public ObservableField<String> id = new ObservableField<>("");
    public ObservableField<String> pwd = new ObservableField<>("");
    public ObservableBoolean checked = new ObservableBoolean(false);
    public ObservableBoolean isShowContract = new ObservableBoolean(false);

    SignUpViewModel(SignModel signModel) {
        mModel = signModel;
    }

    public LiveData<Integer> getValidationStatus(){
        return mValidation;
    }

    public void onSignUpBtn(){
        if(checked.get()){
            if(!isEmailValid()){
                mValidation.setValue(WRONG_EMAIL);
            }
            else if(!isPwdLongerThanEight()) {
                mValidation.setValue(PASSWORD_TOO_SHORT);
            }
            else{
                mModel.signUp(id.get(), pwd.get(), false, -1);
            }
        }
        else{
            mValidation.setValue(AGREE_CONTRACT);
        }

    }

    public void toggleShow(){
        isShowContract.set(!isShowContract.get());
    }

    private boolean isPwdLongerThanEight(){
        return (pwd.get().trim().length() >= 8);
    }

    private boolean isEmailValid() {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(id.get());
        return matcher.matches();
    }

    @FragScope
    public static class SignUpViewModelFactory implements ViewModelProvider.Factory{

        private SignModel signModel;

        @Inject
        public SignUpViewModelFactory(SignModel signModel) {
            this.signModel = signModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new SignUpViewModel(signModel);
        }
    }
}
