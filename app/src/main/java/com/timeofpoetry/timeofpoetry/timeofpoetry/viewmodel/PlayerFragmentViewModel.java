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
import com.timeofpoetry.timeofpoetry.timeofpoetry.interfaces.RepeatState;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.PlayBackStateModel;

import javax.inject.Inject;

/**
 * Created by sangroklee on 2018. 2. 10..
 */

public class PlayerFragmentViewModel extends ViewModel {

    private MyPlayListModel mMyPlayListModel;
    private PlayBackStateModel mPlayBackStateModel;

    public ObservableInt viewState = new ObservableInt();
    public ObservableInt currentMode = new ObservableInt(100);
    public ObservableBoolean isWide = new ObservableBoolean(false);

    PlayerFragmentViewModel(MyPlayListModel myPlayListModel, PlayBackStateModel playBackStateModel) {
        mMyPlayListModel = myPlayListModel;
        mPlayBackStateModel = playBackStateModel;
        viewState.set(playBackStateModel.getState().getValue());
    }

    public LiveData<PoetryModelData> getMyPlayList(){
        return mMyPlayListModel.getPoetry();
    }

    public void setIsWide(boolean isWide){
        this.isWide.set(isWide);
    }

    public void onModeClick(){
        mMyPlayListModel.modeSwitch();
    }

    public LiveData<PoetryClass.Poem> getCurrentMedia() {return mMyPlayListModel.getCurrentPoem();}

    public LiveData<RepeatState> getMode(){return mMyPlayListModel.getMode();}

    public LiveData<Integer> getState(){return mPlayBackStateModel.getState();}

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

        private MyPlayListModel mMyPlayListModel;
        private PlayBackStateModel mPlayBackStateModel;

        @Inject
        public PlayerFragmentViewModelFactory(MyPlayListModel myPlayListModel, PlayBackStateModel playBackStateModel) {
            mMyPlayListModel = myPlayListModel;
            mPlayBackStateModel = playBackStateModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PlayerFragmentViewModel(mMyPlayListModel, mPlayBackStateModel);
        }
    }
}
