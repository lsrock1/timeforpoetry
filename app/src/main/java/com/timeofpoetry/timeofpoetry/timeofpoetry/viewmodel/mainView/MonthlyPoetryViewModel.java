package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainView;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.SupplierPoetryViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.MonthlyPoetryModel;

import javax.inject.Inject;

/**
 * view/naviView/BoardActivity view를 위한 뷰모델
 * 공지사항 데이터를 boardModel에서 가져와 view가 사용할 수 있게 wrapping
 */

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

        private MyPlayListModel mMyPlayListModel;
        private MonthlyPoetryModel mMonthlyPoetryModel;

        @Inject
        MonthlyPoetryViewModelFactory(MyPlayListModel playListModel, MonthlyPoetryModel monthlyPoetryModel) {
            mMyPlayListModel = playListModel;
            this.mMonthlyPoetryModel = monthlyPoetryModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MonthlyPoetryViewModel(mMyPlayListModel, mMonthlyPoetryModel);
        }
    }
}
