package com.timeofpoetry.timeofpoetry.timeofpoetry.di;

import com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer.LyricsFragment;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer.MyPlayListFragment;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer.PlayerFragment;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainViewPager.MonthlyPoetry;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainViewPager.MyPoetryFragment;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainViewPager.NowPoetry;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities.SignInFragment;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities.SignUpFragment;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities.SignUpViewModel;

import dagger.Subcomponent;

/**
 * Created by sangroklee on 2018. 1. 10..
 */
@Subcomponent(modules = {FragModule.class})
@FragScope
public interface FragComponent {
    void inject(LyricsFragment fragment);
    void inject(MyPlayListFragment fragment);
    void inject(PlayerFragment fragment);
    void inject(MonthlyPoetry fragment);
    void inject(MyPoetryFragment fragment);
    void inject(NowPoetry fragment);
    void inject(SignInFragment fragment);
    void inject(SignUpFragment fragment);
}
