package com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.WifiManager;

/**
 * Created by sangroklee on 2018. 1. 3..
 */

public class MediaSystem {

    private static int LOSS = -1;
    private static int DUCK = 0;
    private static int LOSS_TRANSIENT = 2;

    private boolean isRegistered = false;
    private boolean isLocked = false;
    private AudioManager.OnAudioFocusChangeListener afChangeListener; //오디오 포커스 리스너
    private IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);//노이즈 리시버를 위한 인텐트
    private BroadcastReceiver mNoisyReceiver;
    private MediaPlaybackService service;
    private WifiManager.WifiLock wifiLock;
    private int focusState = LOSS;

    public MediaSystem(final MediaPlaybackService service) {
        this.service = service;
        afChangeListener =
                new AudioManager.OnAudioFocusChangeListener() {
                    public void onAudioFocusChange(int focusChange) {
                        switch( focusChange ) {
                            case AudioManager.AUDIOFOCUS_GAIN: {
                                if(focusState == DUCK) {
                                    service.setDuckVolume(false);
                                }
                                else{
                                    service.playProcess();
                                }
                                break;
                            }
                            case AudioManager.AUDIOFOCUS_LOSS: {
                                service.stopProcess();
                                break;
                            }
                            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: {
                                focusState = LOSS_TRANSIENT;
                                service.pauseProcess();
                                break;
                            }
                            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK: {
                                focusState = DUCK;
                                service.setDuckVolume(true);
                                break;
                            }
                        }
                    }
                };

        mNoisyReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                service.pauseProcess();
            }
        };

        wifiLock = ((WifiManager) service.getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
    }

    public void pause(){
        if(isRegistered) {
            service.unregisterReceiver(mNoisyReceiver);
            isRegistered = false;
        }
        if (isLocked) {
            wifiLock.release();
            isLocked = false;
        }
    }

    void stop(){
        AudioManager am = (AudioManager) service.getSystemService(Context.AUDIO_SERVICE);
        am.abandonAudioFocus(afChangeListener);
        if(isRegistered) {
            service.unregisterReceiver(mNoisyReceiver);
            isRegistered = false;
        }
        if (isLocked) {
            wifiLock.release();
            isLocked = false;
        }
    }

    public void play(){
        if(!isLocked) {
            wifiLock.acquire();
            isLocked = true;
        }
        if(!isRegistered) {
            service.registerReceiver(mNoisyReceiver, intentFilter);
            isRegistered = true;
        }
    }

    boolean successfullyRetrievedAudioFocus() {
        AudioManager audioManager = (AudioManager) service.getSystemService(Context.AUDIO_SERVICE);

        int result = audioManager.requestAudioFocus(afChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_GAIN;
    }
}
