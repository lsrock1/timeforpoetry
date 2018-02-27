package com.timeofpoetry.timeofpoetry.timeofpoetry.data;

import android.databinding.ObservableBoolean;

import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.MyPoetryModel;

/**
 * Created by sangroklee on 2018. 2. 27..
 */

public class SupplierPoetryViewModel extends PoetryViewModel {

    private MyPlayListModel model;

    public SupplierPoetryViewModel(PoetryModel poetryModel, MyPlayListModel model) {
        super(poetryModel);
        this.model = model;
    }

    public void addSelectedPoetryToPlayList(){
        model.addPoetry(getSelectedPoetry());
    }

    public void addPoemToPlayList(PoetryClass.Poem poem){
        model.addPoetry(poem.clone());
    }
}
