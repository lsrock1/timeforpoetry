package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.footer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.LyricsLoad;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sangroklee on 2017. 12. 25..
 */

public class LyricsViewModel extends ViewModel {

    private LiveData<PoetryClass.Poem> mPoem;
    private MutableLiveData<String> mLyrics = new MutableLiveData<>();
    private LyricsLoad mLyricsLoad;

    LyricsViewModel(MyPlayListModel myPlayListModel, LyricsLoad lyricsLoad) {
        mPoem = myPlayListModel.getCurrentPoem();
        this.mLyricsLoad = lyricsLoad;
    }

    public LiveData<PoetryClass.Poem> getCurrent(){
        return mPoem;
    }

    public void lyricsLoad(){
        mLyricsLoad.startLyricsLoad(mPoem.getValue().getTextUrl(), mLyrics);
    }

    public LiveData<String> getLyrics(){
        return mLyrics;
    }

    @FragScope
    public static class LyricsViewModelFactory implements ViewModelProvider.Factory {

        private MyPlayListModel mMyPlayListModel;
        private LyricsLoad mLyricsLoad;

        @Inject
        public LyricsViewModelFactory(MyPlayListModel myPlayListModel, LyricsLoad lyricsLoad) {
            this.mMyPlayListModel = myPlayListModel;
            this.mLyricsLoad = lyricsLoad;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new LyricsViewModel(mMyPlayListModel, mLyricsLoad);
        }
    }
}
