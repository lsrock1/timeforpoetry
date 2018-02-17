package com.timeofpoetry.timeofpoetry.timeofpoetry.view;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaControllerCompat;
import android.util.Log;
import android.view.KeyEvent;
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
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviViews.SettingVersionActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.ActivityMainBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.NavHeaderMainBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviViews.OpenSourceActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities.AuthorityActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.MainActivityViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.concerns.BackPressCloseHandler;
import com.tsengvn.typekit.TypekitContextWrapper;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PlayerFragment.OnFragmentInteractionListener{

    ActivityComponent component;
    ActivityMainBinding binding;
    @Inject MainActivityViewModel.MainActivityViewModelFactory factory;
    MainActivityViewModel viewModel;
    private BackPressCloseHandler mHandler;

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

        mHandler = new BackPressCloseHandler(this);

        viewModel.getIsLogin().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                viewModel.setLogin(aBoolean);
            }
        });

        initNav();
        initHeader();
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

    public ActivityComponent getComponent(){
        return component;
    }

    @Override
    public boolean treatMediaButton(KeyEvent event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }
        MediaControllerCompat.getMediaController(this).dispatchMediaButtonEvent(event);
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (treatMediaButton(event)) {
            return super.onKeyDown(keyCode, event);
        }
        else{
            return true;
        }
    }
}