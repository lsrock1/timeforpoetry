package com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.timeofpoetry.timeofpoetry.timeofpoetry.GlobalApplication;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.ActivityPlayerBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.interfaces.OnPlayerFragmentInteractionListener;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.MyPoetryModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.PlayerFragment;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.footer.PlayerViewModel;

import javax.inject.Inject;

public class PlayerActivity extends AppCompatActivity implements OnPlayerFragmentInteractionListener{

    private ActivityComponent mComponent;
    @Inject
    PlayerViewModel.PlayerViewModelFactory viewModelFactory;
    private PlayerViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = ((GlobalApplication) getApplication())
                .getComponent()
                .plus(new ActivityModule());
        mComponent.inject(this);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayerViewModel.class);
        ActivityPlayerBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_player);

        binding.setViewModel(mViewModel);
        mViewModel.getSeek().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                mViewModel.setProgress(integer);
            }
        });

        binding.fragPlayerProgress.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){
                    private int progressValue;

                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mViewModel.setSeek(progressValue);
                        MediaControllerCompat.getMediaController(PlayerActivity.this).getTransportControls().seekTo(progressValue);
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
                    mViewModel.lyricToggle();
            }
        });

        binding.fragPlayerBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLogin()) {
                    if (mViewModel.bookmarkStatus.getValue() == null || mViewModel.bookmarkStatus.getValue() != MyPoetryModel.CONNECTING) {
                        mViewModel.bookMark().observe(PlayerActivity.this, new Observer<Integer>() {
                            @Override
                            public void onChanged(@Nullable Integer integer) {
                                bookMarkStatus(integer);
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "추가 중입니다", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.maple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLogin()) {
                    binding.setLike(Integer.toString(Integer.parseInt(binding.getLike() ) + (mViewModel.like() ? 1 : -1)));
                }
            }
        });

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.hold, R.anim.slide_top_down);
            }
        });

        mViewModel.getCurrent().observe(this, new Observer<PoetryClass.Poem>() {
            @Override
            public void onChanged(@Nullable PoetryClass.Poem poem) {
                if(poem == null || binding.getPoem() != null && poem.getDatabaseId() == binding.getPoem().getDatabaseId()){
                    return;
                }
                binding.setPoem(poem);
                mViewModel.getLikeCount().observe(PlayerActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer integer) {
                        binding.setLike(Integer.toString(integer));
                    }
                });
            }
        });
    }

    private void bookMarkStatus(int status){
        if(status == MyPoetryModel.ALREADY){
            Toast.makeText(getApplicationContext(), "이미 추가된 시입니다", Toast.LENGTH_SHORT).show();
        }
        else if(status == MyPoetryModel.SUCCESS){
            Toast.makeText(getApplicationContext(), "나의 시집에 추가했습니다", Toast.LENGTH_SHORT).show();
        }
        else if(status == MyPoetryModel.ERROR){
            Toast.makeText(getApplicationContext(), "에러가 발생했습니다", Toast.LENGTH_SHORT).show();
        }
        else if(status == MyPoetryModel.NETWORK_ERROR){
            Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean checkLogin(){
        if(mViewModel.isLogin.getValue()){
            return true;
        }
        else{
            Toast.makeText(getApplicationContext(), "로그인해 주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if(!mViewModel.onBackPressed()) {
            super.onBackPressed();
            overridePendingTransition(0, R.anim.slide_top_down);
        }
    }

    public ActivityComponent getComponent(){
        return mComponent;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return treatMediaButton(this, event) || super.onKeyDown(keyCode, event);
    }
}
