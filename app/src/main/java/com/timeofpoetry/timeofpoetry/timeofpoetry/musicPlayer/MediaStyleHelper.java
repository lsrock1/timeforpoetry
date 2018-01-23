package com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.app.NotificationCompat;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.PlayBackStateModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.MediaServiceViewModel;

/**
 * Created by sangroklee on 2017. 9. 24..
 */

public class MediaStyleHelper {
    public static NotificationCompat.Builder from(
            Context context, MediaSessionCompat mediaSession, MediaServiceViewModel viewModel) {
        PoetryClass.Poem poem = viewModel.getCurrentPoem().getValue();
        int[] actionsViewIndexs = new int[]{0, 1, 2};

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        NotificationCompat.Builder builder;
        if(Build.VERSION.SDK_INT < 26){
            builder = new NotificationCompat.Builder(context);
        }
        else{
            NotificationChannel channel = new NotificationChannel("time_for_poetry", "media_playback", NotificationManager.IMPORTANCE_LOW);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            builder = new NotificationCompat.Builder(context, channel.toString());
        }

        builder
                .setContentTitle(poem.getPoem())
                .setContentText(poem.getPoet())
                .setLargeIcon(poem.getArtwork())
                .setContentIntent(intent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                .setDeleteIntent(
                        MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_STOP))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                .setSmallIcon(R.drawable.ic_logo_top)
                .addAction(new NotificationCompat.Action(
                        R.drawable.ic_prev, "rewind",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_REWIND)
                ))
                .addAction(new NotificationCompat.Action(
                        viewModel.getState().getValue() == PlayBackStateModel.PLAYING || viewModel.getState().getValue() == PlayBackStateModel.BUFFERING ?  R.drawable.ic_pause : R.drawable.ic_play,
                        viewModel.getState().getValue() == PlayBackStateModel.PLAYING ? "pause" : "play",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(context, viewModel.getState().getValue() == PlayBackStateModel.PLAYING ? PlaybackStateCompat.ACTION_STOP : PlaybackStateCompat.ACTION_PLAY)
                ))
                .addAction(new NotificationCompat.Action(
                        R.drawable.ic_next, "forward",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_FAST_FORWARD)
                ))


                // Take advantage of MediaStyle features
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(actionsViewIndexs)

                        // Add a cancel button
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                                PlaybackStateCompat.ACTION_STOP)));
        return builder;
    }
}
