package com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.timeofpoetry.timeofpoetry.timeofpoetry.GlobalApplication;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.adapter.PlayListRecyclerViewAdapter;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.ActivityPlayListBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.interfaces.OnPlayerFragmentInteractionListener;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.footer.PlayListViewModel;

import javax.inject.Inject;

public class PlayListActivity extends AppCompatActivity implements OnPlayerFragmentInteractionListener{

    private ActivityComponent mComponent;
    @Inject public PlayListViewModel.PlayListViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = ((GlobalApplication) getApplication())
                .getComponent()
                .plus(new ActivityModule());
        mComponent.inject(this);
        ActivityPlayListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_play_list);
        PlayListViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayListViewModel.class);


        RecyclerView mRecycle = binding.list;
        mRecycle.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecycle.setHasFixedSize(true);
        final PlayListRecyclerViewAdapter mAdapter = new PlayListRecyclerViewAdapter(viewModel);
        binding.setViewModel(viewModel);
        mRecycle.setAdapter(mAdapter);
        viewModel.getPoetry().observe(this, new Observer<PoetryModelData>() {
            @Override
            public void onChanged(@Nullable PoetryModelData poetryModelData) {
                viewModel.listUpdate(poetryModelData.getChange());
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(poetryModelData.getCallback());
                result.dispatchUpdatesTo(mAdapter);
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

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public ActivityComponent getComponent(){
        return mComponent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_top_down);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return treatMediaButton(this, event) || super.onKeyDown(keyCode, event);
    }
}
