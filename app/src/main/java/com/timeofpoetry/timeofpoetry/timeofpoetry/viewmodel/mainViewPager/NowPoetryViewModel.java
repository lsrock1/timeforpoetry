package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainViewPager;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.BannerModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.RankModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.RecommendModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by sangroklee on 2017. 12. 20..
 */

public class NowPoetryViewModel extends ViewModel {

    private MyPlayListModel myPlayListModel;
    private LiveData<PoetryClass.Banner> banner;
    private LiveData<ArrayList<PoetryClass.Poem>> rank;
    private LiveData<ArrayList<PoetryClass.Poem>> recommend;

    NowPoetryViewModel(MyPlayListModel myModel, RankModel rankModel, RecommendModel recommendModel, BannerModel bannerModel) {
        this.myPlayListModel = myModel;
        banner = bannerModel.getBanner();
        rank = rankModel.getRank();
        recommend = recommendModel.getRecommend();
    }

    public LiveData<PoetryClass.Banner> getBanner(){
        return banner;
    }

    public LiveData<ArrayList<PoetryClass.Poem>> getRank(){
        return rank;
    }

    public LiveData<ArrayList<PoetryClass.Poem>> getRecommend(){
        return recommend;
    }

    public void addPlaylistFromRecommend(int position){
        if(recommend.getValue().size() < 3){
            return;
        }
        myPlayListModel.addItems(recommend.getValue().get(position));
    }

    public PoetryClass.Poem getItem(int position){
        return rank.getValue().get(position);
    }

    public void add(PoetryClass.Poem poem){
        myPlayListModel.addItems(poem.clone());
    }

    public int getItemCount(){
        return rank.getValue().size();
    }

    @FragScope
    public static class NowPoetryViewModelFactory implements ViewModelProvider.Factory{

        private MyPlayListModel myPlayListModel;
        private RankModel rankModel;
        private RecommendModel recommendModel;
        private BannerModel bannerModel;

        @Inject
        public NowPoetryViewModelFactory(MyPlayListModel myPlayListModel, RankModel rankModel, RecommendModel recommendModel, BannerModel bannerModel) {
            this.myPlayListModel = myPlayListModel;
            this.rankModel = rankModel;
            this.recommendModel = recommendModel;
            this.bannerModel = bannerModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new NowPoetryViewModel(myPlayListModel, rankModel, recommendModel, bannerModel);
        }
    }
}
