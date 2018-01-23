package com.timeofpoetry.timeofpoetry.timeofpoetry.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviViews.BoardActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.GlobalApplication;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviViews.SettingVersionActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.ActivityMainBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.NavHeaderMainBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer.MediaPlaybackService;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviViews.OpenSourceActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities.AuthorityActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.MainActivityViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.concerns.BackPressCloseHandler;
import com.tsengvn.typekit.TypekitContextWrapper;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    ActivityComponent component;
    ActivityMainBinding binding;
    @Inject MainActivityViewModel.MainActivityViewModelFactory factory;
    MainActivityViewModel viewModel;
    private MediaBrowserCompat mMediaBrowser;
    private BackPressCloseHandler mHandler;

    private final MediaBrowserCompat.ConnectionCallback mConnectionCallbacks =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    super.onConnected();
                    try {
                        MediaSessionCompat.Token token = mMediaBrowser.getSessionToken();
                        MediaControllerCompat mediaController = new MediaControllerCompat(MainActivity.this, token);
                        MediaControllerCompat.setMediaController(MainActivity.this, mediaController);
                        MediaControllerCompat.getMediaController(MainActivity.this).registerCallback(controllerCallback);
                    } catch (RemoteException e) {
                        //세션 토큰 초기화 시 발생하는 에러를 잡기 위함
                    }
                }

                @Override
                public void onConnectionSuspended() {
                    super.onConnectionSuspended();
                    //서비스가 크래쉬됐을 때 컨트롤러를 자동 재열결 시 까지 보이지 않게 만듭니다
                }

                @Override
                public void onConnectionFailed() {
                    super.onConnectionFailed();
                    //서비스가 연결을 거부했을 때
                }
            };

    private final MediaControllerCompat.Callback controllerCallback = new MediaControllerCompat.Callback(){};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = ((GlobalApplication) getApplication())
                .getComponent()
                .plus(new ActivityModule());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        component.inject(this);
        viewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);
        binding.setViewModel(viewModel);

        mMediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MediaPlaybackService.class),
                mConnectionCallbacks,
                null);
        mHandler = new BackPressCloseHandler(this);

        viewModel.getCurrentMedia().observe(this, new Observer<PoetryClass.Poem>() {
            @Override
            public void onChanged(@Nullable PoetryClass.Poem poem) {
                if(poem == null || binding.getPoem() != null && binding.getPoem().getDatabaseId() == poem.getDatabaseId()) return;
                binding.setPoem(poem);
            }
        });

        viewModel.getState().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                viewModel.setState(integer);
            }
        });

        viewModel.getMode().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer mode) {
                if(viewModel.setMode(mode)){
                    if(mode == MyPlayListModel.SHUFFLE){
                        Toast.makeText(getApplicationContext(), "랜덤으로 재생합니다", Toast.LENGTH_SHORT).show();
                    }
                    else if(mode == PlaybackStateCompat.REPEAT_MODE_ALL){
                        Toast.makeText(getApplicationContext(), "전체 시를 반복합니다", Toast.LENGTH_SHORT).show();
                    }
                    else if(mode == PlaybackStateCompat.REPEAT_MODE_ONE){
                        Toast.makeText(getApplicationContext(), "시 하나를 반복합니다", Toast.LENGTH_SHORT).show();
                    }
                    else if(mode == PlaybackStateCompat.REPEAT_MODE_NONE){
                        Toast.makeText(getApplicationContext(), "반복을 사용하지 않습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        viewModel.getIsLogin().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                viewModel.setLogin(aBoolean);
            }
        });

        initNav();
        initHeader();
        buildTransportControls();
        mMediaBrowser.connect();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mMediaBrowser.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (MediaControllerCompat.getMediaController(this) != null) {
            MediaControllerCompat.getMediaController(this).unregisterCallback(controllerCallback);
        }
        mMediaBrowser.disconnect();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = binding.drawerLayout;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(viewModel.onBackPressed()){
            return;
        }
        else {
            mHandler.onBackPressed();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.setting) {
            startActivity(new Intent(this, SettingVersionActivity.class));
        } else if (id == R.id.board) {
            startActivity(new Intent(this, BoardActivity.class));
        }
        else if(id == R.id.open){
            startActivity(new Intent(this, OpenSourceActivity.class));
        }
//        } else if (id == R.id.ask) {
//
//        }

        DrawerLayout drawer = binding.drawerLayout;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initNav(){
        NavHeaderMainBinding _bind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.nav_header_main, binding
                .navView, false);
        binding.navView.addHeaderView(_bind.getRoot());
        _bind.setViewModel(viewModel);
        _bind.navLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewModel.getIsLogin().getValue()){
                    viewModel.logOut();
                }
                else{
                    Intent authorIntent = new Intent(MainActivity.this, AuthorityActivity.class);
                    startActivity(authorIntent);
                }
            }
        });
        binding.navView.setNavigationItemSelectedListener(this);
    }

    private void initHeader(){
        setSupportActionBar(binding.appBarMain.toolbar);
        viewModel.setActionBar(getSupportActionBar());

        final DrawerLayout drawer = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, binding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_top_mennu);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        toggle.syncState();
    }

    private void buildTransportControls()
    {
        binding.appBarMain.contentMain.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel.getIsLogin().getValue()) {
                    MediaControllerCompat.getMediaController(MainActivity.this).getTransportControls().play();
                    Toast.makeText(getApplicationContext(), "재생을 요청합니다", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "로그인해 주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.appBarMain.contentMain.pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {MediaControllerCompat.getMediaController(MainActivity.this).getTransportControls().pause();}
        });

        binding.appBarMain.contentMain.rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {MediaControllerCompat.getMediaController(MainActivity.this).getTransportControls().rewind();}
        });

        binding.appBarMain.contentMain.forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaControllerCompat.getMediaController(MainActivity.this).getTransportControls().fastForward();
            }
        });
    };

    public void showLyrics(){
        viewModel.showLyrics();
    }

    public ActivityComponent getComponent(){
        return component;
    }
}