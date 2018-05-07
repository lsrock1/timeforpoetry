package com.timeofpoetry.timeofpoetry.timeofpoetry.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.timeofpoetry.timeofpoetry.timeofpoetry.interfaces.OnPlayerFragmentInteractionListener;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviView.BoardActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.GlobalApplication;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviView.SettingVersionActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.ActivityMainBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.NavHeaderMainBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviView.OpenSourceActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities.AuthorityActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.MainActivityViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.concerns.BackPressCloseHandler;
import com.tsengvn.typekit.TypekitContextWrapper;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnPlayerFragmentInteractionListener{

    private ActivityComponent mComponent;
    private ActivityMainBinding mBinding;
    @Inject MainActivityViewModel.MainActivityViewModelFactory factory;
    private MainActivityViewModel mViewModel;
    private BackPressCloseHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = ((GlobalApplication) getApplication())
                .getComponent()
                .plus(new ActivityModule());
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mComponent.inject(this);
        mViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);
        mBinding.setViewModel(mViewModel);

        mHandler = new BackPressCloseHandler(this);

        mViewModel.getIsLogin().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                mViewModel.setLogin(aBoolean);
            }
        });

        initNav();
        initHeader();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = mBinding.drawerLayout;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(mViewModel.onBackPressed()){
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

        DrawerLayout drawer = mBinding.drawerLayout;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initNav(){
        NavHeaderMainBinding _bind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.nav_header_main, mBinding
                .navView, false);
        mBinding.navView.addHeaderView(_bind.getRoot());
        _bind.setViewModel(mViewModel);
        _bind.navLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mViewModel.getIsLogin().getValue()){
                    mViewModel.logOut();
                }
                else{
                    Intent authorIntent = new Intent(MainActivity.this, AuthorityActivity.class);
                    startActivity(authorIntent);
                }
            }
        });
        mBinding.navView.setNavigationItemSelectedListener(this);
    }

    private void initHeader(){
        setSupportActionBar(mBinding.appBarMain.toolbar);
        mViewModel.setActionBar(getSupportActionBar());

        final DrawerLayout drawer = mBinding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mBinding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
        return mComponent;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return treatMediaButton(this, event) || super.onKeyDown(keyCode, event);
    }
}