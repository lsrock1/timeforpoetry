package com.timeofpoetry.timeofpoetry.timeofpoetry.interfaces;

import android.media.MediaPlayer;

import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer.MediaPlaybackService;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.MediaServiceViewModel;

/**
 * 재생모드(반복모드 & 셔플) 전환을 strategy 패턴으로 만들기 위한 인터페이스
 * 실제 구현은 data/RepeatMode 내에 있음
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
