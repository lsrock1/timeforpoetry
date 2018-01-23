package com.timeofpoetry.timeofpoetry.timeofpoetry.di;

import android.provider.MediaStore;

import com.timeofpoetry.timeofpoetry.timeofpoetry.GlobalApplication;
import com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer.MediaPlaybackService;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer.LyricsFragment;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer.MyPlayListFragment;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer.PlayerFragment;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainViewPager.MonthlyPoetry;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainViewPager.MyPoetryFragment;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainViewPager.NowPoetry;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities.SignActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities.SignInFragment;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities.SignUpFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sangroklee on 2017. 12. 26..
 */
@Singleton
@Component(modules = {AppModule.class})
public interface ApplicationComponent {
    ActivityComponent plus(ActivityModule activityModule);
    ServiceComponent plus(ServiceModule serviceModule);
}
