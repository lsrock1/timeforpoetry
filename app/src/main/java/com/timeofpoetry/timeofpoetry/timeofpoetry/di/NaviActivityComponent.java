package com.timeofpoetry.timeofpoetry.timeofpoetry.di;

import com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviViews.BoardActivity;

import dagger.Component;
import dagger.Module;

/**
 * Created by sangroklee on 2018. 1. 16..
 */
@ActivityScope
@Component(modules = {NaviActivityModule.class})
public interface NaviActivityComponent {
    void inject(BoardActivity activity);
}
