package com.timeofpoetry.timeofpoetry.timeofpoetry.di;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.timeofpoetry.timeofpoetry.timeofpoetry.model.BannerModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.PlayBackStateModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.concerns.SharedPreferenceController;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.LikeModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.MonthlyPoetryModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.MyPoetryModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.RankModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.RecommendModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign.SignCheckModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign.SignModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.MainActivityViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities.SignViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities.SplashViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sangroklee on 2018. 1. 7..
 */
@Module
public class ActivityModule {

    public ActivityModule() {

    }

    @Provides
    @ActivityScope
    MainActivityViewModel.MainActivityViewModelFactory provideMainActivityViewModelFactory(SignCheckModel model, MyPlayListModel myPlayListModel, PlayBackStateModel playModel){
        return new MainActivityViewModel.MainActivityViewModelFactory(model, myPlayListModel, playModel);
    }

    @Provides
    @ActivityScope
    SignViewModel.SignViewModelFactory provideSignViewModelFactory(SignModel signModel){
        return new SignViewModel.SignViewModelFactory(signModel);
    }

    @Provides
    @ActivityScope
    LikeModel provideLikeModel(){
        return new LikeModel();
    }

    @Provides
    @ActivityScope
    MonthlyPoetryModel provideMonthlyPoetryModel(){
        return new MonthlyPoetryModel();
    }

    @Provides
    @ActivityScope
    MyPoetryModel provideMyPoetryModel(SharedPreferenceController sharedPreferenceController){
        return new MyPoetryModel(sharedPreferenceController);
    }

    @Provides
    @ActivityScope
    RankModel provideRankModel(){
        return new RankModel();
    }

    @Provides
    @ActivityScope
    RecommendModel provideRecommendModel(){
        return new RecommendModel();
    }

    @Provides
    @ActivityScope
    SignModel provideSignModel(SharedPreferenceController sharedPreferenceController){
        return new SignModel(sharedPreferenceController);
    }

    @Provides
    @ActivityScope
    BannerModel provideBannerModel(){
        return new BannerModel();
    }

    @Provides
    @ActivityScope
    SplashViewModel.SplashViewModelFactory provideSplashViewModelFactory(SignCheckModel signCheckModel){
        return new SplashViewModel.SplashViewModelFactory(signCheckModel);
    }
}
