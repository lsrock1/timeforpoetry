package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainView;

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

import javax.inject.Inject;

/**
 * Created by sangroklee on 2017. 12. 20..
 */

public class NowPoetryViewModel extends SupplierPoetryViewModel {

    private LiveData<PoetryClass.Banner> mBanner;
    private LiveData<PoetryModelData> mRecommend;

    NowPoetryViewModel(MyPlayListModel myModel, RankModel rankModel, RecommendModel recommendModel, BannerModel bannerModel) {
        super(rankModel, myModel);
        mBanner = bannerModel.getBanner();
        mRecommend = recommendModel.getPoetry();
    }

    public LiveData<PoetryClass.Banner> getBanner(){
        return mBanner;
    }

    public LiveData<PoetryModelData> getRank(){
        return getPoetry();
    }

    public LiveData<PoetryModelData> getRecommend(){
        return mRecommend;
    }

    public void addPlaylistFromRecommend(int position){
        if(mRecommend.getValue() != null && mRecommend.getValue().getPoetry().size() < 3){
            return;
        }
        addPoemToPlayList(mRecommend.getValue().getPoetry().get(position));
    }

    @FragScope
    public static class NowPoetryViewModelFactory implements ViewModelProvider.Factory{

        private MyPlayListModel mMyPlayListModel;
        private RankModel mRankModel;
        private RecommendModel mRecommendModel;
        private BannerModel mBannerModel;

        @Inject
        public NowPoetryViewModelFactory(MyPlayListModel myPlayListModel, RankModel rankModel, RecommendModel recommendModel, BannerModel bannerModel) {
            mMyPlayListModel = myPlayListModel;
            mRankModel = rankModel;
            mRecommendModel = recommendModel;
            mBannerModel = bannerModel;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new NowPoetryViewModel(mMyPlayListModel, mRankModel, mRecommendModel, mBannerModel);
        }
    }
}
