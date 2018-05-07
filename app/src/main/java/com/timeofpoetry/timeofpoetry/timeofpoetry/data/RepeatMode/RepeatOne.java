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

    private static RepeatOne mInstance;
    private MyPlayListModel mMyPlayListModel;
    private boolean mIsAlert = false;

    public static RepeatOne getInstance(MyPlayListModel myPlayListModel){
        if(mInstance == null)
            mInstance = new RepeatOne(myPlayListModel);
        mInstance.alertReset();
        return mInstance;
    }

    public RepeatOne(MyPlayListModel myPlayListModel) {
        this.mMyPlayListModel = myPlayListModel;
    }

    @Override
    public void modeSwitch() {
        mMyPlayListModel.setMode(RepeatNone.getInstance(mMyPlayListModel), PlaybackStateCompat.REPEAT_MODE_NONE);
    }

    @Override
    public String getToastString() {
        mIsAlert = true;
        return "시 하나를 반복합니다";
    }

    @Override
    public void alertReset(){
        mIsAlert = false;
    }

    @Override
    public boolean isAlert(){
        return mIsAlert;
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
