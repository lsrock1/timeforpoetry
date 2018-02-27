package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.v4.media.session.PlaybackStateCompat;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.PlayBackStateModel;

import javax.inject.Inject;

/**
 * Created by sangroklee on 2018. 2. 10..
 */

public class PlayerFragmentViewModel extends ViewModel {

    private MyPlayListModel myPlayListModel;
    private PlayBackStateModel playBackStateModel;

    public ObservableInt viewState = new ObservableInt();
    public ObservableInt currentMode = new ObservableInt(100);
    public ObservableBoolean isWide = new ObservableBoolean(false);

    PlayerFragmentViewModel(MyPlayListModel myPlayListModel, PlayBackStateModel playBackStateModel) {
        this.myPlayListModel = myPlayListModel;
        this.playBackStateModel = playBackStateModel;

        viewState.set(playBackStateModel.getState().getValue());
    }

    public LiveData<PoetryModelData> getMyPlayList(){
        return myPlayListModel.getPoetry();
    }

    public void setIsWide(boolean isWide){
        this.isWide.set(isWide);
    }

    public void onModeClick(){
        if(currentMode.get() == MyPlayListModel.SHUFFLE){
            myPlayListModel.setMode(PlaybackStateCompat.REPEAT_MODE_ALL);
        }
        else if(currentMode.get() == PlaybackStateCompat.REPEAT_MODE_ALL){
            myPlayListModel.setMode(PlaybackStateCompat.REPEAT_MODE_ONE);
        }
        else if(currentMode.get() == PlaybackStateCompat.REPEAT_MODE_ONE){
            myPlayListModel.setMode(PlaybackStateCompat.REPEAT_MODE_NONE);
        }
        else if(currentMode.get() == PlaybackStateCompat.REPEAT_MODE_NONE){
            myPlayListModel.setMode(MyPlayListModel.SHUFFLE);
        }
    }

    public LiveData<PoetryClass.Poem> getCurrentMedia() {return myPlayListModel.getCurrentPoem();}

    public LiveData<Integer> getMode(){return myPlayListModel.getMode();}

    public LiveData<Integer> getState(){return playBackStateModel.getState();}

    public void setState(int state){
        this.viewState.set(state);
    }

    public boolean setMode(int mode){
        if(currentMode.get() == 100){
            currentMode.set(mode);
            return false;
        }
        else{
            currentMode.set(mode);
            return true;
        }
    }

    @FragScope
    public static class PlayerFragmentViewModelFactory implements ViewModelProvider.Factory{

        private MyPlayListModel myPlayListModel;
        private PlayBackStateModel playBackStateModel;

        @Inject
        public PlayerFragmentViewModelFactory(MyPlayListModel myPlayListModel, PlayBackStateModel playBackStateModel) {
            this.myPlayListModel = myPlayListModel;
            this.playBackStateModel = playBackStateModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PlayerFragmentViewModel(myPlayListModel, playBackStateModel);
        }
    }
}
