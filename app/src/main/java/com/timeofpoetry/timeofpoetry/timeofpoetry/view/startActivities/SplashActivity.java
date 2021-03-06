package com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.timeofpoetry.timeofpoetry.timeofpoetry.GlobalApplication;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.ActivitySplashBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities.SplashViewModel;

import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity {

    private final int mDisplayLength = 1000;

    @Inject
    SplashViewModel.SplashViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        ((GlobalApplication) getApplication())
                .getComponent()
                .plus(new ActivityModule())
                .inject(this);
        SplashViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel.class);
        Intent intent;
        if(viewModel.isLogin()) {
            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else{
            intent = new Intent(this, AuthorityActivity.class);
        }

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, mDisplayLength);
    }
}
