package com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.Toast;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.interfaces.RepeatState;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.MediaServiceViewModel;

import java.io.IOException;

/**
 * Created by sangroklee on 2018. 1. 3..
 */

public class Player implements MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{

    private MediaServiceViewModel mViewModel;
    private MediaPlaybackService mService;
    private MediaPlayer mMediaPlayer;
    private boolean mIsPrepared = false;
    private boolean mIsPlaying = false;
    private PoetryClass.Poem mPoem;
    private PrepareTask mPrepareTask;
    private boolean mIsNew = false;
    private boolean mIsTimeout = false;
    private TimeOutTask mTimeOutTask;

    public Player(MediaPlaybackService service, MediaServiceViewModel viewModel) {
        mMediaPlayer = new MediaPlayer();
        this.mService = service;
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setWakeMode(service.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setVolume(1.0f, 1.0f);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mPrepareTask = new PrepareTask(this);
        mTimeOutTask = new TimeOutTask(this);
        this.mViewModel = viewModel;
    }

    boolean seekTo(int pos){
        if(mIsPlaying){
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
        mIsPlaying = false;
        mMediaPlayer.pause();
        mViewModel.saveSeek();
        mViewModel.state_pause();
    }

    void stop(){
        mIsPlaying = false;
        mIsPrepared = false;
        mViewModel.saveSeek();
        try {
            mMediaPlayer.reset();
        }
        catch (IllegalStateException e){
            mMediaPlayer.release();
            mMediaPlayer = new MediaPlayer();
        }
        if(mIsTimeout){
            mIsTimeout = false;
        }//lg 폰에서 timeout이 일어났을 경우 on complete로 가지 않고 stop로 오는것으로 보임..
        mViewModel.state_stop();
    }

    void setDuckVolume(boolean bool){
        mMediaPlayer.setVolume(bool ? 0.1f : 1.0f, bool ? 0.1f : 1.0f);
    }

    public void setMedia(PoetryClass.Poem poem){
        //current media에 새로운 poem이 들어왔을 때 분기
        //로그인이 안됐을 경우 또는 최초 미디어 셋일 경우 재생하지 않고 초기화만 함
        //동일한 poem일 경우(데이터베이스 id로 구분) new 는 false고 재생중이면 놔두고 재생중이지 않을 때 (미디어가 셋만 됐을 때)는 재생
        //나머지 경우는 새 곡이 들어온 경우 seek bar를 0으로 하고 재생 요청
        mIsNew = true;
        if(!mViewModel.getIsLogIn().getValue() || this.mPoem == null) {
            this.mPoem = poem;
        }
        else if(this.mPoem.getDatabaseId() == poem.getDatabaseId()) {
            if (poem.isWard()) {
                mMediaPlayer.seekTo(0);
            } else {
                mIsNew = false;
                if (!mIsPlaying) mService.playProcess();
            }
        }
        else{
            this.mPoem = poem;
            mViewModel.setSeek(0);
            mService.playProcess();
        }
        this.mPoem.setWard(false);
    }

    void play(){
        //sound url이 없는 경우(null poem) 정지
        if(mPoem.getSoundUrl() == null){
            mService.stopProcess();
            return;
        }

        //새 시인 경우 미디어플레이어를 리셋해야 새로운 미디어 재생 가능
        //새 시가 아니고 pause했다 재생하는 경우는 리셋하지 않음
        if(mIsNew){
            refresh();
            mIsNew = false;
        }

        mIsPlaying = true;

        if(mIsPrepared){
            if(mViewModel.getSeek() > 0) {
                mMediaPlayer.seekTo(mViewModel.getSeek());
            }
            else{
                mMediaPlayer.start();
                mViewModel.state_play();
            }
        }
        else {
            while(true) {
                try {
                    mMediaPlayer.setDataSource(mPoem.getSoundUrl());
                    mPrepareTask.startPrepare();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (IllegalStateException e){
                    e.printStackTrace();
                    mMediaPlayer.reset();
                }
            }
            mViewModel.state_buffer();
            mIsPrepared = true;
        }
    }

    boolean rewind(){
        try {
            if (mMediaPlayer.getCurrentPosition() <= 2000) {
                return true;
            } else {
                mMediaPlayer.seekTo(0);
                return false;
            }
        }
        catch (IllegalStateException e){
            return true;
        }
    }

    boolean getIsPlaying(){
        return mIsPlaying;
    }

    void setProgress(){
        mViewModel.setSeek(mMediaPlayer.getCurrentPosition());
    }

    private void refresh(){
        mMediaPlayer.reset();
        mIsPrepared = false;
        mIsPlaying = false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        mViewModel.state_play();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Toast.makeText(mService.getApplicationContext(), "네트워크 연결이 불안정합니다", Toast.LENGTH_SHORT).show();
        mService.stopProcess();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(mIsTimeout){
            mService.stopProcess();
            return;
        }
        mViewModel.getMode().getValue().onComplete(mediaPlayer, mService, mViewModel);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mTimeOutTask.cancel();
        if(mViewModel.getSeek() > 0) {
            mediaPlayer.seekTo(mViewModel.getSeek());
        }
        else{
            mediaPlayer.start();
            mViewModel.state_play();
        }
    }

    void prepareStartMediaPlayer(){
        try {
            mIsTimeout = false;
            mTimeOutTask.startTimeout();
            mMediaPlayer.prepare();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    void timeOut(){
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mService.getApplicationContext(), "네트워크 연결이 불안정합니다", Toast.LENGTH_SHORT).show();
                mIsTimeout = true;
                mService.stopProcess();
            }
        });
    }
}
