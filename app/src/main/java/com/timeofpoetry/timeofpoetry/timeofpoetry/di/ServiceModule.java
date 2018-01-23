package com.timeofpoetry.timeofpoetry.timeofpoetry.di;

import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.support.v4.media.session.MediaSessionCompat;

import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.PlayBackStateModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.SeekModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign.SignCheckModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer.MediaPlaybackService;
import com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer.MediaSystem;
import com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer.Player;
import com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer.ProgressTask;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.MediaServiceViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sangroklee on 2018. 1. 11..
 */
@Module
public class ServiceModule {

    private MediaPlaybackService service;

    public ServiceModule(MediaPlaybackService service) {
        this.service = service;
    }

    @Provides
    @ActivityScope
    MediaPlaybackService provideMediaPlayBackService(){
        return service;
    }

    @Provides
    @ActivityScope
    MediaServiceViewModel provideMediaServiceViewModel(MyPlayListModel model, PlayBackStateModel playModel, SignCheckModel signCheckModel, SeekModel seekModel){
        return new MediaServiceViewModel(model, playModel, signCheckModel, seekModel);
    }

    @Provides
    @ActivityScope
    Player providePlayer(MediaPlaybackService mediaPlaybackService, MediaServiceViewModel mediaServiceViewModel){
        return new Player(mediaPlaybackService, mediaServiceViewModel);
    }

    @Provides
    @ActivityScope
    MediaSystem provideMediaSystem(MediaPlaybackService service){
        return new MediaSystem(service);
    }

    @Provides
    @ActivityScope
    ProgressTask provideProgressTask(Player player){
        return new ProgressTask(player);
    }

    @Provides
    @ActivityScope
    MediaSessionCompat provideMediaSessionCompat(Context context){
        return new MediaSessionCompat(context, "Time for Poetry");
    }

    @Provides
    @ActivityScope
    LifecycleRegistry provideLifecycleRegistry(MediaPlaybackService service){
        return new LifecycleRegistry(service);
    }
}
