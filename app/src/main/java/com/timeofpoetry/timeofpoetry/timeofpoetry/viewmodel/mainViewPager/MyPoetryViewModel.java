package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainViewPager;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.SupplierPoetryViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.MyPoetryModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign.SignCheckModel;

import javax.inject.Inject;

/**
 * 나의 시집 다중 선택, 플레이리스트에 추가, 사버 통신을 통해 나의 시집에서 삭제
 */

public class MyPoetryViewModel extends SupplierPoetryViewModel {

    private SignCheckModel signCheckModel;
    private MyPoetryModel myPoetryModel;

    MyPoetryViewModel(SignCheckModel mSignCheckModel, MyPoetryModel mMyPoetryModel, MyPlayListModel myPlayListModel) {
        super(mMyPoetryModel, myPlayListModel);
        signCheckModel = mSignCheckModel;
        myPoetryModel = mMyPoetryModel;
    }

    public LiveData<Boolean> getIsLogin(){
        return signCheckModel.getIsLogin();
    }

    @Override
    public void addSelectedPoetryToPlayList() {
        super.addSelectedPoetryToPlayList();
        toggleEditMode();
    }

    public void selectedPoetryRemove(){
        myPoetryModel.removePoetry();
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
