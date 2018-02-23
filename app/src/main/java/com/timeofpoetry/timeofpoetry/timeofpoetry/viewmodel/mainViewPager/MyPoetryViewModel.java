package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainViewPager;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.interfaces.PlayListController;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.MyPoetryModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign.SignCheckModel;

import javax.inject.Inject;

/**
 * 나의 시집 다중 선택, 플레이리스트에 추가, 사버 통신을 통해 나의 시집에서 삭제
 */

public class MyPoetryViewModel extends ViewModel implements PlayListController {

    private SignCheckModel signCheckModel;
    private MyPoetryModel myPoetryModel;
    private MyPlayListModel playListModel;
    private LiveData<PoetryModelData> myPoetry;

    public ObservableBoolean isEditMode = new ObservableBoolean();

    MyPoetryViewModel(SignCheckModel mSignCheckModel, MyPoetryModel mMyPoetryModel, MyPlayListModel myPlayListModel) {
        signCheckModel = mSignCheckModel;
        myPoetryModel = mMyPoetryModel;
        playListModel = myPlayListModel;
        loadData();
    }

    public void loadData(){
        myPoetry = myPoetryModel.getMyPoetryList();
    }

    public LiveData<Boolean> getIsLogin(){
        return signCheckModel.getIsLogin();
    }

    public LiveData<PoetryModelData> getMyPoetry(){
        return myPoetry;
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

    public void allSelect(){
        myPoetryModel.setSelectAll(true);
    }

    public PoetryClass.Poem getItem(int position){
        return myPoetry.getValue().getPoetry().get(position);
    }

    @Override
    public void multiSelect(PoetryClass.Poem poem){
        if(isEditMode.get()){
            poem.setIsSelect(!poem.getIsSelected().get());
        }
    }

    @Override
    public void add(PoetryClass.Poem poem){
        playListModel.addItems(poem.clone());
    }

    public int getItemCount(){
        return myPoetry.getValue().getPoetry().size();
    }

    public void multiAdd(){
        playListModel.addItems(myPoetryModel.getSelectedItems());
        toggleEditMode();
    }

    private void setAllUnselected(){
        myPoetryModel.setSelectAll(false);
    }

    public void multiRemove(){
        myPoetryModel.selectedItemsRemove();
        toggleEditMode();
    }

    @FragScope
    public static class MyPoetryViewModelFactory implements ViewModelProvider.Factory{

        private SignCheckModel signCheckModel;
        private MyPoetryModel myPoetryModel;
        private MyPlayListModel myPlayListModel;

        @Inject
        public MyPoetryViewModelFactory(SignCheckModel signCheckModel, MyPoetryModel myPoetryModel, MyPlayListModel myPlayListModel) {
            this.signCheckModel = signCheckModel;
            this.myPoetryModel = myPoetryModel;
            this.myPlayListModel = myPlayListModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MyPoetryViewModel(signCheckModel, myPoetryModel, myPlayListModel);
        }
    }
}
