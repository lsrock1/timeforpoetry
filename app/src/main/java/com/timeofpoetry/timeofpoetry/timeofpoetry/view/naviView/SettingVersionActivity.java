package com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviView;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.timeofpoetry.timeofpoetry.timeofpoetry.GlobalApplication;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.ActivitySettingVersionBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.UserInfoModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.naviView.SettingVersionViewModel;
import com.tsengvn.typekit.TypekitContextWrapper;

import javax.inject.Inject;

public class SettingVersionActivity extends AppCompatActivity {

    @Inject SettingVersionViewModel.SettingVersionViewModelFactory viewModelFactory;
    SettingVersionViewModel viewModel;
    private ActivityComponent component;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingVersionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_version);
        component = ((GlobalApplication) getApplication())
                .getComponent()
                .plus(new ActivityModule());
        component.inject(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SettingVersionViewModel.class);
        binding.setViewModel(viewModel);

        viewModel.getData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                switch (integer){
                    case UserInfoModel.SUCCESS:{
                        successUpdate();
                        break;
                    }
                    case UserInfoModel.NETWORK_ERROR:{
                        networkFail();
                        break;
                    }
                    case UserInfoModel.FAIL:{
                        failUpdate();
                        break;
                    }
                    default:{
                        break;
                    }
                }
            }
        });

        viewModel.getUserInitData().observe(this, new Observer<PoetryClass.GetAddInfo>() {
            @Override
            public void onChanged(@Nullable PoetryClass.GetAddInfo getAddInfo) {
                if(getAddInfo != null){
                    viewModel.setUserInitData(getAddInfo);
                    binding.likePoem.setEnabled(true);
                    binding.likePoet.setEnabled(true);
                    binding.likeSeason.setEnabled(true);
                }
            }
        });

        binding.onlyLogin.setVisibility(viewModel.getIsLogin() ? View.VISIBLE : View.GONE);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.logOut();
                onBackPressed();
            }
        });

        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.userInfoUpdate();
            }
        });
    }

    public void networkFail(){
        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다", Toast.LENGTH_SHORT).show();
    }

    public void successUpdate(){
        Toast.makeText(getApplicationContext(), "정보 업데이트에 성공했습니다", Toast.LENGTH_SHORT).show();
    }

    public void failUpdate(){
        Toast.makeText(getApplicationContext(), "정보 업데이트에 실패했습니다", Toast.LENGTH_SHORT).show();
    }
}
