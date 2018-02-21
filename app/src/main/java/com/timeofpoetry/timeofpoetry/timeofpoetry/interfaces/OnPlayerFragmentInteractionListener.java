package com.timeofpoetry.timeofpoetry.timeofpoetry.interfaces;

import android.app.Activity;
import android.os.Build;
import android.support.v4.media.session.MediaControllerCompat;
import android.view.KeyEvent;

import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityComponent;

/**
 * Created by sangroklee on 2018. 2. 19..
 */

public interface OnPlayerFragmentInteractionListener {
    ActivityComponent getComponent();
    default  boolean treatMediaButton(Activity activity, KeyEvent event){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        else if(event.getKeyCode() == KeyEvent.KEYCODE_HEADSETHOOK || event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY ||
                event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE|| event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PAUSE ||
                event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_STOP || event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_NEXT ||
                event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PREVIOUS){
            MediaControllerCompat.getMediaController(activity).dispatchMediaButtonEvent(event);
            return true;
        }
        return false;
    }
}
