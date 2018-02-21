package com.timeofpoetry.timeofpoetry.timeofpoetry.data;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by sangroklee on 2018. 1. 2..
 */

public class PoetryModelData {

    private ArrayList<PoetryClass.Poem> poetry;
    private DiffCallback callback;
    private int change;
    private boolean alert = false;

    public PoetryModelData(ArrayList<PoetryClass.Poem> newList) {
        this.poetry = newList;
        this.callback = new DiffCallback(null, newList);
        this.change = 0;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public ArrayList<PoetryClass.Poem> getPoetry() {
        return poetry;
    }

    public DiffCallback getCallback() {
        return callback;
    }

    public void setNewArray(ArrayList<PoetryClass.Poem> newArray, boolean isInit){
        callback = new DiffCallback(poetry, newArray);
        change = isInit ? 0 : newArray.size() - poetry.size();
        poetry = newArray;
        alert = false;
    }

    public void addOnePoem(PoetryClass.Poem poem){
        ArrayList<PoetryClass.Poem> tmp = new ArrayList<>(poetry);
        tmp.add(0, poem);
        setNewArray(tmp, false);
    }

    public int getChange() {
        return change;
    }
}
