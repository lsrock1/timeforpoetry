package com.timeofpoetry.timeofpoetry.timeofpoetry.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainView.MonthlyPoetry;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainView.MyPoetry;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainView.NowPoetry;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    public TabPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "지금 몇 시";
            case 1:
                return "나의 시집";
            case 2:
                return "월간 몇 시";
            default:
                return null;
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new NowPoetry();
            case 1:
                return new MyPoetry();
            case 2:
                return new MonthlyPoetry();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}
