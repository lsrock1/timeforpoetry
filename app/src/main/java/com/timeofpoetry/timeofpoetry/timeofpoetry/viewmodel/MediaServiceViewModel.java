package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
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

    private MyPlayListModel model;
    private PlayBackStateModel playBackStateModel;
    private MutableLiveData<Integer> position;
    private LiveData<PoetryClass.Poem> currentPoem;
    private SignCheckModel signCheckModel;
    private SeekModel seekModel;

    public MediaServiceViewModel(MyPlayListModel model, PlayBackStateModel playModel, SignCheckModel signCheckModel, SeekModel seekModel) {
        this.model = model;
        playBackStateModel = playModel;
        this.signCheckModel = signCheckModel;
        this.seekModel = seekModel;
        load();
    }

    private void load(){
        position = model.getPosition();
        currentPoem = model.getCurrentPoem();
    }

    public LiveData<Integer> getPosition(){return position;}

    public void state_stop(){
        playBackStateModel.setState(PlayBackStateModel.STOP);
    }

    public void state_pause(){
        playBackStateModel.setState(PlayBackStateModel.PAUSE);
    }

    public void state_buffer(){
        playBackStateModel.setState(PlayBackStateModel.BUFFERING);
    }

    public void state_play(){
        playBackStateModel.setState(PlayBackStateModel.PLAYING);
    }

    public void forward(){
        model.forward();
    }

    public void backward(){
        model.backward();
    }

    public LiveData<Integer> getState(){
        return playBackStateModel.getState();
    }

    public LiveData<Integer> getMode() {return model.getMode();}

    public LiveData<PoetryClass.Poem> getCurrentPoem(){
        return currentPoem;
    }

    public LiveData<Boolean> getIsLogIn(){
        return signCheckModel.getIsLogin();
    }

    public void saveSeek(){
        seekModel.saveSeek();
    }

    public void setSeek(int position){
        seekModel.setSeek(position);
    }

    public int getSeek(){
        return seekModel.getSeek().getValue();
    }

    public int getLength(){
        return model.getPlayList().getValue().getPoetry().size();
    }
}
