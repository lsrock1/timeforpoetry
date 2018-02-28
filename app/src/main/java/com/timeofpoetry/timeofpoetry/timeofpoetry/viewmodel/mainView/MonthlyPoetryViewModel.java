package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainView;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.SupplierPoetryViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.MonthlyPoetryModel;

import javax.inject.Inject;

//서버에서 월간 몇시 받아오기, 다중 선택, 플레이리스트에 추가

public class MonthlyPoetryViewModel extends SupplierPoetryViewModel {

    MonthlyPoetryViewModel(MyPlayListModel playListModel, MonthlyPoetryModel monthlyPoetryModel) {
        super(monthlyPoetryModel, playListModel);
    }

    @Override
    public void addSelectedPoetryToPlayList(){
        super.addSelectedPoetryToPlayList();
        toggleEditMode();
    }

    @FragScope
    public static class MonthlyPoetryViewModelFactory implements ViewModelProvider.Factory{

        private MyPlayListModel myPlayListModel;
        private MonthlyPoetryModel monthlyPoetryModel;

        @Inject
        MonthlyPoetryViewModelFactory(MyPlayListModel playListModel, MonthlyPoetryModel monthlyPoetryModel) {
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