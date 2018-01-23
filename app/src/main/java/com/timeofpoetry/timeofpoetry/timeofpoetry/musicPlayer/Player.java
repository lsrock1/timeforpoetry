package com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.Toast;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.SeekModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.MediaServiceViewModel;

import java.io.IOException;

/**
 * Created by sangroklee on 2018. 1. 3..
 */

public class Player implements MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{

    private MediaServiceViewModel mediaServiceViewModel;
    private MediaPlaybackService service;
    private MediaPlayer mMediaPlayer;
    private boolean isPrepared = false;
    private boolean isPlaying = false;
    private PoetryClass.Poem poem;
    private PrepareTask prepareTask;
    private boolean isNew = false;
    private boolean isTimeout = false;
    private TimeOutTask timeOutTask;

    public Player(MediaPlaybackService service, MediaServiceViewModel mediaServiceViewModel) {
        mMediaPlayer = new MediaPlayer();
        this.service = service;
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setWakeMode(service.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setVolume(1.0f, 1.0f);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        prepareTask = new PrepareTask(this);
        timeOutTask = new TimeOutTask(this);
        this.mediaServiceViewModel = mediaServiceViewModel;
    }

    boolean seekTo(int pos){
        if(isPlaying){
            mMediaPlayer.seekTo(pos);
            return true;
        }
        else{
            return false;
        }
    }

    void onDestroy(){
        mMediaPlayer.release();
    }

    public void pause(){
        isPlaying = false;
        mMediaPlayer.pause();
        mediaServiceViewModel.saveSeek();
        mediaServiceViewModel.state_pause();
    }

    void stop(){
        isPlaying = false;
        isPrepared = false;
        mediaServiceViewModel.saveSeek();
        mMediaPlayer.reset();
        mediaServiceViewModel.state_stop();
    }

    void setDuckVolume(boolean bool){
        mMediaPlayer.setVolume(bool ? 0.1f : 1.0f, bool ? 0.1f : 1.0f);
    }

    public void setMedia(PoetryClass.Poem poem){
        isNew = true;
        if(!mediaServiceViewModel.getIsLogIn().getValue() || this.poem == null) {
            this.poem = poem;
        }
        else if(this.poem.getDatabaseId() == poem.getDatabaseId()){
            isNew = false;
            if(!isPlaying) service.playProcess();
        }
        else{
            this.poem = poem;
            mediaServiceViewModel.setSeek(0);
            service.playProcess();
        }
    }

    void play(){
        if(poem.getSoundUrl() == null){
            service.stopProcess();
            return;
        }

        if(isNew){
            refresh();
            isNew = false;
        }

        isPlaying = true;

        if(isPrepared){
            if(mediaServiceViewModel.getSeek() > 0) {
                mMediaPlayer.seekTo(mediaServiceViewModel.getSeek());
            }
            else{
                mMediaPlayer.start();
                mediaServiceViewModel.state_play();
            }
        }
        else {
            while(true) {
                try {
                    mMediaPlayer.setDataSource(poem.getSoundUrl());
                    prepareTask.startPrepare();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (IllegalStateException e){
                    e.printStackTrace();
                    mMediaPlayer.reset();
                }
            }
            mediaServiceViewModel.state_buffer();
            isPrepared = true;
        }
    }

    boolean rewind(){
        if(mMediaPlayer.getCurrentPosition() <= 2000){
            return true;
        }
        else{
            mMediaPlayer.seekTo(0);
            return false;
        }
    }

    boolean getIsPlaying(){
        return isPlaying;
    }

    void setProgress(){
        mediaServiceViewModel.setSeek(mMediaPlayer.getCurrentPosition());
    }

    private void refresh(){
        mMediaPlayer.reset();
        isPrepared = false;
        isPlaying = false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        mediaServiceViewModel.state_play();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Toast.makeText(service.getApplicationContext(), "네트워크 연결이 불안정합니다", Toast.LENGTH_SHORT).show();
        service.stopProcess();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(isTimeout){
            service.stopProcess();
            return;
        }
        int state = mediaServiceViewModel.getMode().getValue();
        if(state == PlaybackStateCompat.REPEAT_MODE_ONE || state == MyPlayListModel.SHUFFLE && mediaServiceViewModel.getLength() == 1){
            mediaPlayer.seekTo(0);
        }
        else if(state == MyPlayListModel.SHUFFLE || state == PlaybackStateCompat.REPEAT_MODE_ALL) {
            mediaServiceViewModel.forward();
        }
        else if(state == PlaybackStateCompat.REPEAT_MODE_NONE){
            service.stopProcess();
            mediaServiceViewModel.setSeek(0);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if(mediaServiceViewModel.getSeek() > 0) {
            mediaPlayer.seekTo(mediaServiceViewModel.getSeek());
        }
        else{
            mediaPlayer.start();
            mediaServiceViewModel.state_play();
        }
        timeOutTask.cancel();
    }

    void prepareStartMediaPlayer(){
        try {
            isTimeout = false;
            timeOutTask.startTimeout();
            mMediaPlayer.prepare();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    void timeOut(){
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(service.getApplicationContext(), "네트워크 연결이 불안정합니다", Toast.LENGTH_SHORT).show();
                isTimeout = true;
                service.stopProcess();
            }
        });
    }
}
