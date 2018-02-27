package com.timeofpoetry.timeofpoetry.timeofpoetry.data;

import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * Created by sangroklee on 2018. 2. 27..
 */

abstract public class PoetryModel {

    public void addPoetry(List<PoetryClass.Poem> poetry){

    }

    public void removePoetry(List<PoetryClass.Poem> poetry){

    }

    public abstract LiveData<PoetryModelData> getPoetry();
}
