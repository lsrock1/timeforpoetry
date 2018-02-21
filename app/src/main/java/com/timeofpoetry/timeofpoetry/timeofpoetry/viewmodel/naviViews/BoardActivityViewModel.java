package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.naviViews;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.BoardModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by sangroklee on 2018. 1. 16..
 */

public class BoardActivityViewModel extends ViewModel {

    private BoardModel boardModel;
    private LiveData<ArrayList<PoetryClass.BoardIdItem>> boardItems;

    BoardActivityViewModel(BoardModel boardModel) {
        this.boardModel = boardModel;
        boardItems = this.boardModel.getList();
    }

    public LiveData<ArrayList<PoetryClass.BoardIdItem>> getBoardItems(){
        return boardItems;
    }

    public void getBody(View view, int id, PoetryClass.BoardIdItem content){
        ExpandableLinearLayout exView = ((ExpandableLinearLayout) ((ViewGroup)view.getParent().getParent()).getChildAt(1));
        if(content.displayContent.get().equals("")){
            boardModel.getContent(id, content.displayContent);
        }
        else{
            exView.toggle();
        }
    }

    @ActivityScope
    public static class BoardActivityViewModelFactory implements ViewModelProvider.Factory{

        private BoardModel boardModel;

        @Inject
        public BoardActivityViewModelFactory(BoardModel boardModel) {
            this.boardModel = boardModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new BoardActivityViewModel(boardModel);
        }
    }
}
