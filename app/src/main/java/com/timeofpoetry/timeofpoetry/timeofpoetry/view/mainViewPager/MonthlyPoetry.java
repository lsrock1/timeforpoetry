package com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainViewPager;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timeofpoetry.timeofpoetry.timeofpoetry.GlobalApplication;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.FragmentMonthlyPoetryBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainViewPager.MonthlyPoetryViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

public class MonthlyPoetry extends Fragment {

    private FragComponent component;
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
        component = ((MainActivity) getActivity())
                .getComponent()
                .plus(new FragModule());
        component.inject(this);
        MonthlyPoetryViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(MonthlyPoetryViewModel.class);
        RecyclerView mRecycle = binding.poemFraglist;
        mRecycle.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecycle.setHasFixedSize(true);
        mRecycle.setFocusable(false);
        binding.setViewModel(viewModel);
        final MonthlyPoetryRecyclerViewAdapter mAdapter = new MonthlyPoetryRecyclerViewAdapter(viewModel);
        mRecycle.setAdapter(mAdapter);

        viewModel.getMonthlyPoetry().observe(this, new Observer<ArrayList<PoetryClass.Poem>>() {
            @Override
            public void onChanged(@Nullable ArrayList<PoetryClass.Poem> poems) {
                mAdapter.notifyDataSetChanged();
            }
        });
        return binding.getRoot();
    }
}
