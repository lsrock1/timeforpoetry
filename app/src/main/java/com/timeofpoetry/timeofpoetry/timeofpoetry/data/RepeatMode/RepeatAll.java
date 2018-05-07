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

    private static RepeatAll mInstance;
    private MyPlayListModel mMyPlayListModel;
    private boolean mIsAlert = false;

    public static RepeatAll getInstance(MyPlayListModel myPlayListModel){
        if(mInstance == null)
            mInstance = new RepeatAll(myPlayListModel);
        mInstance.alertReset();
        return mInstance;
    }


    public RepeatAll(MyPlayListModel myPlayListModel) {
        this.mMyPlayListModel = myPlayListModel;
    }

    @Override
    public void modeSwitch() {
        mMyPlayListModel.setMode(RepeatOne.getInstance(mMyPlayListModel), PlaybackStateCompat.REPEAT_MODE_ONE);
    }

    @Override
    public String getToastString() {
        mIsAlert = true;
        return "전체 시를 반복합니다";
    }

    @Override
    public void onComplete(MediaPlayer mediaPlayer, MediaPlaybackService service, MediaServiceViewModel viewModel) {
        if(mMyPlayListModel.getPoetry().getValue().getPoetry().size() == 1){
            mediaPlayer.seekTo(0);
        }
        else{
            forward(mMyPlayListModel);
        }
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
    public int getPreferData() {
        return PlaybackStateCompat.REPEAT_MODE_ALL;
    }
}
