package com.timeofpoetry.timeofpoetry.timeofpoetry.data;

import android.databinding.ObservableInt;

/**
 * Created by sangroklee on 2018. 1. 4..
 */

public interface PlayListController {
    void add(PoetryClass.Poem poem);
    void multiSelect(PoetryClass.Poem poem);
}
