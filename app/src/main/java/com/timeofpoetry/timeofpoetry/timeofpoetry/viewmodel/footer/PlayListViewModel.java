package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.footer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.util.ArrayMap;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.PlayBackStateModel;

import java.util.ListIterator;

import javax.inject.Inject;

/**
 * Created by sangroklee on 2017. 12. 21..
 */

public class PlayListViewModel extends PoetryViewModel{

    private LiveData<Integer> mState;
    private LiveData<PoetryClass.Poem> mCurrentPoem;
    private ArrayMap<Integer, ObservableInt> mPlayHash = new ArrayMap<>();
    private int currentId = -1;

    PlayListViewModel(MyPlayListModel model, PlayBackStateModel playBackStateModel) {
        super(model);
        mCurrentPoem = model.getCurrentPoem();
        mState = playBackStateModel.getState();
        init();
    }

    private void init(){
        for(PoetryClass.Poem poem : super.getPoetry().getValue().getPoetry()){
            mPlayHash.put(poem.getDatabaseId(), new ObservableInt(0));
        }
        mPlayHash.put(-1, new ObservableInt(0));
    }

    public void setCurrentPoem(){
        if(currentId != -1)
            mPlayHash.get(currentId).set(0);
        currentId = mCurrentPoem.getValue().getDatabaseId();
        if(mPlayHash.get(currentId) == null)
            mPlayHash.put(currentId, new ObservableInt(0));
        setPlayingShow();
    }

    public void setPlayingShow(){
        if(mState.getValue() == PlayBackStateModel.PLAYING || mState.getValue() == PlayBackStateModel.BUFFERING){
            mPlayHash.get(currentId).set(2);
        }
        else{
            mPlayHash.get(currentId).set(1);
        }
    }

    public void removeSelectedPoetry(){
        super.getModel().removePoetry();
    }

    public LiveData<Integer> getState() {
        return mState;
    }

    public LiveData<PoetryClass.Poem> getCurrentPoem(){
        return mCurrentPoem;
    }

    @Override
    public void touchItem(PoetryClass.Poem poem) {
        super.touchItem(poem);
        if(!isEditMode.get()){
            ((MyPlayListModel) super.getModel()).setPosition(getPositionByDbId(poem.getDatabaseId()), false);
        }
    }

    public ObservableInt getDisplayById(PoetryClass.Poem poem) {
        return mPlayHash.get(poem.getDatabaseId());
    }

    private int getPositionByDbId(int id){
        ListIterator<PoetryClass.Poem> itr = super.getPoetry().getValue().getPoetry().listIterator();

        while(itr.hasNext()){
            int index = itr.nextIndex();
            PoetryClass.Poem poem = itr.next();
            if(poem.getDatabaseId() == id){
                return index;
            }
        }

        return -1;
    }

    public void listUpdate(int change){
        if(change > 0){
            for(PoetryClass.Poem poem : super.getPoetry().getValue().getPoetry().subList(0, change)){
                if(mPlayHash.get(poem.getDatabaseId()) == null) mPlayHash.put(poem.getDatabaseId(), new ObservableInt(0));
            }
        }
    }

    @ActivityScope
    public static class PlayListViewModelFactory implements ViewModelProvider.Factory{

        private MyPlayListModel mMyPlayListModel;
        private PlayBackStateModel mPlayBackStateModel;

        @Inject
        public PlayListViewModelFactory(MyPlayListModel model, PlayBackStateModel playModel) {
            mMyPlayListModel = model;
            mPlayBackStateModel = playModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PlayListViewModel(mMyPlayListModel, mPlayBackStateModel);
        }
    }
}
