package com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.timeofpoetry.timeofpoetry.timeofpoetry.GlobalApplication;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.FragmentSignUpBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities.SignUpViewModel;

import javax.inject.Inject;

public class SignUpFragment extends Fragment {

    private FragComponent component;
    @Inject
    SignUpViewModel.SignUpViewModelFactory viewModelFactory;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSignUpBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);
        component = ((SignActivity) getActivity())
                .getComponent()
                .plus(new FragModule());
        component.inject(this);
        SignUpViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(SignUpViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.getValidationStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                reactValidation(integer);
            }
        });

        return binding.getRoot();
    }

    private void reactValidation(int integer){
        switch (integer){
            case SignUpViewModel.PASSWORD_TOO_SHORT:{
                Toast.makeText(getActivity(), "비밀번호는 8자 이상이어야 합니다", Toast.LENGTH_SHORT).show();
                break;
            }
            case SignUpViewModel.WRONG_EMAIL:{
                Toast.makeText(getActivity(), "올바르지 않은 이메일입니다", Toast.LENGTH_SHORT).show();
                break;
            }
            case SignUpViewModel.AGREE_CONTRACT:{
                Toast.makeText(getActivity(), "이용약관에 동의해주세요", Toast.LENGTH_SHORT).show();
                break;
            }
            default: break;
        }
    }
}
