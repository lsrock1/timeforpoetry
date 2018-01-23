package com.timeofpoetry.timeofpoetry.timeofpoetry.di;

import com.timeofpoetry.timeofpoetry.timeofpoetry.model.BoardModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.naviViews.BoardActivityViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sangroklee on 2018. 1. 16..
 */
@Module
public class NaviActivityModule {

    public NaviActivityModule() {
    }

    @Provides
    @ActivityScope
    BoardModel provideBoardModel(){
        return new BoardModel();
    }

    @Provides
    @ActivityScope
    BoardActivityViewModel.BoardActivityViewModelFactory provideBoardActivityViewModelFactory(BoardModel boardModel){
        return new BoardActivityViewModel.BoardActivityViewModelFactory(boardModel);
    }
}