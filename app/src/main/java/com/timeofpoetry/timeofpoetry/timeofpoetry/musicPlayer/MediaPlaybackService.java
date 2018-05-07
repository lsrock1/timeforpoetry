package com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RemoteViews;
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
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.MediaServiceViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.widget.AppWidget;

import java.util.List;

import javax.inject.Inject;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MediaPlaybackService extends MediaBrowserServiceCompat implements LifecycleOwner{

    private static final int NOTI_ID = 13;

    @Inject public MediaServiceViewModel viewModel;
    @Inject public LifecycleRegistry lifecycleRegistry;
    @Inject public Player player;
    @Inject public MediaSystem mediaSystem;
    @Inject public ProgressTask progressTask;
    @Inject public MediaSessionCompat mediaSessionCompat;
    private MediaSessionCompat.Callback callback = new
        MediaSessionCompat.Callback(){
            @Override
            public void onPlay() {
                super.onPlay();
                if(viewModel.getIsLogIn().getValue()) {
                    if(viewModel.getCurrentPoem().getValue().getPoet().equals("")){
                        Toast.makeText(getApplicationContext(), "재생목록에 시를 추가해 주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "재생을 요청합니다", Toast.LENGTH_SHORT).show();
                    playProcess();
                }
                else{
                    Toast.makeText(getApplicationContext(), "로그인해 주세요", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSeekTo(long pos) {
                if(player.seekTo((int) pos)) {
                    playProcess();
                }
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                viewModel.forward();
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                if(player.rewind()){
                    viewModel.backward();
                }
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
            public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
                KeyEvent keyEvent = mediaButtonEvent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                if(keyEvent.getAction() == KeyEvent.ACTION_UP) return false;

                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_HEADSETHOOK) {
                    if(viewModel.getState().getValue() == PlayBackStateModel.PLAYING){
                        stopProcess();
                    }
                    else{
                        playProcess();
                    }
                }
                else {
                    switch (keyEvent.getKeyCode()){
                        case KeyEvent.KEYCODE_MEDIA_PLAY:
                            onPlay();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                            if(viewModel.getState().getValue() == PlayBackStateModel.PLAYING){
                                stopProcess();
                            }
                            else{
                                playProcess();
                            }
                            break;
                        case KeyEvent.KEYCODE_MEDIA_PAUSE:
                            onPause();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_STOP:
                            onStop();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_NEXT:
                            onSkipToNext();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                            onSkipToPrevious();
                            break;
                        default:
                            break;
                    }
                }
                return true;
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
        lifecycleRegistry.markState(Lifecycle.State.CREATED);
        lifecycleRegistry.markState(Lifecycle.State.STARTED);
        viewModelInit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED);
        player.onDestroy();
        mediaSessionCompat.release();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    private void viewModelInit(){
        viewModel.getCurrentPoem().observe(this, new Observer<PoetryClass.Poem>() {
            @Override
            public void onChanged(@Nullable PoetryClass.Poem poem) {
                player.setMedia(poem);
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
                PlaybackStateCompat.Builder mStateBuilder = new PlaybackStateCompat.Builder()
                        .setState(integer, 0, 1);
                mediaSessionCompat.setPlaybackState(mStateBuilder.build());
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
        mediaSessionCompat.setCallback(callback);
        mediaSessionCompat.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        PlaybackStateCompat.Builder mStateBuilder = new PlaybackStateCompat.Builder()
            .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_STOP |
                    PlaybackStateCompat.ACTION_PAUSE |
            PlaybackStateCompat.ACTION_SEEK_TO|PlaybackStateCompat.ACTION_SKIP_TO_NEXT|PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .setState(PlaybackStateCompat.STATE_STOPPED, 0, 1);
        mediaSessionCompat.setPlaybackState(mStateBuilder.build());

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setClass(getApplicationContext(), MediaButtonReceiver.class);

        mediaSessionCompat.setMediaButtonReceiver(PendingIntent.getBroadcast(getApplicationContext(), 0, mediaButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        setSessionToken(mediaSessionCompat.getSessionToken());
    }

    private void showNotification() {
        if(viewModel.getCurrentPoem().getValue() == null){
            return;
        }
        else if(viewModel.getCurrentPoem().getValue().getPoet().equals("")){
            sendBroadCast();
            return;
        }
        else if(viewModel.getCurrentPoem().getValue().getArtwork() == null) {
            getBitmap(viewModel.getCurrentPoem().getValue());
            return;
        }
        NotificationCompat.Builder builder = MediaStyleHelper.from(MediaPlaybackService.this, mediaSessionCompat, viewModel);
        sendBroadCast();
        if( builder == null ) {
            return;
        }
        startForeground(NOTI_ID, builder.build());
    }

    private void getBitmap(final PoetryClass.Poem tmpPoem){
        try {
            Glide.with(getApplicationContext()).asBitmap().load(tmpPoem.getArtworkUrl()).listener(
                    new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            tmpPoem.setArtwork(resource);
                            showNotification();
                            return false;
                        }
                    })
                    .submit(80, 80);
        }
        catch(Exception e){
            BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(), R.drawable.logo);
            Bitmap resource = drawable.getBitmap();
            tmpPoem.setArtwork(resource);
            showNotification();
        }
    }

    public void pauseProcess(){
        mediaSystem.pause();
        player.pause();
        stopForeground(false);
    }

    public void setDuckVolume(boolean bool){
        player.setDuckVolume(bool);
    }

    public void stopProcess(){
        mediaSystem.stop();
        player.stop();
        stopSelf();
        mediaSessionCompat.setActive(false);
        stopForeground(false);
    }

    public void playProcess(){
        if(mediaSystem.successfullyRetrievedAudioFocus()) {
            player.play();
            startService(new Intent(getApplicationContext(), MediaPlaybackService.class));
            mediaSessionCompat.setActive(true);
            mediaSystem.play();
        }
        else{
            Toast.makeText(getApplicationContext(), "다른 플레이어가 실행중입니다", Toast.LENGTH_SHORT).show();
            stopProcess();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && intent.getAction() != null && intent.getAction().equals("android.media.browse.MediaBrowserService") && intent.getBooleanExtra("fromWidget", false)){
            sendBroadCast();
        }
        MediaButtonReceiver.handleIntent(mediaSessionCompat, intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendBroadCast(){
        Intent intent = new Intent();
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra("state", viewModel.getState().getValue());
        intent.putExtra("poem", viewModel.getCurrentPoem().getValue().getPoem());
        intent.putExtra("poet", viewModel.getCurrentPoem().getValue().getPoet());
        intent.putExtra("url", viewModel.getCurrentPoem().getValue().getArtworkUrl());
        sendBroadcast(intent);
    }
}
