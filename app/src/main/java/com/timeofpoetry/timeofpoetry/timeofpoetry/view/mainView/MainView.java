package com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainView;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.adapter.TabPagerAdapter;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.FragmentMainViewBinding;

public class MainView extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentMainViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_view, container, false);
        TabPagerAdapter mAdapter = new TabPagerAdapter(getChildFragmentManager());
        final ViewPager mPager = binding.viewpager;
        mPager.setAdapter(mAdapter);

        //텝 초기화
        TabLayout mTab = binding.tab;
        mTab.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTab));

        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }});
        return binding.getRoot();
    }

}
