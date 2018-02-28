package com.timeofpoetry.timeofpoetry.timeofpoetry.data.RepeatMode;

import android.media.MediaPlayer;
import android.support.v4.media.session.PlaybackStateCompat;

import com.timeofpoetry.timeofpoetry.timeofpoetry.interfaces.RepeatState;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer.MediaPlaybackService;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.MediaServiceViewModel;

/**
 * Created by sangroklee on 2018. 3. 1..
 */

public class RepeatOne implements RepeatState {

    private static RepeatOne instance;
    public static RepeatOne getInstance(MyPlayListModel myPlayListModel){
        if(instance == null)
            instance = new RepeatOne(myPlayListModel);
        instance.alertReset();
        return instance;
    }

    private MyPlayListModel myPlayListModel;
    private boolean isAlert = false;

    public RepeatOne(MyPlayListModel myPlayListModel) {
        this.myPlayListModel = myPlayListModel;
    }

    @Override
    public void modeSwitch() {
        myPlayListModel.setMode(RepeatNone.getInstance(myPlayListModel), PlaybackStateCompat.REPEAT_MODE_NONE);
    }

    @Override
    public String getToastString() {
        isAlert = true;
        return "시 하나를 반복합니다";
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
    public void onComplete(MediaPlayer mediaPlayer, MediaPlaybackService service, MediaServiceViewModel viewModel) {
        mediaPlayer.seekTo(0);
    }

    @Override
    public int getPreferData() {
        return PlaybackStateCompat.REPEAT_MODE_ONE;
    }
}
