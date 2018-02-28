package com.timeofpoetry.timeofpoetry.timeofpoetry.di;

import com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviView.BoardActivity;

import dagger.Component;

/**
 * Created by sangroklee on 2018. 1. 16..
 */
@ActivityScope
@Component(modules = {NaviActivityModule.class})
public interface NaviActivityComponent {
    void inject(BoardActivity activity);
}
