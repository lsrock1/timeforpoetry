package com.timeofpoetry.timeofpoetry.timeofpoetry.di;

import android.app.Application;
import android.content.Context;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PlayListDB;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.PlayBackStateModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.SeekModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.concerns.SharedPreferenceController;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign.SignCheckModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sangroklee on 2017. 12. 25..
 */
@Module
public class AppModule {

    private Application mApp;

    public AppModule(Application mApp) {
        this.mApp = mApp;
    }

    @Provides
    @Singleton
    Context provideContext(){
        return this.mApp;
    }
}
