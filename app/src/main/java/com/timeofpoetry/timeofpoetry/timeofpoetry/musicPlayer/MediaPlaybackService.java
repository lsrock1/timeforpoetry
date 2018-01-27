package com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.timeofpoetry.timeofpoetry.timeofpoetry.GlobalApplication;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ServiceModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.PlayBackStateModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.concerns.SharedPreferenceController;
import com.timeofpoetry.timeofpoetry.timeofpoetry.NetworkTasks;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.MediaServiceViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

public class MediaPlaybackService extends MediaBrowserServiceCompat implements LifecycleOwner{

    private static final int NOTI_ID = 13;

    @Inject public MediaServiceViewModel viewModel;
    @Inject public LifecycleRegistry mLifecycleRegistry;
    @Inject public Player mPlayer;
    @Inject public MediaSystem mediaSystem;
    @Inject public ProgressTask progressTask;
    @Inject public MediaSessionCompat mMediaSessionCompat;
    private MediaSessionCompat.Callback callback = new
        MediaSessionCompat.Callback(){
            @Override
            public void onPlay() {
                super.onPlay();
                playProcess();
            }

            @Override
            public void onSeekTo(long pos) {
                if(mPlayer.seekTo((int) pos)) {
                    playProcess();
                }
            }

            @Override
            public void onFastForward() {
                super.onFastForward();
                viewModel.forward();
            }

            @Override
            public void onPause() {
                pauseProcess();
            }

            @Override
            public void onStop() {
                stopProcess();
            }

            @Override
            public void onRewind() {
                super.onRewind();
                if(mPlayer.rewind()){
                    viewModel.backward();
                }
            }
        };

    @Override
    public void onCreate() {
        super.onCreate();
        ((GlobalApplication) getApplication())
                .getComponent()
                .plus(new ServiceModule(this))
                .inject(this);
        initMediaSession();
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        mLifecycleRegistry.markState(Lifecycle.State.STARTED);
        viewModelInit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLifecycleRegistry.markState(Lifecycle.State.DESTROYED);
        mPlayer.onDestroy();
        mMediaSessionCompat.release();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    private void viewModelInit(){
        viewModel.getCurrentPoem().observe(this, new Observer<PoetryClass.Poem>() {
            @Override
            public void onChanged(@Nullable PoetryClass.Poem poem) {
                mPlayer.setMedia(poem);
            }
        });
        viewModel.getIsLogIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(!aBoolean){
                    stopProcess();
                }
            }
        });

        viewModel.getState().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                showNotification();
                if(integer == PlayBackStateModel.PLAYING){
                    progressTask.startProgress();
                }
                else{
                    progressTask.stopProgress();
                }
            }
        });
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        if(TextUtils.equals(clientPackageName, getPackageName())) {
            return new BrowserRoot(getString(R.string.app_name), null);
        }
        return null;
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.sendResult(null);
    }

    private void initMediaSession(){
        mMediaSessionCompat.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS|
                MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS);

        PlaybackStateCompat.Builder mStateBuilder = new PlaybackStateCompat.Builder()
            .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_STOP | PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
            |PlaybackStateCompat.ACTION_SEEK_TO|PlaybackStateCompat.ACTION_FAST_FORWARD|PlaybackStateCompat.ACTION_REWIND
            |PlaybackStateCompat.ACTION_SET_REPEAT_MODE | PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE);
        mMediaSessionCompat.setPlaybackState(mStateBuilder.build());
        mMediaSessionCompat.setCallback(callback);
        setSessionToken(mMediaSessionCompat.getSessionToken());
    }

    private void showNotification() {
        if(viewModel.getCurrentPoem().getValue() == null || viewModel.getCurrentPoem().getValue().getPoem() == null)
            return;
        NotificationCompat.Builder builder = MediaStyleHelper.from(MediaPlaybackService.this, mMediaSessionCompat, viewModel);
        if( builder == null ) {
            return;
        }
        startForeground(NOTI_ID, builder.build());
    }

    public void pauseProcess(){
        mediaSystem.pause();
        mPlayer.pause();
        stopForeground(false);
    }

    public void setDuckVolume(boolean bool){
        mPlayer.setDuckVolume(bool);
    }

    public void stopProcess(){
        mediaSystem.stop();
        mPlayer.stop();
        stopSelf();
        mMediaSessionCompat.setActive(false);
        stopForeground(false);
    }

    public void playProcess(){
        if(mediaSystem.successfullyRetrievedAudioFocus()) {
            mPlayer.play();
            startService(new Intent(getApplicationContext(), MediaPlaybackService.class));
            mMediaSessionCompat.setActive(true);
            mediaSystem.play();
        }
        else{
            Toast.makeText(getApplicationContext(), "다른 플레이어가 실행중입니다", Toast.LENGTH_SHORT).show();
            stopProcess();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MediaButtonReceiver.handleIntent(mMediaSessionCompat, intent);
        return super.onStartCommand(intent, flags, startId);
    }
}
