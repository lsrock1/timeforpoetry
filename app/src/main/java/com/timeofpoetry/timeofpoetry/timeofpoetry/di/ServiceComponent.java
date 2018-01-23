package com.timeofpoetry.timeofpoetry.timeofpoetry.di;

import com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer.MediaPlaybackService;

import dagger.Subcomponent;

/**
 * Created by sangroklee on 2018. 1. 11..
 */
@ActivityScope
@Subcomponent(modules = {ServiceModule.class})
public interface ServiceComponent {
    void inject(MediaPlaybackService service);
}
