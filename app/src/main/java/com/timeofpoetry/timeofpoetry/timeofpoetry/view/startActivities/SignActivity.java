package com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.timeofpoetry.timeofpoetry.timeofpoetry.GlobalApplication;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.ActivitySignBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.sign.SignModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities.SignViewModel;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.Arrays;

import javax.inject.Inject;


public class SignActivity extends AppCompatActivity {
    private ActivityComponent component;
    @Inject
    SignViewModel.SignViewModelFactory factory;
    SignViewModel viewModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        component = ((GlobalApplication) getApplication())
                .getComponent()
                .plus(new ActivityModule());

        final ActivitySignBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_sign);
        component.inject(this);
        viewModel = ViewModelProviders.of(this, factory).get(SignViewModel.class);
        viewModel.setMode(getIntent().getBooleanExtra("inorup", false));
        binding.setViewModel(viewModel);

        binding.facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewModel.getFacebookAccessToken() == null){
                    binding.facebookBtn.performClick();
                }
                else{
                    viewModel.facebookInfoRequest();
                }
            }
        });

        binding.kakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.kakaoBtn.performClick();
            }
        });
        binding.facebookBtn.setReadPermissions(Arrays.asList("email", "public_profile"));
        binding.facebookBtn.registerCallback(viewModel.getFacebookCallBackManager(), viewModel.getFacebookCallback());
        setStatusVariables();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data);
    }

    void setStatusVariables(){
        viewModel.getSignInStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                statusFunction(integer);
            }
        });

        viewModel.getSignUpStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                statusFunction(integer);
            }
        });
    }

    public void statusFunction(int integer){
        switch(integer){
            case SignModel.IN_SUCCESS:{
                inSuccess();
                break;
            }
            case SignModel.UP_SUCCESS:{
                upSuccess();
                break;
            }
            case SignModel.NETWORK_FAIL:{
                networkFail();
                break;
            }
            case SignModel.REQUEST_FAIL:{
                requestFail();
                break;
            }
            case SignModel.DUP_ID:{
                dupId();
                break;
            }
            default: break;
        }
    }

    private void inSuccess(){
        Toast.makeText(getApplicationContext(), "로그인에 성공했습니다", Toast.LENGTH_SHORT).show();
        overridePendingTransition(R.anim.slide_left_center, R.anim.slide_center_right);
        startMainActivity();
    }

    private void upSuccess(){
        Toast.makeText(getApplicationContext(), "가입했습니다 자동으로 로그인합니다", Toast.LENGTH_SHORT).show();
        startMainActivity();
    }

    private void dupId(){
        Toast.makeText(getApplicationContext(), "동일한 이메일이 존재합니다", Toast.LENGTH_SHORT).show();
        viewModel.socialLogOut();
    }

    private void networkFail(){
        Toast.makeText(getApplicationContext(), "네트워크 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
    }

    private void requestFail(){
        Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
        viewModel.socialLogOut();
    }

    private void startMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (viewModel.getStack()) {
            viewModel.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    public ActivityComponent getComponent(){
        return component;
    }
}
