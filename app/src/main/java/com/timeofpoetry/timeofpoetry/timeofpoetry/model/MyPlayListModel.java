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
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.concerns.SharedPreferenceController;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Handler;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyPlayListModel extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String DATABASE_NAME = "PlayLists.db";
    private static final String TABLE_NAME = "playList";
    public static final String COLUMN_NAME_LISTNAME = "listName";
    public static final String COLUMN_NAME_POET = "poet";
    public static final String COLUMN_NAME_POEM = "poem";
    public static final String COLUMN_NAME_VOICE = "voice";
    public static final String COLUMN_NAME_ARTWORK = "artwork";
    public static final String COLUMN_NAME_DURATION = "duration";
    public static final String COLUMN_NAME_SOUNDURL = "soundUrl";
    public static final String COLUMN_NAME_COMPOSER = "composer";
    public static final String COLUMN_NAME_LYRICSURL = "lyricsUrl";
    public static final String COLUMN_NAME_PREV = "prev";
    public static final String COLUMN_NAME_NEXT = "next";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME_LISTNAME + TYPE_TEXT + " DEFAULT 'DFAULTLIST'" + COMMA_SEP +
            COLUMN_NAME_POET + TYPE_TEXT + COMMA_SEP +
            COLUMN_NAME_POEM + TYPE_TEXT + COMMA_SEP +
            COLUMN_NAME_VOICE + TYPE_TEXT + COMMA_SEP +
            COLUMN_NAME_ARTWORK + TYPE_TEXT + COMMA_SEP +
            COLUMN_NAME_SOUNDURL + TYPE_TEXT + COMMA_SEP +
            COLUMN_NAME_COMPOSER + TYPE_TEXT + COMMA_SEP +
            COLUMN_NAME_DURATION + TYPE_INTEGER + COMMA_SEP +
            COLUMN_NAME_LYRICSURL + TYPE_TEXT + COMMA_SEP +
            COLUMN_NAME_PREV + TYPE_INTEGER + " DEFAULT -1" + COMMA_SEP +
            COLUMN_NAME_NEXT + TYPE_INTEGER + " DEFAULT -1 )";
    private  static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final int SHUFFLE = 4;


    private SharedPreferenceController sharedPreferenceController;
    private MutableLiveData<Integer> mode = new MutableLiveData<>();
    private MutableLiveData<PoetryClass.Poem> currentPoem = new MutableLiveData<>();
    private MutableLiveData<Integer> position = new MutableLiveData<>();
    private PoetryModelData poetryModelData = new PoetryModelData(new ArrayList<PoetryClass.Poem>());
    private MutableLiveData<PoetryModelData> playList = new MutableLiveData<>();

    public MyPlayListModel(Context context, SharedPreferenceController sharedPreferenceController) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.sharedPreferenceController = sharedPreferenceController;
        position.setValue(sharedPreferenceController.getLastPosition());
        mode.setValue(sharedPreferenceController.getShuffleMode() ? MyPlayListModel.SHUFFLE : sharedPreferenceController.getRepeatMode());
        loadPlayList();
        currentPoem.setValue(PoetryClass.getNullPoem());
        autoSetCurrentPoem(false);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void addItems(PoetryClass.Poem poem){
        poem.setDatabaseId((int) addItem(poem));
        position.setValue(0);
        poetryModelData.addOnePoem(poem);
        autoSetCurrentPoem(false);
        playList.setValue(poetryModelData);
    }

    public void addItems(ArrayList<PoetryClass.Poem> items) {
        if (items.size() != 0) {
            ArrayList<PoetryClass.Poem> data = new ArrayList<>(poetryModelData.getPoetry());
            for (PoetryClass.Poem item : items) {
                item.setDatabaseId((int) addItem(item));
                data.add(0, item);
            }

            position.setValue(0);
            poetryModelData.setNewArray(data, false);
            autoSetCurrentPoem(false);
            playList.setValue(poetryModelData);
        }
    }

    public void removeItems(){
        int start = 0;
        int tmp = position.getValue();
        ArrayList<PoetryClass.Poem> data = new ArrayList<>(poetryModelData.getPoetry());

        while(start < data.size()){
            PoetryClass.Poem poem = data.get(start);
            if(poem.getIsSelected().get()){
                removeMedia(data.remove(start).getDatabaseId());
                if(start < tmp){
                    tmp--;
                }
                else{
                    if(tmp > data.size() - 1){
                        tmp = 0;
                    }
                }
                position.setValue(tmp);
            }
            else{
                start++;
            }
        }

        poetryModelData.setNewArray(data, false);
        autoSetCurrentPoem(true);
        playList.setValue(poetryModelData);
    }

    private long addItem(PoetryClass.Poem poem){
        int id = getLastMediaId();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_VOICE, poem.getVoice());
        values.put(COLUMN_NAME_POET, poem.getPoet());
        values.put(COLUMN_NAME_POEM, poem.getPoem());
        values.put(COLUMN_NAME_ARTWORK, poem.getArtworkUrl());
        values.put(COLUMN_NAME_DURATION, poem.getPlayTime());
        values.put(COLUMN_NAME_SOUNDURL, poem.getSoundUrl());
        values.put(COLUMN_NAME_LYRICSURL, poem.getTextUrl());
        values.put(COLUMN_NAME_COMPOSER, poem.getComposer());
        values.put(COLUMN_NAME_PREV, id);
        long newRowId = db.insert(TABLE_NAME, null, values);
        if(id != -1)changeNext(id, (int) newRowId);
        return newRowId;
    }

    private void removeMedia(int id){
        int[] prevNextId = getPrevNextId(id);
        if(prevNextId[0] != -1)changeNext(prevNextId[0], prevNextId[1]);
        if(prevNextId[1] != -1)changePrev(prevNextId[1], prevNextId[0]);
        SQLiteDatabase readDB = getReadableDatabase();
        String selection = "_id LIKE ?";
        String[] selectionArgs = { Integer.toString(id) };
        readDB.delete(TABLE_NAME, selection, selectionArgs);
    }

    @NonNull
    private PoetryClass.Poem getPoem(int id){
        SQLiteDatabase db = getReadableDatabase();
        String sId = Integer.toString(id);
        String selection = "_id = ?";
        String[] selectionArgs = {sId};
        Cursor c = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        c.moveToFirst();

        return new PoetryClass.Poem(c, id);
    }

    @NonNull
    private int[] getPrevNextId(int id){
        SQLiteDatabase db = getReadableDatabase();
        String sId = Integer.toString(id);
        String selection = "_id = ?";
        String[] selectionArgs = {sId};
        Cursor c = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        c.moveToFirst();
        int prev = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_PREV));
        int next = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_NEXT));
        c.close();
        return new int[] {prev, next};
    }

    private void changeNext(int id, int next){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_NEXT, next);
        String sId = Integer.toString(id);
        String selection = "_id LIKE ?";
        String[] selectionArgs = {sId};

        db.update(TABLE_NAME, values, selection, selectionArgs);
    }

    private void changePrev(int id, int prev){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_PREV, prev);
        String sId = Integer.toString(id);
        String selection = "_id LIKE ?";
        String[] selectionArgs = {sId};

        db.update(TABLE_NAME, values, selection, selectionArgs);
    }

    private int getLastMediaId(){
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_NAME_NEXT + " = ?";
        String[] selectionArgs = {"-1"};
        Cursor c = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if(c.getCount() == 0) return -1;
        c.moveToFirst();
        int result = c.getInt(c.getColumnIndexOrThrow("_id"));
        c.close();

        return result;
    }

    private int getFirstMediaId(){
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_NAME_PREV + " = ?";
        String[] selectionArgs = {"-1"};
        Cursor c = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if (c.getCount() == 0) return -1;

        c.moveToFirst();
        int result = c.getInt(c.getColumnIndexOrThrow("_id"));
        c.close();

        return result;
    }

    private void loadPlayList(){
        ArrayList<PoetryClass.Poem> playListData = new ArrayList<>();
        int currentId = getLastMediaId();
        while(currentId != -1){
            playListData.add(getPoem(currentId));
            currentId = getPrevNextId(currentId)[0];
        }
        poetryModelData.setNewArray(playListData, true);
        playList.setValue(poetryModelData);
    }

    private void autoSetCurrentPoem(boolean isRemoved){
        PoetryClass.Poem tmpPoem;
        if(poetryModelData.getPoetry().size() == 0){
            tmpPoem = PoetryClass.getNullPoem();
            BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(), R.drawable.logo);
            Bitmap resource = drawable.getBitmap();
            tmpPoem.setArtwork(resource);
            currentPoem.setValue(tmpPoem);
        }
        else{
            tmpPoem = poetryModelData.getPoetry().get(position.getValue());
            if(!isRemoved||currentPoem.getValue().getDatabaseId() != tmpPoem.getDatabaseId()){
                currentPoem.setValue(tmpPoem);
            }
        }
    }

    private int getRand() {
        Random r = new Random();
        if (poetryModelData.getPoetry().size() == 1) {
            return position.getValue();
        } else {
            int i;
            do {
                i = r.nextInt(poetryModelData.getPoetry().size());
            } while (i == position.getValue());

            return i;
        }
    }

    public LiveData<PoetryModelData> getPlayList(){
        return playList;
    }

    public LiveData<PoetryClass.Poem> getCurrentPoem(){
        return currentPoem;
    }

    public MutableLiveData<Integer> getPosition(){
        return position;
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
        this.position.setValue(position);
        autoSetCurrentPoem(false);
    }

    public void forward(){
        if(mode.getValue() == SHUFFLE){
            setPosition(getRand());
        }
        else{
            if(position.getValue() == poetryModelData.getPoetry().size() - 1){
                setPosition(0);
            }
            else{
                setPosition(position.getValue() + 1);
            }
        }
        autoSetCurrentPoem(false);
    }

    public void backward(){
        if(mode.getValue() == SHUFFLE) {
            setPosition(getRand());
        }
        else{
            if(position.getValue() == 0){
                setPosition(poetryModelData.getPoetry().size() - 1);
            }
            else{
                setPosition(position.getValue() - 1);
            }
        }
        autoSetCurrentPoem(false);
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
