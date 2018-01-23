package com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.timeofpoetry.timeofpoetry.timeofpoetry.GlobalApplication;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.FragmentPlayerBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.MyPoetryModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.footer.PlayerViewModel;

import javax.inject.Inject;


public class PlayerFragment extends Fragment {

    FragComponent component;
    private PlayerViewModel viewModel;
    @Inject public PlayerViewModel.PlayerViewModelFactory viewModelFactory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentPlayerBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false);
        component = ((MainActivity) getActivity())
                .getComponent()
                .plus(new FragModule());
        component.inject(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayerViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.getSeek().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                viewModel.setProgress(integer);
            }
        });

        binding.fragPlayerProgress.setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener(){
                private int progressValue;

                public void onStopTrackingTouch(SeekBar seekBar) {
                    viewModel.setSeek(progressValue);
                    MediaControllerCompat.getMediaController(getActivity()).getTransportControls().seekTo(progressValue);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressValue = progress;
                }
            }
        );

        binding.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLogin())
                    ((MainActivity) getActivity()).showLyrics();
            }
        });

        binding.fragPlayerBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLogin()) {
                    if (viewModel.bookmarkStatus.getValue() == null || viewModel.bookmarkStatus.getValue() != MyPoetryModel.CONNECTING) {
                        viewModel.bookMark().observe(PlayerFragment.this, new Observer<Integer>() {
                            @Override
                            public void onChanged(@Nullable Integer integer) {
                                bookMarkStatus(integer);
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "추가 중입니다", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.maple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLogin()) {
                    binding.setLike(Integer.toString(Integer.parseInt(binding.getLike() ) + (viewModel.like() ? 1 : -1)));
                }
            }
        });

        binding.playerClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        viewModel.getCurrent().observe(this, new Observer<PoetryClass.Poem>() {
            @Override
            public void onChanged(@Nullable PoetryClass.Poem poem) {
                if(poem == null || binding.getPoem() != null && poem.getDatabaseId() == binding.getPoem().getDatabaseId()){
                    return;
                }
                binding.setPoem(poem);
                viewModel.getLikeCount().observe(PlayerFragment.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer integer) {
                        binding.setLike(Integer.toString(integer));
                    }
                });
            }
        });

        return binding.getRoot();
    }

    private void bookMarkStatus(int status){
        if(status == MyPoetryModel.ALREADY){
            Toast.makeText(getContext(), "이미 추가된 시입니다", Toast.LENGTH_SHORT).show();
        }
        else if(status == MyPoetryModel.SUCCESS){
            Toast.makeText(getContext(), "나의 시집에 추가했습니다", Toast.LENGTH_SHORT).show();
        }
        else if(status == MyPoetryModel.ERROR){
            Toast.makeText(getContext(), "에러가 발생했습니다", Toast.LENGTH_SHORT).show();
        }
        else if(status == MyPoetryModel.NETWORK_ERROR){
            Toast.makeText(getContext(), "네트워크 연결이 불안정합니다", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean checkLogin(){
        if(viewModel.isLogin.getValue()){
            return true;
        }
        else{
            Toast.makeText(getContext(), "로그인해 주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
