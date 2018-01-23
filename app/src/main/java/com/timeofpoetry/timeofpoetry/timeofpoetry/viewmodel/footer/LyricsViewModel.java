package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.footer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.LyricsLoad;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sangroklee on 2017. 12. 25..
 */

public class LyricsViewModel extends ViewModel {

    private LiveData<PoetryClass.Poem> current;
    private MutableLiveData<String> lyrics = new MutableLiveData<>();
    private LyricsLoad lyricsLoad;

    LyricsViewModel(MyPlayListModel myPlayListModel, LyricsLoad lyricsLoad) {
        current = myPlayListModel.getCurrentPoem();
        this.lyricsLoad = lyricsLoad;
    }

    public LiveData<PoetryClass.Poem> getCurrent(){
        return current;
    }

    public void lyricsLoad(){
        lyricsLoad.startLyricsLoad(current.getValue().getTextUrl(), lyrics);
    }

    public LiveData<String> getLyrics(){
        return lyrics;
    }

    public static class LyricsViewModelFactory implements ViewModelProvider.Factory {

        private MyPlayListModel myPlayListModel;
        private LyricsLoad lyricsLoad;

        public LyricsViewModelFactory(MyPlayListModel myPlayListModel, LyricsLoad lyricsLoad) {
            this.myPlayListModel = myPlayListModel;
            this.lyricsLoad = lyricsLoad;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new LyricsViewModel(myPlayListModel, lyricsLoad);
        }
    }
}
