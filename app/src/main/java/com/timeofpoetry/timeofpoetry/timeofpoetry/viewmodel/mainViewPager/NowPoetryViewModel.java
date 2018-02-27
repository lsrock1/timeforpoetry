package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainViewPager;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.SupplierPoetryViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragScope;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.BannerModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.RankModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.RecommendModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sangroklee on 2017. 12. 20..
 */

public class NowPoetryViewModel extends SupplierPoetryViewModel {

    private LiveData<PoetryClass.Banner> banner;
    private LiveData<PoetryModelData> recommend;

    NowPoetryViewModel(MyPlayListModel myModel, RankModel rankModel, RecommendModel recommendModel, BannerModel bannerModel) {
        super(rankModel, myModel);
        banner = bannerModel.getBanner();
        recommend = recommendModel.getPoetry();
    }

    public LiveData<PoetryClass.Banner> getBanner(){
        return banner;
    }

    public LiveData<PoetryModelData> getRank(){
        return getPoetry();
    }

    public LiveData<PoetryModelData> getRecommend(){
        return recommend;
    }

    public void addPlaylistFromRecommend(int position){
        if(recommend.getValue() != null && recommend.getValue().getPoetry().size() < 3){
            return;
        }
        addPoemToPlayList(recommend.getValue().getPoetry().get(position));
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
