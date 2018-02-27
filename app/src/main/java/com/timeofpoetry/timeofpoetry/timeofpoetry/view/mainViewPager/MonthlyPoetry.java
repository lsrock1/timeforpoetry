package com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainViewPager;

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

import com.timeofpoetry.timeofpoetry.timeofpoetry.adapter.MonthlyPoetryRecyclerViewAdapter;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.FragmentMonthlyPoetryBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainViewPager.MonthlyPoetryViewModel;

import java.util.List;

import javax.inject.Inject;

public class MonthlyPoetry extends Fragment {

    @Inject
    MonthlyPoetryViewModel.MonthlyPoetryViewModelFactory viewModelFactory;

    public MonthlyPoetry() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentMonthlyPoetryBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_monthly_poetry, container, false);
        ((MainActivity) getActivity())
                .getComponent()
                .plus(new FragModule())
                .inject(this);
        MonthlyPoetryViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(MonthlyPoetryViewModel.class);
        RecyclerView mRecycle = binding.poemFraglist;
        mRecycle.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecycle.setHasFixedSize(true);
        mRecycle.setFocusable(false);
        binding.setViewModel(viewModel);
        final MonthlyPoetryRecyclerViewAdapter mAdapter = new MonthlyPoetryRecyclerViewAdapter(viewModel);
        mRecycle.setAdapter(mAdapter);

        viewModel.getMonthlyPoetry().observe(this, new Observer<PoetryModelData>() {
            @Override
            public void onChanged(@Nullable PoetryModelData poems) {
                if(poems != null && poems.getPoetry().size() > 0) {
                    binding.scroll.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        return binding.getRoot();
    }
}
