package com.timeofpoetry.timeofpoetry.timeofpoetry.di;

import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviViews.SettingVersionActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities.SignActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities.SplashActivity;

import dagger.Subcomponent;

/**
 * Created by sangroklee on 2018. 1. 7..
 */
@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(SignActivity signActivity);
    void inject(SplashActivity activity);
    void inject(SettingVersionActivity activity);
    FragComponent plus(FragModule fragModule);
}
