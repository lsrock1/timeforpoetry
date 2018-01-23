package com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainViewPager;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.timeofpoetry.timeofpoetry.timeofpoetry.GlobalApplication;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.FragmentMainViewBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.FragmentNowPoetryBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.NetworkTasks;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainViewPager.NowPoetryViewModel;

import java.util.ArrayList;

import javax.inject.Inject;


public class NowPoetry extends Fragment {

    private FragComponent component;
    @Inject
    NowPoetryViewModel.NowPoetryViewModelFactory viewModelFactory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentNowPoetryBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_now_poetry, container, false);
        component = ((MainActivity) getActivity())
                .getComponent()
                .plus(new FragModule());
        component.inject(this);
        final NowPoetryViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(NowPoetryViewModel.class);
        RecyclerView mRecycle = binding.recyclerView;
        final NowPoetryRecyclerViewAdapter mAdapter = new NowPoetryRecyclerViewAdapter(viewModel);

        mRecycle.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        binding.setViewModel(viewModel);
        viewModel.getRecommend().observe(this, new Observer<ArrayList<PoetryClass.Poem>>() {
            @Override
            public void onChanged(@Nullable ArrayList<PoetryClass.Poem> poems) {
                if(poems.size() > 2)
                    binding.setPoetry(poems);
            }
        });

        viewModel.getBanner().observe(this, new Observer<PoetryClass.Banner>() {
            @Override
            public void onChanged(@Nullable PoetryClass.Banner banner) {
                binding.setBannerUrl(banner.getUri());
            }
        });

        viewModel.getRank().observe(this, new Observer<ArrayList<PoetryClass.Poem>>() {
            @Override
            public void onChanged(@Nullable ArrayList<PoetryClass.Poem> poems) {
                if(poems.size() > 0)
                    mAdapter.notifyItemRangeInserted(0, 10);
            }
        });

        // Inflate the layout for this fragment
        mRecycle.setAdapter(mAdapter);
        return binding.getRoot();
    }
}
