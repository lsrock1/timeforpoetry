package com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainView;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timeofpoetry.timeofpoetry.timeofpoetry.adapter.MyPoetryRecyclerViewAdapter;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.FragmentMyPoetryBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainView.MyPoetryViewModel;

import javax.inject.Inject;

public class MyPoetry extends Fragment {

    @Inject
    MyPoetryViewModel.MyPoetryViewModelFactory viewModelFactory;

    public MyPoetry() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentMyPoetryBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_poetry, container, false);
        ((MainActivity) getActivity())
                .getComponent()
                .plus(new FragModule())
                .inject(this);
        final MyPoetryViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyPoetryViewModel.class);
        binding.setViewModel(viewModel);
        RecyclerView mRecycle = binding.list;
        mRecycle.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecycle.setHasFixedSize(true);
        final MyPoetryRecyclerViewAdapter mAdapter = new MyPoetryRecyclerViewAdapter(viewModel);
        mRecycle.setAdapter(mAdapter);
        viewModel.getIsLogin().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                binding.setIsLogin(aBoolean);
                viewModel.getReloadedPoetry();
            }
        });

        viewModel.getPoetry().observe(this, new Observer<PoetryModelData>() {
            @Override
            public void onChanged(@Nullable PoetryModelData poetryModelData) {
                binding.setIsZeroQueue(poetryModelData.getPoetry().size() == 0);
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(poetryModelData.getCallback());
                result.dispatchUpdatesTo(mAdapter);
            }
        });

        return binding.getRoot();
    }
}
