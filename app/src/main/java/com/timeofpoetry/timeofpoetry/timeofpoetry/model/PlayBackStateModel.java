package com.timeofpoetry.timeofpoetry.timeofpoetry.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;

import com.timeofpoetry.timeofpoetry.timeofpoetry.model.concerns.SharedPreferenceController;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sangroklee on 2017. 12. 19..
 */

public class PlayBackStateModel {

    final public static int BUFFERING = -1;
    final public static int PLAYING = 1;
    final public static int STOP = 0;
    final public static int PAUSE = 2;

    private MutableLiveData<Integer> state = new MutableLiveData<>();

    public PlayBackStateModel() {
        state.setValue(STOP);
    }

    public MutableLiveData<Integer> getState(){
        return state;
    }

    public void setState(int state){
        this.state.setValue(state);
    }
}
