package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.footer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.Log;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PlayListController;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.PlayBackStateModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.MediaServiceViewModel;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sangroklee on 2017. 12. 21..
 */

public class MyPlayListViewModel extends ViewModel{

    public ObservableBoolean isEditMode = new ObservableBoolean();
    private MyPlayListModel model;
    private PlayBackStateModel playBackStateModel;
    private LiveData<Integer> state;
    private LiveData<PoetryModelData> myPlayList;
    private LiveData<PoetryClass.Poem> currentPoem;
    private ArrayMap<Integer, ObservableInt> playHash = new ArrayMap<>();
    private int currentId = -1;

    MyPlayListViewModel(MyPlayListModel model, PlayBackStateModel playModel) {
        this.model = model;
        playBackStateModel = playModel;
        myPlayList = model.getPlayList();
        currentPoem = model.getCurrentPoem();
        state = playBackStateModel.getState();
        init();
    }

    public void toggleEditMode(){
        if(isEditMode.get()){
            isEditMode.set(false);
            setAllUnselected();
        }
        else{
            isEditMode.set(true);
        }
    }

    private void init(){
        for(PoetryClass.Poem poem : myPlayList.getValue().getPoetry()){
            playHash.put(poem.getDatabaseId(), new ObservableInt(0));
        }
        playHash.put(-1, new ObservableInt(0));
    }

    public void setCurrentPoem(){
        if(currentId != -1)
            playHash.get(currentId).set(0);
        currentId = currentPoem.getValue().getDatabaseId();
        if(playHash.get(currentId) == null)
            playHash.put(currentId, new ObservableInt(0));
        setPlayingShow();
    }

    public void setPlayingShow(){
        if(state.getValue() == PlayBackStateModel.PLAYING || state.getValue() == PlayBackStateModel.BUFFERING){
            playHash.get(currentId).set(2);
        }
        else{
            playHash.get(currentId).set(1);
        }
    }

    public void removeItems(){
        model.removeItems();
    }

    private void setAllUnselected(){
        model.setSelectAll(false);
    }

    public void setAllSelected(){
        model.setSelectAll(true);
    }

    public int getItemCount(){
        return myPlayList.getValue().getPoetry().size();
    }

    public PoetryClass.Poem getItem(int index){
        return myPlayList.getValue().getPoetry().get(index);
    }

    public LiveData<PoetryModelData> getMyPlayList(){
        return myPlayList;
    }

    public LiveData<Integer> getState() {
        return state;
    }

    public LiveData<PoetryClass.Poem> getCurrentPoem(){
        return currentPoem;
    }

    public void rowSelect(PoetryClass.Poem poem){
        if(isEditMode.get()){
            poem.setIsSelect(!poem.getIsSelected().get());
        }
        else{
            model.setPosition(getPoemByDbId(poem.getDatabaseId()));
        }
    }

    public ObservableInt getDisplayById(PoetryClass.Poem poem) {
        return playHash.get(poem.getDatabaseId());
    }

    private int getPoemByDbId(int id){
        for(int i = 0 ; i < myPlayList.getValue().getPoetry().size() ; i++){
            if(myPlayList.getValue().getPoetry().get(i).getDatabaseId() == id){
                return i;
            }
        }
        return -1;
    }

    public void listUpdate(int change){
        if(change > 0){
            for(PoetryClass.Poem poem : myPlayList.getValue().getPoetry().subList(0, change)){
                if(playHash.get(poem.getDatabaseId()) == null) playHash.put(poem.getDatabaseId(), new ObservableInt(0));
            }
        }
    }

    public static class MyPlayListViewModelFactory implements ViewModelProvider.Factory{

        private MyPlayListModel myPlayListModel;
        private PlayBackStateModel playBackStateModel;

        public MyPlayListViewModelFactory(MyPlayListModel model, PlayBackStateModel playModel) {
            myPlayListModel = model;
            playBackStateModel = playModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MyPlayListViewModel(myPlayListModel, playBackStateModel);
        }
    }
}
