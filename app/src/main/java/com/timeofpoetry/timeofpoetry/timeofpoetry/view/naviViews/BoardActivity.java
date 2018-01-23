package com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviViews;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.ActivityBoardBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.DaggerNaviActivityComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.NaviActivityComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.NaviActivityModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.naviViews.BoardActivityViewModel;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

import javax.inject.Inject;

public class BoardActivity extends AppCompatActivity {

    @Inject
    BoardActivityViewModel.BoardActivityViewModelFactory viewModelFactory;
    NaviActivityComponent component;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBoardBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_board);
        component = DaggerNaviActivityComponent.builder()
                .build();
        component.inject(this);
        BoardActivityViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(BoardActivityViewModel.class);
        RecyclerView mRecycler = binding.boardRecycler;
        mRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        viewModel.getBoardItems().observe(this, new Observer<ArrayList<PoetryClass.BoardIdItem>>() {
            @Override
            public void onChanged(@Nullable ArrayList<PoetryClass.BoardIdItem> boardIdItems) {
                mRecycler.setAdapter(new BoardActivityAdapter(boardIdItems, viewModel));
            }
        });

        binding.boardBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
