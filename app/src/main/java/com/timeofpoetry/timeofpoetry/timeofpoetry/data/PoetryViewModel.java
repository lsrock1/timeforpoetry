package com.timeofpoetry.timeofpoetry.timeofpoetry.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sangroklee on 2018. 2. 27..
 */

public class PoetryViewModel extends ViewModel {

    private PoetryModel model;
    private LiveData<PoetryModelData> data;
    public ObservableBoolean isEditMode = new ObservableBoolean(false);

    public PoetryViewModel(PoetryModel poetryModel) {
        this.model = poetryModel;
        data = model.getPoetry();
    }

    protected void selectAll(){
        if(data.getValue() == null) return;
        for(PoetryClass.Poem poem : data.getValue().getPoetry()){
            poem.setIsSelect(true);
        }
    }

    protected void unSelectAll(){
        if(data.getValue() == null) return;
        for(PoetryClass.Poem poem : data.getValue().getPoetry()){
            poem.setIsSelect(false);
        }
    }

    protected List<PoetryClass.Poem> getSelectedPoetry(){
        if(data.getValue() == null) return new LinkedList<>();
        List<PoetryClass.Poem> poetry = new LinkedList<>();
        for(PoetryClass.Poem poem : data.getValue().getPoetry()){
            if(poem.getIsSelected().get()) poetry.add(poem.clone());
        }

        return poetry;
    }

    public PoetryClass.Poem getItem(int index){
        if(data.getValue() == null) return PoetryClass.getNullPoem();
        return data.getValue().getPoetry().get(index);
    }

    public int getItemCount(){
        if(data.getValue() == null) return 0;
        return data.getValue().getPoetry().size();
    }

    public void getReloadedPoetry(){
        model.getPoetry();
    }

    public LiveData<PoetryModelData> getPoetry(){
        return data;
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
        return model;
    }
}
