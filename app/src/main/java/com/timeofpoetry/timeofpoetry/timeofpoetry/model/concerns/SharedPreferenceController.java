package com.timeofpoetry.timeofpoetry.timeofpoetry.model.concerns;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.media.session.PlaybackStateCompat;

import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.PlayBackStateModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sangroklee on 2017. 10. 4..
 */
@Singleton
public class SharedPreferenceController {

    private SharedPreferences mSharedPreference;

    @Inject
    public SharedPreferenceController(Context context){
        this.mSharedPreference = context.getSharedPreferences(context.getString(R.string.UserInfos), context.MODE_PRIVATE);
    }

    public void setListener(SharedPreferences.OnSharedPreferenceChangeListener listener){
        mSharedPreference.registerOnSharedPreferenceChangeListener(listener);
    }

    public void onDestroy(SharedPreferences.OnSharedPreferenceChangeListener listener){
        mSharedPreference.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public boolean isLogin(){
        return mSharedPreference.getBoolean("IsLogin", false);
    }

    public String getUserId(){
        return mSharedPreference.getString("UserId", "");
    }

    public String getUserPwd(){
        return mSharedPreference.getString("UserPwd", "");
    }

    public int getRepeatMode(){
        return mSharedPreference.getInt("RepeatMode", PlaybackStateCompat.REPEAT_MODE_ALL);
    }

    public int getLastPosition(){ return mSharedPreference.getInt("LastPosition", 0); }

    public int getLastSeek(){ return mSharedPreference.getInt("LastSeek", 0); }

    public Boolean getShuffleMode(){
        return mSharedPreference.getBoolean("IsShuffle", false);
    }

    public boolean setUserId(String UserId){
        return mSharedPreference.edit().putString("UserId", UserId).commit();
    }

    public boolean setUserPwd(String UserPwd){
        return mSharedPreference.edit().putString("UserPwd", UserPwd).commit();
    }

    public boolean setLogin(boolean bool){
        return mSharedPreference.edit().putBoolean("IsLogin", bool).commit();
    }

    public boolean setRepeatMode(int in){
        return mSharedPreference.edit().putInt("RepeatMode", in).commit();
    }

    public boolean setShuffleMode(boolean bool){
        return mSharedPreference.edit().putBoolean("IsShuffle", bool).commit();
    }

    public boolean setLastPosition(int position){
        return mSharedPreference.edit().putInt("LastPosition", position).commit();
    }

    public boolean setLastSeek(int seek){
        return mSharedPreference.edit().putInt("LastSeek", seek).commit();
    }
}
