package com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.concerns.SharedPreferenceController;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sangroklee on 2017. 12. 11..
 */
@Singleton
public class SignCheckModel implements SharedPreferences.OnSharedPreferenceChangeListener{

    private MutableLiveData<Boolean> mIsLogin = new MutableLiveData<>();
    private SharedPreferenceController mSharedPreferenceController;

    @Inject
    public SignCheckModel(SharedPreferenceController sharedPreferenceController) {
        mSharedPreferenceController = sharedPreferenceController;
        mSharedPreferenceController.setListener(this);
        mIsLogin.setValue(mSharedPreferenceController.isLogin());
    }

    public void setLogin(boolean bool){
        mSharedPreferenceController.setLogin(bool);
    }

    public String getUserId(){
        return mSharedPreferenceController.getUserId();
    }

    public MutableLiveData<Boolean> getIsLogin(){
        return mIsLogin;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals("IsLogin")){
            if(!mSharedPreferenceController.isLogin()) {
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
            mIsLogin.setValue(mSharedPreferenceController.isLogin());
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        mSharedPreferenceController.onDestroy(this);
    }
}
