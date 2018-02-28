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

public class RepeatNone implements RepeatState {

    private static RepeatNone instance;
    public static RepeatNone getInstance(MyPlayListModel myPlayListModel){
        if(instance == null)
            instance = new RepeatNone(myPlayListModel);
        instance.alertReset();
        return instance;
    }

    private MyPlayListModel myPlayListModel;
    private boolean isAlert = false;

    public RepeatNone(MyPlayListModel myPlayListModel) {
        this.myPlayListModel = myPlayListModel;
    }

    @Override
    public void modeSwitch() {
        myPlayListModel.setMode(Shuffle.getInstance(myPlayListModel), MyPlayListModel.SHUFFLE);
    }

    @Override
    public String getToastString() {
        isAlert = true;
        return "반복을 사용하지 않습니다";
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
        service.stopProcess();
        viewModel.setSeek(0);
    }

    @Override
    public int getPreferData() {
        return PlaybackStateCompat.REPEAT_MODE_NONE;
    }
}
