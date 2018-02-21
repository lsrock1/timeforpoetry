package com.timeofpoetry.timeofpoetry.timeofpoetry.interfaces;

import android.databinding.ObservableInt;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;

/**
 * Created by sangroklee on 2018. 1. 4..
 */

public interface PlayListController {
    void add(PoetryClass.Poem poem);
    void multiSelect(PoetryClass.Poem poem);
}
