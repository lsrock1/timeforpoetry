package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainViewPager;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.interfaces.PlayListController;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.MonthlyPoetryModel;

import java.util.ArrayList;

import javax.inject.Inject;

//서버에서 월간 몇시 받아오기, 다중 선택, 플레이리스트에 추가

public class MonthlyPoetryViewModel extends ViewModel implements PlayListController {

    private MyPlayListModel playListModel;
    private MonthlyPoetryModel monthlyPoetryModel;
    private MutableLiveData<ArrayList<PoetryClass.Poem>> poetry;
    public ObservableBoolean isEditMode = new ObservableBoolean();

    MonthlyPoetryViewModel(MyPlayListModel playListModel, MonthlyPoetryModel monthlyPoetryModel) {
        this.playListModel = playListModel;
        this.monthlyPoetryModel = monthlyPoetryModel;
        loadData();
    }

    public LiveData<ArrayList<PoetryClass.Poem>> getMonthlyPoetry(){
        return poetry;
    }

    private void loadData(){
        poetry = monthlyPoetryModel.getMonthlyPoetry();
    }

    public PoetryClass.Poem getItem(int index){
        return poetry.getValue().get(index);
    }

    public int getItemCount(){
        return poetry.getValue().size();
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

    @Override
    public void multiSelect(PoetryClass.Poem poem){
        if(isEditMode.get()){
            poem.setIsSelect(!poem.getIsSelected().get());
        }
    }

    public void multiAdd(){
        playListModel.addItems(monthlyPoetryModel.getSelectedItems());
        toggleEditMode();
    }

    @Override
    public void add(PoetryClass.Poem poem){
        playListModel.addItems(poem.clone());
    }

    private void setAllUnselected(){
        monthlyPoetryModel.setSelectAll(false);
    }

    @FragScope
    public static class MonthlyPoetryViewModelFactory implements ViewModelProvider.Factory{

        private MyPlayListModel myPlayListModel;
        private MonthlyPoetryModel monthlyPoetryModel;

        @Inject
        public MonthlyPoetryViewModelFactory(MyPlayListModel playListModel, MonthlyPoetryModel monthlyPoetryModel) {
            myPlayListModel = playListModel;
            this.monthlyPoetryModel = monthlyPoetryModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MonthlyPoetryViewModel(myPlayListModel, monthlyPoetryModel);
        }
    }
}
