package com.timeofpoetry.timeofpoetry.timeofpoetry;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.AppModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ApplicationComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.DaggerApplicationComponent;
import com.tsengvn.typekit.Typekit;

import dagger.android.support.DaggerApplication;

/**
 * Created by sangroklee on 2017. 10. 31..
 */

public class GlobalApplication extends Application{

    private ApplicationComponent component;
    private static GlobalApplication mInstance;
    private static volatile Activity currentActivity = null;

    private static class KakaoSDKAdapter extends KakaoAdapter {
        /**
         * Session Config에 대해서는 default값들이 존재한다.
         * 필요한 상황에서만 override해서 사용하면 됨.
         * @return Session의 설정값.
         */
        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return GlobalApplication.getGlobalApplicationContext();
                }
            };
        }
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }

    public static GlobalApplication getGlobalApplicationContext() {
        if(mInstance == null)
            throw new IllegalStateException("this application does not inherit GlobalApplication");
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .appModule(new AppModule(this))
                .build();

        mInstance = this;
        KakaoSDK.init(new KakaoSDKAdapter());

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/KoPubDotumMedium.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/KoPubDotumBold.ttf"));
    }

    public ApplicationComponent getComponent(){
        return component;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
}
