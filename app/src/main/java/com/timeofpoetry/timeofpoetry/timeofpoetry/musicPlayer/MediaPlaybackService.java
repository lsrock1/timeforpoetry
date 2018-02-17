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
                if(viewModel.getIsLogIn().getValue()) {
                    Toast.makeText(getApplicationContext(), "재생을 요청합니다", Toast.LENGTH_SHORT).show();
                    playProcess();
                }
                else{
                    Toast.makeText(getApplicationContext(), "로그인해 주세요", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSeekTo(long pos) {
                if(mPlayer.seekTo((int) pos)) {
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
                if(mPlayer.rewind()){
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
                PlaybackStateCompat.Builder mStateBuilder = new PlaybackStateCompat.Builder()
                        .setState(integer, 0, 1);
                mMediaSessionCompat.setPlaybackState(mStateBuilder.build());
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
        mMediaSessionCompat.setCallback(callback);
        mMediaSessionCompat.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        PlaybackStateCompat.Builder mStateBuilder = new PlaybackStateCompat.Builder()
            .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_STOP |
                    PlaybackStateCompat.ACTION_PAUSE |
            PlaybackStateCompat.ACTION_SEEK_TO|PlaybackStateCompat.ACTION_SKIP_TO_NEXT|PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .setState(PlaybackStateCompat.STATE_STOPPED, 0, 1);
        mMediaSessionCompat.setPlaybackState(mStateBuilder.build());

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setClass(getApplicationContext(), MediaButtonReceiver.class);

        mMediaSessionCompat.setMediaButtonReceiver(PendingIntent.getBroadcast(getApplicationContext(), 0, mediaButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        setSessionToken(mMediaSessionCompat.getSessionToken());
    }

    private void showNotification() {
        if(viewModel.getCurrentPoem().getValue() == null){
            return;
        }
        else if(viewModel.getCurrentPoem().getValue().getPoem() == null){
            return;
        }
        else if(viewModel.getCurrentPoem().getValue().getArtwork() == null)
            getBitmap(viewModel.getCurrentPoem().getValue());
        NotificationCompat.Builder builder = MediaStyleHelper.from(MediaPlaybackService.this, mMediaSessionCompat, viewModel);
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

//    private void updateWidget(){
//        ComponentName thisWidget = new ComponentName(this, AppWidget.class);
//        AppWidgetManager manager = AppWidgetManager.getInstance(this);
//        manager.updateAppWidget(thisWidget, buildView());
//    }
//
//    private RemoteViews buildView(){
//        RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.app_widget);
//        PoetryClass.Poem poem = viewModel.getCurrentPoem().getValue();
//        int state = viewModel.getState().getValue();
//        views.setViewVisibility(R.id.play, state == PlayBackStateModel.PAUSE || state == PlayBackStateModel.STOP ? View.VISIBLE : View.GONE);
//        views.setViewVisibility(R.id.pause, state == PlayBackStateModel.PLAYING ? View.GONE : View.VISIBLE);
//        views.setViewVisibility(R.id.loading, state == PlayBackStateModel.BUFFERING ? View.INVISIBLE : View.GONE);
//
//        views.setOnClickPendingIntent(R.id.play,  MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(), PlaybackStateCompat.ACTION_PLAY));
//        views.setOnClickPendingIntent(R.id.pause,  MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(), PlaybackStateCompat.ACTION_STOP));
//        views.setOnClickPendingIntent(R.id.prev,  MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(), PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));
//        views.setOnClickPendingIntent(R.id.next,  MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(), PlaybackStateCompat.ACTION_SKIP_TO_NEXT));
//
//        if(poem.getPoem() == null){
//            views.setImageViewResource(R.id.cover, R.drawable.logo);
//            views.setTextViewText(R.id.title, "추가된 시가 없습니다");
//            views.setTextViewText(R.id.poet, "");
//        }
//        else{
//            views.setImageViewBitmap(R.id.cover, poem.getArtwork());
//            views.setTextViewText(R.id.title, poem.getPoem());
//            views.setTextViewText(R.id.poet, poem.getPoet());
//        }
//
//        return views;
//    }
}
