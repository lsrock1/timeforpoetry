package com.timeofpoetry.timeofpoetry.timeofpoetry.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.PlayBackStateModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer.MediaPlaybackService;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(intent != null && intent.getAction() != null && intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE") && intent.hasExtra("state")){

            buildView(intent.getParcelableExtra("poem"), intent.getIntExtra("state", PlayBackStateModel.STOP), context);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent intent = new Intent(context, MediaPlaybackService.class);
        intent.setAction("android.media.browse.MediaBrowserService");
        intent.putExtra("fromWidget", true);
        context.startService(intent);
        // There may be multiple widgets active, so update all of them
    }

    @Override
    public void onEnabled(Context context) {
//        Log.d("test", "onEnabled");
//        context.startService(new Intent(context, MediaPlaybackService.class));
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void buildView(PoetryClass.Poem poem, int state, Context context){
        RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.app_widget);
        views.setViewVisibility(R.id.play, state == PlayBackStateModel.PAUSE || state == PlayBackStateModel.STOP ? View.VISIBLE : View.GONE);
        views.setViewVisibility(R.id.stop, state == PlayBackStateModel.PLAYING ? View.VISIBLE : View.GONE);
        views.setViewVisibility(R.id.loading, state == PlayBackStateModel.BUFFERING ? View.INVISIBLE : View.GONE);

        views.setOnClickPendingIntent(R.id.play,  MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(), PlaybackStateCompat.ACTION_PLAY));
        views.setOnClickPendingIntent(R.id.stop,  MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(), PlaybackStateCompat.ACTION_STOP));
        views.setOnClickPendingIntent(R.id.prev,  MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(), PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));
        views.setOnClickPendingIntent(R.id.next,  MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(), PlaybackStateCompat.ACTION_SKIP_TO_NEXT));

        if(poem.getPoet().equals("")){
            views.setImageViewResource(R.id.cover, R.drawable.logo);
            views.setTextViewText(R.id.title, "추가된 시가 없습니다");
            views.setTextViewText(R.id.poet, "");
            setView(context, views);
        }
        else{
            views.setTextViewText(R.id.title, poem.getPoem());
            views.setTextViewText(R.id.poet, poem.getPoet());
            getBitmap(views, context, poem.getArtworkUrl());
        }
    }

    private void setView(Context context, RemoteViews views){
        ComponentName thisWidget = new ComponentName(context, AppWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, views);
    }

    private void getBitmap(RemoteViews views, Context context, String url){
        try {
            Glide.with(getApplicationContext()).asBitmap().load(url).listener(
                    new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            views.setImageViewBitmap(R.id.cover, resource);
                            setView(context, views);
                            return false;
                        }
                    })
                    .submit(180, 180);
        }
        catch(Exception e){
            BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(), R.drawable.logo);
            Bitmap resource = drawable.getBitmap();
            views.setImageViewBitmap(R.id.cover, resource);
        }
    }
}

