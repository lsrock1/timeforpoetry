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
import android.view.View;
import android.widget.Toast;

import com.timeofpoetry.timeofpoetry.timeofpoetry.GlobalApplication;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.ActivityPlayListBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.footer.PlayListViewModel;

import javax.inject.Inject;

public class PlayListActivity extends AppCompatActivity implements com.timeofpoetry.timeofpoetry.timeofpoetry.view.PlayerFragment.OnFragmentInteractionListener{

    private ActivityComponent component;
    @Inject public PlayListViewModel.PlayListViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = ((GlobalApplication) getApplication())
                .getComponent()
                .plus(new ActivityModule());
        component.inject(this);
        ActivityPlayListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_play_list);
        PlayListViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayListViewModel.class);


        RecyclerView mRecycle = binding.list;
        mRecycle.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecycle.setHasFixedSize(true);
        final PlayListRecyclerViewAdapter mAdapter = new PlayListRecyclerViewAdapter(viewModel);
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
                        Toast.makeText(getApplicationContext(), Integer.toString(poetryModelData.getChange()) + "개의 시집을 감상목록에 담았습니다", Toast.LENGTH_SHORT).show();
                    }
                    else if(poetryModelData.getChange() < 0){
                        Toast.makeText(getApplicationContext(), Integer.toString(poetryModelData.getChange() * -1) + "개의 시집이 감상목록에서 삭제되었습니다", Toast.LENGTH_SHORT).show();
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

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public ActivityComponent getComponent(){
        return component;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_top_down);
    }
}
