package com.timeofpoetry.timeofpoetry.timeofpoetry.di;

import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer.PlayListActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer.PlayerActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviView.SettingVersionActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities.SignActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities.SplashActivity;

import dagger.Subcomponent;

/**
 * singleton model들이 필요한 엑티비에 model을 제공하는 컴포넌트
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
