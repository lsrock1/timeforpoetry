package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.interfaces.RepeatState;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.PlayBackStateModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.SeekModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign.SignCheckModel;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sangroklee on 2017. 12. 16..
 */

public class MediaServiceViewModel extends ViewModel {

    private MyPlayListModel mMyPlayListModel;
    private PlayBackStateModel mPlayBackStateModel;
    private LiveData<PoetryClass.Poem> currentPoem;
    private SignCheckModel mSignCheckModel;
    private SeekModel mSeekModel;

    public MediaServiceViewModel(MyPlayListModel model, PlayBackStateModel playModel, SignCheckModel signCheckModel, SeekModel seekModel) {
        mMyPlayListModel = model;
        mPlayBackStateModel = playModel;
        mSignCheckModel = signCheckModel;
        mSeekModel = seekModel;
        load();
    }

    private void load(){
        currentPoem = mMyPlayListModel.getCurrentPoem();
    }

    public void state_stop(){
        mPlayBackStateModel.setState(PlayBackStateModel.STOP);
    }

    public void state_pause(){
        mPlayBackStateModel.setState(PlayBackStateModel.PAUSE);
    }

    public void state_buffer(){
        mPlayBackStateModel.setState(PlayBackStateModel.BUFFERING);
    }

    public void state_play(){
        mPlayBackStateModel.setState(PlayBackStateModel.PLAYING);
    }

    public void forward(){
        mMyPlayListModel.forward();
    }

    public void backward(){
        mMyPlayListModel.backward();
    }

    public LiveData<Integer> getState(){
        return mPlayBackStateModel.getState();
    }

    public LiveData<RepeatState> getMode() {return mMyPlayListModel.getMode();}

    public LiveData<PoetryClass.Poem> getCurrentPoem(){
        return currentPoem;
    }

    public LiveData<Boolean> getIsLogIn(){
        return mSignCheckModel.getIsLogin();
    }

    public void saveSeek(){
        mSeekModel.saveSeek();
    }

    public void setSeek(int position){
        mSeekModel.setSeek(position);
    }

    public int getSeek(){
        return mSeekModel.getSeek().getValue();
    }
}
