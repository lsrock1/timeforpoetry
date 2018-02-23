package com.timeofpoetry.timeofpoetry.timeofpoetry.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sangroklee on 2018. 1. 2..
 */

public class PoetryModelData {

    private List<PoetryClass.Poem> poetry;
    private DiffCallback callback;
    private int change;
    private boolean alert = false;

    public PoetryModelData(List<PoetryClass.Poem> newList) {
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

    public List<PoetryClass.Poem> getPoetry() {
        return poetry;
    }

    public DiffCallback getCallback() {
        return callback;
    }

    public void setNewArray(List<PoetryClass.Poem> newArray, boolean isInit){
        callback = new DiffCallback(poetry, newArray);
        change = isInit ? 0 : newArray.size() - poetry.size();
        poetry = newArray;
        alert = false;
    }

    public void addOnePoem(PoetryClass.Poem poem){
        LinkedList<PoetryClass.Poem> tmp = new LinkedList<>(poetry);
        tmp.add(0, poem);
        setNewArray(tmp, false);
    }

    public int getChange() {
        return change;
    }
}
