package com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainView;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timeofpoetry.timeofpoetry.timeofpoetry.adapter.NowPoetryRecyclerViewAdapter;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.FragmentNowPoetryBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainView.NowPoetryViewModel;

import javax.inject.Inject;


public class NowPoetry extends Fragment {

    @Inject
    NowPoetryViewModel.NowPoetryViewModelFactory viewModelFactory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentNowPoetryBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_now_poetry, container, false);
        ((MainActivity) getActivity())
                .getComponent()
                .plus(new FragModule())
                .inject(this);
        final NowPoetryViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(NowPoetryViewModel.class);
        RecyclerView mRecycle = binding.recyclerView;
        mRecycle.setNestedScrollingEnabled(false);
        final NowPoetryRecyclerViewAdapter mAdapter = new NowPoetryRecyclerViewAdapter(viewModel);

        mRecycle.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        binding.setViewModel(viewModel);
        viewModel.getRecommend().observe(this, new Observer<PoetryModelData>() {
            @Override
            public void onChanged(@Nullable PoetryModelData poems) {
                if(poems != null && poems.getPoetry().size() > 2)
                    binding.setPoetry(poems.getPoetry());
            }
        });

        viewModel.getBanner().observe(this, new Observer<PoetryClass.Banner>() {
            @Override
            public void onChanged(@Nullable PoetryClass.Banner banner) {
                if(banner != null)
                    binding.setBannerUrl(banner.getUri());
            }
        });

        viewModel.getRank().observe(this, new Observer<PoetryModelData>() {
            @Override
            public void onChanged(@Nullable PoetryModelData poems) {
                if(poems != null && poems.getPoetry().size() > 0)
                    mAdapter.notifyItemRangeInserted(0, 10);
            }
        });

        // Inflate the layout for this fragment
        mRecycle.setAdapter(mAdapter);
        return binding.getRoot();
    }
}
