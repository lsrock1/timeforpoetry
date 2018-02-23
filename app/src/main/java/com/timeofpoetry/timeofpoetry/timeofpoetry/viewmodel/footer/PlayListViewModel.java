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
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.PlayBackStateModel;

import java.util.ListIterator;

import javax.inject.Inject;

/**
 * Created by sangroklee on 2017. 12. 21..
 */

public class PlayListViewModel extends ViewModel{

    public ObservableBoolean isEditMode = new ObservableBoolean();
    private MyPlayListModel model;
    private LiveData<Integer> state;
    private LiveData<PoetryModelData> myPlayList;
    private LiveData<PoetryClass.Poem> currentPoem;
    private ArrayMap<Integer, ObservableInt> playHash = new ArrayMap<>();
    private int currentId = -1;

    PlayListViewModel(MyPlayListModel model, PlayBackStateModel playBackStateModel) {
        this.model = model;
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
            model.setPosition(getPositionByDbId(poem.getDatabaseId()));
        }
    }

    public ObservableInt getDisplayById(PoetryClass.Poem poem) {
        return playHash.get(poem.getDatabaseId());
    }

    private int getPositionByDbId(int id){
        ListIterator<PoetryClass.Poem> itr = myPlayList.getValue().getPoetry().listIterator();

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
            for(PoetryClass.Poem poem : myPlayList.getValue().getPoetry().subList(0, change)){
                if(playHash.get(poem.getDatabaseId()) == null) playHash.put(poem.getDatabaseId(), new ObservableInt(0));
            }
        }
    }

    @ActivityScope
    public static class PlayListViewModelFactory implements ViewModelProvider.Factory{

        private MyPlayListModel myPlayListModel;
        private PlayBackStateModel playBackStateModel;

        @Inject
        public PlayListViewModelFactory(MyPlayListModel model, PlayBackStateModel playModel) {
            myPlayListModel = model;
            playBackStateModel = playModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PlayListViewModel(myPlayListModel, playBackStateModel);
        }
    }
}
