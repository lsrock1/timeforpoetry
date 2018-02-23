package com.timeofpoetry.timeofpoetry.timeofpoetry.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PlayListDB;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.concerns.SharedPreferenceController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.logging.Handler;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.facebook.FacebookSdk.getApplicationContext;

@Singleton
public class MyPlayListModel{

    public static final int SHUFFLE = 4;

    private SharedPreferenceController sharedPreferenceController;
    private PlayListDB playListDB;

    private MutableLiveData<Integer> mode = new MutableLiveData<>();
    private MutableLiveData<PoetryClass.Poem> currentPoem = new MutableLiveData<>();
    private int position;
    private PoetryModelData poetryModelData;
    private MutableLiveData<PoetryModelData> playList = new MutableLiveData<>();

    @Inject
    public MyPlayListModel(SharedPreferenceController sharedPreferenceController, PlayListDB playListDB) {
        this.sharedPreferenceController = sharedPreferenceController;
        this.playListDB = playListDB;
        position = sharedPreferenceController.getLastPosition();
        mode.setValue(sharedPreferenceController.getShuffleMode() ? MyPlayListModel.SHUFFLE : sharedPreferenceController.getRepeatMode());
        poetryModelData = playListDB.getPoetryModelData();
        playList.setValue(poetryModelData);
        currentPoem.setValue(PoetryClass.getNullPoem());
        autoSetCurrentPoem(false);
    }

    public void addItems(PoetryClass.Poem poem){
        poem.setDatabaseId((int) playListDB.addItem(poem));
        position = 0;
        poetryModelData.addOnePoem(poem);
        autoSetCurrentPoem(false);
        playList.setValue(poetryModelData);
    }

    public void addItems(List<PoetryClass.Poem> items) {
        if (items.size() != 0) {
            LinkedList<PoetryClass.Poem> data = new LinkedList<>(poetryModelData.getPoetry());
            for (PoetryClass.Poem item : items) {
                item.setDatabaseId((int) playListDB.addItem(item));
                data.add(0, item);
            }

            position = 0;
            poetryModelData.setNewArray(data, false);
            autoSetCurrentPoem(false);
            playList.setValue(poetryModelData);
        }
    }

    public void removeItems(){
        LinkedList<PoetryClass.Poem> data = new LinkedList<>(poetryModelData.getPoetry());
        ListIterator<PoetryClass.Poem> itr = data.listIterator();

        while(itr.hasNext()){
            int index = itr.nextIndex();
            PoetryClass.Poem poem = itr.next();
            if(poem.getIsSelected().get()){
                playListDB.removeItem(poem.getDatabaseId());
                itr.remove();
                if(index < position){
                    position--;
                }
                else if(position > data.size() - 1){
                    position = 0;
                }
            }
        }

        poetryModelData.setNewArray(data, false);
        autoSetCurrentPoem(true);
        playList.setValue(poetryModelData);
    }

    private void autoSetCurrentPoem(boolean isRemoved){
        PoetryClass.Poem tmpPoem;
        sharedPreferenceController.setLastPosition(position);
        if(poetryModelData.getPoetry().size() == 0){
            tmpPoem = PoetryClass.getNullPoem();
            BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(), R.drawable.logo);
            Bitmap resource = drawable.getBitmap();
            tmpPoem.setArtwork(resource);
            currentPoem.setValue(tmpPoem);
        }
        else{
            tmpPoem = poetryModelData.getPoetry().get(position);
            //삭제 후 같은 시일 경우에 currentpoem set이 되면서 재생이되는데 이를 막고자 삭제일 경우 동일 음원은 observer event를 일으키지 않게 함
            if(!isRemoved || currentPoem.getValue().getDatabaseId() != tmpPoem.getDatabaseId()){
                currentPoem.setValue(tmpPoem);
            }
        }
    }

    private int getRand() {
        Random r = new Random();
        if (poetryModelData.getPoetry().size() == 1) {
            return position;
        } else {
            int i;
            do {
                i = r.nextInt(poetryModelData.getPoetry().size());
            } while (i == position);

            return i;
        }
    }

    public LiveData<PoetryModelData> getPlayList(){
        return playList;
    }

    public LiveData<PoetryClass.Poem> getCurrentPoem(){
        return currentPoem;
    }

    public MutableLiveData<Integer> getMode(){
        return mode;
    }

    public void setMode(int mode){
        if(mode == MyPlayListModel.SHUFFLE){
            sharedPreferenceController.setShuffleMode(true);
        }
        else if(mode == PlaybackStateCompat.REPEAT_MODE_ALL){
            sharedPreferenceController.setShuffleMode(false);
            sharedPreferenceController.setRepeatMode(mode);
        }
        else if(mode == PlaybackStateCompat.REPEAT_MODE_ONE){
            sharedPreferenceController.setRepeatMode(mode);
        }
        else if(mode == PlaybackStateCompat.REPEAT_MODE_NONE){
            sharedPreferenceController.setRepeatMode(mode);
        }
        this.mode.setValue(mode);
    }

    public void setPosition(int position){
        if(position >= poetryModelData.getPoetry().size() || position < 0){
            return;
        }
        this.position = position;
        autoSetCurrentPoem(false);
    }

    public void forward(){
        if(mode.getValue() == SHUFFLE){
            setPosition(getRand());
        }
        else{
            if(position == poetryModelData.getPoetry().size() - 1){
                setPosition(0);
            }
            else{
                setPosition(position + 1);
            }
        }
    }

    public void backward(){
        if(mode.getValue() == SHUFFLE) {
            setPosition(getRand());
        }
        else{
            if(position == 0){
                setPosition(poetryModelData.getPoetry().size() - 1);
            }
            else{
                setPosition(position - 1);
            }
        }
    }

    public void setSelect(int index){
        PoetryClass.Poem poem = poetryModelData.getPoetry().get(index);
        poem.setIsSelect(!poem.getIsSelected().get());
    }

    public void setSelectAll(boolean bool){
        for(PoetryClass.Poem poem : poetryModelData.getPoetry()){
            poem.setIsSelect(bool);
        }
    }
}
