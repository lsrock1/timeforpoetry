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
 * sql로 데이터를 가져옴
 */
@Singleton
public class PlayListDB extends SQLiteOpenHelper {

    private static final int sDatabaseVersion = 1;
    private static final String sTypeText = " TEXT";
    private static final String sTypeInteger = " INTEGER";
    private static final String sCommaSep = ",";
    private static final String sDatabaseName = "PlayLists.db";
    private static final String sTableName = "playList";
    private static final String sColumnNameListName = "listName";
    private static final String sColumnNamePoet = "poet";
    private static final String sColumnNamePoem = "poem";
    private static final String sColumnNameVoice = "voice";
    private static final String sColumnNameArtWork = "artwork";
    private static final String sColumnNameDuration = "duration";
    private static final String sColumnNameSoundUrl = "soundUrl";
    private static final String sColumnNameComposer = "composer";
    private static final String sColumnNameLyricsUrl = "lyricsUrl";
    private static final String sColumnNamePrev = "prev";
    private static final String sColumnNameNext = "next";
    private static final String sSqlCreateEntries =
            "CREATE TABLE " + sTableName + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    sColumnNameListName + sTypeText + " DEFAULT 'DFAULTLIST'" + sCommaSep +
                    sColumnNamePoet + sTypeText + sCommaSep +
                    sColumnNamePoem + sTypeText + sCommaSep +
                    sColumnNameVoice + sTypeText + sCommaSep +
                    sColumnNameArtWork + sTypeText + sCommaSep +
                    sColumnNameSoundUrl + sTypeText + sCommaSep +
                    sColumnNameComposer + sTypeText + sCommaSep +
                    sColumnNameDuration + sTypeInteger + sCommaSep +
                    sColumnNameLyricsUrl + sTypeText + sCommaSep +
                    sColumnNamePrev + sTypeInteger + " DEFAULT -1" + sCommaSep +
                    sColumnNameNext + sTypeInteger + " DEFAULT -1 )";
    private  static final String sSqlDeleteEntries = "DROP TABLE IF EXISTS " + sTableName;

    @Inject
    public PlayListDB(Context context){
        super(context, sDatabaseName, null, sDatabaseVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sSqlCreateEntries);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(sSqlDeleteEntries);
        onCreate(db);
    }

    public long addItem(PoetryClass.Poem poem){
        int id = getLastItemId();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(sColumnNameVoice, poem.getVoice());
        values.put(sColumnNamePoet, poem.getPoet());
        values.put(sColumnNamePoem, poem.getPoem());
        values.put(sColumnNameArtWork, poem.getArtworkUrl());
        values.put(sColumnNameDuration, poem.getPlayTime());
        values.put(sColumnNameSoundUrl, poem.getSoundUrl());
        values.put(sColumnNameLyricsUrl, poem.getTextUrl());
        values.put(sColumnNameComposer, poem.getComposer());
        values.put(sColumnNamePrev, id);
        long newRowId = db.insert(sTableName, null, values);
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
        readDB.delete(sTableName, selection, selectionArgs);
    }

    @Nullable
    private PoetryClass.Poem getPoem(int id){
        SQLiteDatabase db = getReadableDatabase();
        String sId = Integer.toString(id);
        String selection = "_id = ?";
        String[] selectionArgs = {sId};
        Cursor c = db.query(sTableName, null, selection, selectionArgs, null, null, null);
        if(c != null && c.moveToFirst()){
            c.close();
            return new PoetryClass.Poem(
                    c.getString(c.getColumnIndexOrThrow(sColumnNameArtWork)),
                    c.getString(c.getColumnIndexOrThrow(sColumnNamePoet)),
                    c.getString(c.getColumnIndexOrThrow(sColumnNamePoem)),
                    c.getString(c.getColumnIndexOrThrow(sColumnNameSoundUrl)),
                    c.getString(c.getColumnIndexOrThrow(sColumnNameLyricsUrl)),
                    c.getString(c.getColumnIndexOrThrow(sColumnNameVoice)),
                    c.getInt(c.getColumnIndexOrThrow(sColumnNameDuration)),
                    c.getString(c.getColumnIndexOrThrow(sColumnNameComposer)),
                    id);
        }
        return null;
    }

    @NonNull
    private int[] getPrevNextId(int id){
        SQLiteDatabase db = getReadableDatabase();
        String sId = Integer.toString(id);
        String selection = "_id = ?";
        String[] selectionArgs = {sId};
        Cursor c = db.query(sTableName, null, selection, selectionArgs, null, null, null);
        if(c != null && c.moveToFirst()){
            int prev = c.getInt(c.getColumnIndexOrThrow(sColumnNamePrev));
            int next = c.getInt(c.getColumnIndexOrThrow(sColumnNameNext));
            c.close();
            return new int[]{prev, next};
        }
        return new int[]{-1, -1};
    }

    private void changeNext(int id, int next){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(sColumnNameNext, next);
        String sId = Integer.toString(id);
        String selection = "_id LIKE ?";
        String[] selectionArgs = {sId};

        db.update(sTableName, values, selection, selectionArgs);
    }

    private void changePrev(int id, int prev){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(sColumnNamePrev, prev);
        String sId = Integer.toString(id);
        String selection = "_id LIKE ?";
        String[] selectionArgs = {sId};

        db.update(sTableName, values, selection, selectionArgs);
    }

    private int getLastItemId(){
        SQLiteDatabase db = getReadableDatabase();
        String selection = sColumnNameNext + " = ?";
        String[] selectionArgs = {"-1"};
        Cursor c = db.query(sTableName, null, selection, selectionArgs, null, null, null);
        if(c != null && c.moveToFirst()){
            int result = c.getInt(c.getColumnIndexOrThrow("_id"));
            c.close();
            return result;
        }
        return -1;
    }

    private int getFirstItemId(){
        SQLiteDatabase db = getReadableDatabase();
        String selection = sColumnNameNext + " = ?";
        String[] selectionArgs = {"-1"};
        Cursor c = db.query(sTableName, null, selection, selectionArgs, null, null, null);
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
