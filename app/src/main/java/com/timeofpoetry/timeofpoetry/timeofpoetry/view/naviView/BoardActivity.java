package com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviView;

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
import android.view.View;

import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.adapter.BoardActivityAdapter;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.ActivityBoardBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.DaggerNaviActivityComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.naviView.BoardActivityViewModel;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.List;

import javax.inject.Inject;

/**
 * 공지사항을 위한 view 클래스
 */

public class BoardActivity extends AppCompatActivity {

    @Inject
    BoardActivityViewModel.BoardActivityViewModelFactory viewModelFactory;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBoardBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_board);
        DaggerNaviActivityComponent.builder()
                .build()
                .inject(this);
        BoardActivityViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(BoardActivityViewModel.class);
        RecyclerView mRecycler = binding.boardRecycler;
        mRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        viewModel.getBoardItems().observe(this, new Observer<List<PoetryClass.BoardIdItem>>() {
            @Override
            public void onChanged(@Nullable List<PoetryClass.BoardIdItem> boardIdItems) {
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
