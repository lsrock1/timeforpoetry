package com.timeofpoetry.timeofpoetry.timeofpoetry.di;

import com.timeofpoetry.timeofpoetry.timeofpoetry.model.BannerModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.PlayBackStateModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.SeekModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.LikeModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.LyricsLoad;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.MonthlyPoetryModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.MyPoetryModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.RankModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.RecommendModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign.SignCheckModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign.SignModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.PlayerFragmentViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.footer.LyricsViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.footer.PlayListViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.footer.PlayerViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainViewPager.MonthlyPoetryViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainViewPager.MyPoetryViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainViewPager.NowPoetryViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities.SignInViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities.SignUpViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sangroklee on 2018. 1. 10..
 */
@Module
public class FragModule {

    public FragModule() {
    }

    @Provides
    @FragScope
    LyricsViewModel.LyricsViewModelFactory provideLyricsViewModelFactory(MyPlayListModel myPlayListModel, LyricsLoad lyricsLoad){
        return new LyricsViewModel.LyricsViewModelFactory(myPlayListModel, lyricsLoad);
    }

    @Provides
    @FragScope
    MonthlyPoetryViewModel.MonthlyPoetryViewModelFactory provideMonthlyPoetryViewModelFactory(MyPlayListModel playListModel, MonthlyPoetryModel monthlyPoetryModel){
        return new MonthlyPoetryViewModel.MonthlyPoetryViewModelFactory(playListModel, monthlyPoetryModel);
    }

    @Provides
    @FragScope
    MyPoetryViewModel.MyPoetryViewModelFactory provideMyPoetryViewModelFactory(SignCheckModel signCheckModel, MyPoetryModel myPoetryModel, MyPlayListModel myPlayListModel){
        return new MyPoetryViewModel.MyPoetryViewModelFactory(signCheckModel, myPoetryModel, myPlayListModel);
    }

    @Provides
    @FragScope
    NowPoetryViewModel.NowPoetryViewModelFactory provideNowPoetryViewModelFactory(MyPlayListModel myPlayListModel, RankModel rankModel, RecommendModel recommendModel, BannerModel bannerModel){
        return new NowPoetryViewModel.NowPoetryViewModelFactory(myPlayListModel, rankModel, recommendModel, bannerModel);
    }

    @Provides
    @FragScope
    SignInViewModel.SignInViewModelFactory provideSignInViewModelFactory(SignModel signModel){
        return new SignInViewModel.SignInViewModelFactory(signModel);
    }

    @Provides
    @FragScope
    SignUpViewModel.SignUpViewModelFactory provideSignUpViewModelFactory(SignModel signModel){
        return new SignUpViewModel.SignUpViewModelFactory(signModel);
    }

    @Provides
    @FragScope
    PlayerFragmentViewModel.PlayerFragmentViewModelFactory providePlayerFragmentViewModelFactory(MyPlayListModel myPlayListModel, PlayBackStateModel playBackStateModel){
        return new PlayerFragmentViewModel.PlayerFragmentViewModelFactory(myPlayListModel, playBackStateModel);
    }
}
