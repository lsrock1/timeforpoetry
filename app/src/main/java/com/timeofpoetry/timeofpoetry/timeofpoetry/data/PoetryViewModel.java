package com.timeofpoetry.timeofpoetry.timeofpoetry.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

import java.util.LinkedList;
import java.util.List;

/**
 * 시 리스트를 보여주는 뷰를 위한 뷰모델이 상속하는 클래스
 * 클래스에서 공유하는 기능들이 구현되어 있음
 * 시 리스트를 보여주는 뷰의 layout에서 데이터바인딩으로 메소드를 사용하고 있음
 */

public class PoetryViewModel extends ViewModel {

    private PoetryModel mModel;
    private LiveData<PoetryModelData> mData;
    public ObservableBoolean isEditMode = new ObservableBoolean(false);
    private boolean mToggleSelect = true;

    public PoetryViewModel(PoetryModel poetryModel) {
        this.mModel = poetryModel;
        mData = mModel.getPoetry();
    }

    public void toggleSelectAll(){
        if(mData.getValue() == null) return;
        if(mToggleSelect){
            selectAll();
        }
        else{
            unSelectAll();
        }
        mToggleSelect = !mToggleSelect;
    }

    private void selectAll(){
        if(mData.getValue() == null) return;
        for(PoetryClass.Poem poem : mData.getValue().getPoetry()){
            poem.setIsSelect(true);
        }
    }

    private void unSelectAll(){
        if(mData.getValue() == null) return;
        for(PoetryClass.Poem poem : mData.getValue().getPoetry()){
            poem.setIsSelect(false);
        }
    }

    List<PoetryClass.Poem> getSelectedPoetry(){
        if(mData.getValue() == null) return new LinkedList<>();
        List<PoetryClass.Poem> poetry = new LinkedList<>();
        for(PoetryClass.Poem poem : mData.getValue().getPoetry()){
            if(poem.getIsSelected().get()) poetry.add(poem.clone());
        }

        return poetry;
    }

    public PoetryClass.Poem getItem(int index){
        if(mData.getValue() == null) return PoetryClass.getNullPoem();
        return mData.getValue().getPoetry().get(index);
    }

    public int getItemCount(){
        if(mData.getValue() == null) return 0;
        return mData.getValue().getPoetry().size();
    }

    public void getReloadedPoetry(){
        mModel.getPoetry();
    }

    public LiveData<PoetryModelData> getPoetry(){
        return mData;
    }

    public void toggleEditMode(){
        if(isEditMode.get()){
            isEditMode.set(false);
            unSelectAll();
        }
        else{
            isEditMode.set(true);
        }
    }

    public void touchItem(PoetryClass.Poem poem){
        if(isEditMode.get()){
            poem.setIsSelect(!poem.getIsSelected().get());
        }
    }

    protected PoetryModel getModel(){
        return mModel;
    }
}
