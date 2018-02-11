package com.timeofpoetry.timeofpoetry.timeofpoetry.di;

import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer.PlayListActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer.PlayerActivity;
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
    void inject(SignActivity activity);
    void inject(SplashActivity activity);
    void inject(SettingVersionActivity activity);
    void inject(PlayListActivity activity);
    void inject(PlayerActivity activity);
    FragComponent plus(FragModule fragModule);
}
