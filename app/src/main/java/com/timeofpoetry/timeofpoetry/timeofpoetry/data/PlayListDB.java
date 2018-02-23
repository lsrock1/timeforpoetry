package com.timeofpoetry.timeofpoetry.timeofpoetry.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sangroklee on 2018. 2. 19..
 */
@Singleton
public class PlayListDB extends SQLiteOpenHelper {

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
    public static final String SQL_CREATE_ENTRIES =
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

    @Inject
    public PlayListDB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    public long addItem(PoetryClass.Poem poem){
        int id = getLastItemId();
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

    public void removeItem(int id){
        int[] prevNextId = getPrevNextId(id);
        if(prevNextId[0] != -1)changeNext(prevNextId[0], prevNextId[1]);
        if(prevNextId[1] != -1)changePrev(prevNextId[1], prevNextId[0]);
        SQLiteDatabase readDB = getReadableDatabase();
        String selection = "_id LIKE ?";
        String[] selectionArgs = { Integer.toString(id) };
        readDB.delete(TABLE_NAME, selection, selectionArgs);
    }

    @Nullable
    private PoetryClass.Poem getPoem(int id){
        SQLiteDatabase db = getReadableDatabase();
        String sId = Integer.toString(id);
        String selection = "_id = ?";
        String[] selectionArgs = {sId};
        Cursor c = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if(c != null && c.moveToFirst()){
            return new PoetryClass.Poem(c, id);
        }

        return null;
    }

    @NonNull
    private int[] getPrevNextId(int id){
        SQLiteDatabase db = getReadableDatabase();
        String sId = Integer.toString(id);
        String selection = "_id = ?";
        String[] selectionArgs = {sId};
        Cursor c = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if(c != null && c.moveToFirst()){
            int prev = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_PREV));
            int next = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_NEXT));
            c.close();
            return new int[]{prev, next};
        }
        return new int[]{-1, -1};
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

    private int getLastItemId(){
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_NAME_NEXT + " = ?";
        String[] selectionArgs = {"-1"};
        Cursor c = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if(c != null && c.moveToFirst()){
            int result = c.getInt(c.getColumnIndexOrThrow("_id"));
            c.close();
            return result;
        }
        return -1;
    }

    private int getFirstItemId(){
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_NAME_PREV + " = ?";
        String[] selectionArgs = {"-1"};
        Cursor c = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if(c != null && c.moveToFirst()){
            c.close();
            return c.getInt(c.getColumnIndexOrThrow("_id"));
        }
        return -1;
    }

    public PoetryModelData getPoetryModelData(){
        LinkedList<PoetryClass.Poem> playListData = new LinkedList<>();
        int currentId = getLastItemId();
        while(currentId != -1){
            PoetryClass.Poem poem = getPoem(currentId);
            if(poem != null){
                playListData.add(poem);
            }
            currentId = getPrevNextId(currentId)[0];
        }
        return new PoetryModelData(playListData);
    }
}
