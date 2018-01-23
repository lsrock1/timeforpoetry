package com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.timeofpoetry.timeofpoetry.timeofpoetry.GlobalApplication;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.FragmentPlayListBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.footer.MyPlayListViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class MyPlayListFragment extends Fragment {

    private FragComponent component;
    @Inject public MyPlayListViewModel.MyPlayListViewModelFactory viewModelFactory;

    public MyPlayListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentPlayListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_play_list, container, false);
        component = ((MainActivity) getActivity())
                .getComponent()
                .plus(new FragModule());
        component.inject(this);
        final MyPlayListViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyPlayListViewModel.class);
        RecyclerView mRecycle = binding.list;
        mRecycle.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecycle.setHasFixedSize(true);
        final MyPlayListRecyclerViewAdapter mAdapter = new MyPlayListRecyclerViewAdapter(this, viewModel);
        binding.setViewModel(viewModel);
        mRecycle.setAdapter(mAdapter);
        viewModel.getMyPlayList().observe(this, new Observer<PoetryModelData>() {
            @Override
            public void onChanged(@Nullable PoetryModelData poetryModelData) {
                viewModel.listUpdate(poetryModelData.getChange());
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(poetryModelData.getCallback());
                result.dispatchUpdatesTo(mAdapter);
                if(!poetryModelData.isAlert()){
                    if(poetryModelData.getChange() > 0){
                        Toast.makeText(getContext(), Integer.toString(poetryModelData.getChange()) + "개의 시집을 감상목록에 담았습니다", Toast.LENGTH_SHORT).show();
                    }
                    else if(poetryModelData.getChange() < 0){
                        Toast.makeText(getContext(), Integer.toString(poetryModelData.getChange() * -1) + "개의 시집이 감상목록에서 삭제되었습니다", Toast.LENGTH_SHORT).show();
                    }
                    poetryModelData.setAlert(true);
                }
                binding.setIsZeroQueue(poetryModelData.getPoetry().size() == 0);
            }
        });

        viewModel.getCurrentPoem().observe(this, new Observer<PoetryClass.Poem>() {
            @Override
            public void onChanged(@Nullable PoetryClass.Poem poem) {
                viewModel.setCurrentPoem();
            }
        });

        viewModel.getState().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                viewModel.setPlayingShow();
            }
        });

        binding.musicFragClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return binding.getRoot();
    }
}
