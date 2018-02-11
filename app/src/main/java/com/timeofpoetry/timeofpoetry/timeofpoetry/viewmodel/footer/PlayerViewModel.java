package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.footer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
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
    private SignCheckModel signCheckModel;
    private MyPoetryModel bookMarkModel;
    private LikeModel likeModel;
    private SeekModel seekModel;
    public ObservableInt progress = new ObservableInt();
    public ObservableBoolean isLiked = new ObservableBoolean();
    public ObservableBoolean lyricShow = new ObservableBoolean(false);
    public LiveData<Boolean> isLogin;
    public LiveData<Integer> bookmarkStatus = new MutableLiveData<>();
    public LiveData<PoetryClass.Poem> current;

    PlayerViewModel(SeekModel seekModel, MyPlayListModel myPlayListModel, SignCheckModel signCheckModel, MyPoetryModel bookMarkModel, LikeModel likeModel) {
        this.seekModel = seekModel;
        this.myPlayListModel = myPlayListModel;
        this.signCheckModel = signCheckModel;
        this.bookMarkModel = bookMarkModel;
        this.likeModel = likeModel;
        load();
    }

    private void load(){
        this.isLogin = signCheckModel.getIsLogin();
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
        bookmarkStatus = bookMarkModel.checkInBookMark(current.getValue().clone());
        return bookmarkStatus;
    }

    public void setSeek(int pos){
        seekModel.setSeek(pos);
    }

    public LiveData<Integer> getSeek(){
        return seekModel.getSeek();
    }

    public void setProgress(int progress){
        this.progress.set(progress);
    }

    public LiveData<PoetryClass.Poem> getCurrent(){
        return current;
    }

    public LiveData<Integer> getLikeCount(){
        return likeModel.updateTxt(current.getValue());
    }

    public Boolean like(){
        isLiked.set(!isLiked.get());
        if(isLiked.get()){
            likeModel.like(current.getValue());
        }
        else{
            likeModel.unlike(current.getValue());
        }
        return isLiked.get();
    }

    public static class PlayerViewModelFactory implements ViewModelProvider.Factory{

        private SeekModel seekModel;
        private MyPlayListModel myPlayListModel;
        private SignCheckModel signCheckModel;
        private MyPoetryModel myPoetryModel;
        private LikeModel likeModel;

        public PlayerViewModelFactory(SeekModel seekModel, MyPlayListModel myPlayListModel, SignCheckModel signCheckModel, MyPoetryModel bookMarkModel, LikeModel likeModel) {
            this.seekModel = seekModel;
            this.myPlayListModel = myPlayListModel;
            this.signCheckModel = signCheckModel;
            this.myPoetryModel = bookMarkModel;
            this.likeModel = likeModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PlayerViewModel(seekModel, myPlayListModel, signCheckModel, myPoetryModel, likeModel);
        }
    }
}
