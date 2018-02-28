package com.timeofpoetry.timeofpoetry.timeofpoetry.interfaces;

import android.media.MediaPlayer;

import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer.MediaPlaybackService;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.MediaServiceViewModel;

/**
 * Created by sangroklee on 2018. 2. 28..
 */

public interface RepeatState {

    void modeSwitch();
    String getToastString();
    default void forward(MyPlayListModel myPlayListModel){
        if(myPlayListModel.getPoetry().getValue().getPoetry().size() > 0){
            if(myPlayListModel.getPosition() == myPlayListModel.getPoetry().getValue().getPoetry().size() - 1){
                myPlayListModel.setPosition(0, true);
            }
            else{
                myPlayListModel.setPosition(myPlayListModel.getPosition() + 1, true);
            }
        }
    }
    default void backward(MyPlayListModel myPlayListModel){
        if(myPlayListModel.getPoetry().getValue().getPoetry().size() > 0){
            if(myPlayListModel.getPosition() == 0){
                myPlayListModel.setPosition(myPlayListModel.getPoetry().getValue().getPoetry().size() - 1, true);
            }
            else{
                myPlayListModel.setPosition(myPlayListModel.getPosition() - 1, true);
            }
        }
    }
    void onComplete(MediaPlayer mediaPlayer, MediaPlaybackService service, MediaServiceViewModel viewModel);
    int getPreferData();
    boolean isAlert();
    void alertReset();
}
