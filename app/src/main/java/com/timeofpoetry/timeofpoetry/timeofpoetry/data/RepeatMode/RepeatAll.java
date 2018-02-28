package com.timeofpoetry.timeofpoetry.timeofpoetry.data.RepeatMode;

import android.media.MediaPlayer;
import android.support.v4.media.session.PlaybackStateCompat;

import com.timeofpoetry.timeofpoetry.timeofpoetry.interfaces.RepeatState;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.PlayBackStateModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer.MediaPlaybackService;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.MediaServiceViewModel;

/**
 * Created by sangroklee on 2018. 2. 28..
 */

public class RepeatAll implements RepeatState {

    private static RepeatAll instance;
    public static RepeatAll getInstance(MyPlayListModel myPlayListModel){
        if(instance == null)
            instance = new RepeatAll(myPlayListModel);
        instance.alertReset();
        return instance;
    }

    private MyPlayListModel myPlayListModel;
    private boolean isAlert = false;

    public RepeatAll(MyPlayListModel myPlayListModel) {
        this.myPlayListModel = myPlayListModel;
    }

    @Override
    public void modeSwitch() {
        myPlayListModel.setMode(RepeatOne.getInstance(myPlayListModel), PlaybackStateCompat.REPEAT_MODE_ONE);
    }

    @Override
    public String getToastString() {
        isAlert = true;
        return "전체 시를 반복합니다";
    }

    @Override
    public void onComplete(MediaPlayer mediaPlayer, MediaPlaybackService service, MediaServiceViewModel viewModel) {
        if(myPlayListModel.getPoetry().getValue().getPoetry().size() == 1){
            mediaPlayer.seekTo(0);
        }
        else{
            forward(myPlayListModel);
        }
    }

    @Override
    public void alertReset(){
        isAlert = false;
    }

    @Override
    public boolean isAlert(){
        return isAlert;
    }

    @Override
    public int getPreferData() {
        return PlaybackStateCompat.REPEAT_MODE_ALL;
    }
}
