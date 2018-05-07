package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.footer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.SeekModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.LikeModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.MyPoetryModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign.SignCheckModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by sangroklee on 2017. 12. 23..
 */

public class PlayerViewModel extends ViewModel {

    private MyPlayListModel myPlayListModel;
    private SignCheckModel mSignCheckModel;
    private MyPoetryModel mBookMarkModel;
    private LikeModel mLikeModel;
    private SeekModel mSeekModel;
    public ObservableInt progress = new ObservableInt();
    public ObservableBoolean isLiked = new ObservableBoolean();
    public ObservableBoolean lyricShow = new ObservableBoolean(false);
    public LiveData<Boolean> isLogin;
    public LiveData<Integer> bookmarkStatus = new MutableLiveData<>();
    public LiveData<PoetryClass.Poem> current;

    PlayerViewModel(SeekModel seekModel, MyPlayListModel myPlayListModel, SignCheckModel signCheckModel, MyPoetryModel bookMarkModel, LikeModel likeModel) {
        mSeekModel = seekModel;
        this.myPlayListModel = myPlayListModel;
        this.mSignCheckModel = signCheckModel;
        this.mBookMarkModel = bookMarkModel;
        mLikeModel = likeModel;
        load();
    }

    private void load(){
        this.isLogin = mSignCheckModel.getIsLogin();
        this.current = myPlayListModel.getCurrentPoem();
    }

    public void lyricToggle(){
        lyricShow.set(!lyricShow.get());
    }

    public boolean onBackPressed(){
        if(lyricShow.get()){
            lyricToggle();
            return true;
        }
        else{
            return false;
        }
    }

    public LiveData<Integer> bookMark(){
        if(current.getValue() == null){
            return null;
        }
        bookmarkStatus = mBookMarkModel.checkInBookMark(current.getValue().clone());
        return bookmarkStatus;
    }

    public void setSeek(int pos){
        mSeekModel.setSeek(pos);
    }

    public LiveData<Integer> getSeek(){
        return mSeekModel.getSeek();
    }

    public void setProgress(int progress){
        this.progress.set(progress);
    }

    public LiveData<PoetryClass.Poem> getCurrent(){
        return current;
    }

    public LiveData<Integer> getLikeCount(){
        return mLikeModel.updateTxt(current.getValue());
    }

    public Boolean like(){
        isLiked.set(!isLiked.get());
        if(isLiked.get()){
            mLikeModel.like(current.getValue());
        }
        else{
            mLikeModel.unlike(current.getValue());
        }
        return isLiked.get();
    }

    @ActivityScope
    public static class PlayerViewModelFactory implements ViewModelProvider.Factory{

        private SeekModel mSeekModel;
        private MyPlayListModel mMyPlayListModel;
        private SignCheckModel mSignCheckModel;
        private MyPoetryModel mMyPoetryModel;
        private LikeModel mLikeModel;

        @Inject
        public PlayerViewModelFactory(SeekModel seekModel, MyPlayListModel myPlayListModel, SignCheckModel signCheckModel, MyPoetryModel bookMarkModel, LikeModel likeModel) {
            mSeekModel = seekModel;
            mMyPlayListModel = myPlayListModel;
            mSignCheckModel = signCheckModel;
            mMyPoetryModel = bookMarkModel;
            mLikeModel = likeModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PlayerViewModel(mSeekModel, mMyPlayListModel, mSignCheckModel, mMyPoetryModel, mLikeModel);
        }
    }
}
