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
    private static Shuffle instance;
    public static Shuffle getInstance(MyPlayListModel myPlayListModel){
        if(instance == null)
            instance = new Shuffle(myPlayListModel);
        instance.alertReset();
        return instance;
    }

    private MyPlayListModel myPlayListModel;
    private boolean isAlert = false;

    public Shuffle(MyPlayListModel myPlayListModel) {
        this.myPlayListModel = myPlayListModel;
    }

    @Override
    public void modeSwitch() {
        myPlayListModel.setMode(RepeatAll.getInstance(myPlayListModel), PlaybackStateCompat.REPEAT_MODE_ALL);
    }

    @Override
    public String getToastString() {
        isAlert = true;
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
        isAlert = false;
    }

    @Override
    public boolean isAlert(){
        return isAlert;
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

    private int getRand() {
        Random r = new Random();
        int currentPosition = myPlayListModel.getPosition();
        if (myPlayListModel.getPoetry().getValue().getPoetry().size() == 1) {
            return currentPosition;
        }
        else {
            int i;
            do {
                i = r.nextInt(myPlayListModel.getPoetry().getValue().getPoetry().size());
            } while (i == currentPosition);

            return i;
        }
    }

    @Override
    public int getPreferData() {
        return MyPlayListModel.SHUFFLE;
    }
}
