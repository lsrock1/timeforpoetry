package com.timeofpoetry.timeofpoetry.timeofpoetry.data.RepeatMode;

import android.media.MediaPlayer;
import android.support.v4.media.session.PlaybackStateCompat;

import com.timeofpoetry.timeofpoetry.timeofpoetry.interfaces.RepeatState;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer.MediaPlaybackService;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.MediaServiceViewModel;

import java.util.Random;

/**
 * Created by sangroklee on 2018. 2. 28..
 */

public class Shuffle implements RepeatState {

    private static Shuffle mInstance;
    private MyPlayListModel mMyPlayListModel;
    private boolean mIsAlert = false;

    public static Shuffle getInstance(MyPlayListModel myPlayListModel){
        if(mInstance == null)
            mInstance = new Shuffle(myPlayListModel);
        mInstance.alertReset();
        return mInstance;
    }

    public Shuffle(MyPlayListModel myPlayListModel) {
        this.mMyPlayListModel = myPlayListModel;
    }

    @Override
    public void modeSwitch() {
        mMyPlayListModel.setMode(RepeatAll.getInstance(mMyPlayListModel), PlaybackStateCompat.REPEAT_MODE_ALL);
    }

    @Override
    public String getToastString() {
        mIsAlert = true;
        return "랜덤으로 재생합니다";
    }

    @Override
    public void forward(MyPlayListModel myPlayListModel) {
        if(myPlayListModel.getPoetry().getValue().getPoetry().size() > 0){
            myPlayListModel.setPosition(getRand(), true);
        }
    }

    @Override
    public void backward(MyPlayListModel myPlayListModel) {
        if(myPlayListModel.getPoetry().getValue().getPoetry().size() > 0){
            myPlayListModel.setPosition(getRand(), true);
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
    public void onComplete(MediaPlayer mediaPlayer, MediaPlaybackService service, MediaServiceViewModel viewModel) {
        if(mMyPlayListModel.getPoetry().getValue().getPoetry().size() == 1){
            mediaPlayer.seekTo(0);
        }
        else{
            forward(mMyPlayListModel);
        }
    }

    private int getRand() {
        Random r = new Random();
        int currentPosition = mMyPlayListModel.getPosition();
        if (mMyPlayListModel.getPoetry().getValue().getPoetry().size() == 1) {
            return currentPosition;
        }
        else {
            int i;
            do {
                i = r.nextInt(mMyPlayListModel.getPoetry().getValue().getPoetry().size());
            } while (i == currentPosition);

            return i;
        }
    }

    @Override
    public int getPreferData() {
        return MyPlayListModel.SHUFFLE;
    }
}
