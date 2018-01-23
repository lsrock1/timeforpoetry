package com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timeofpoetry.timeofpoetry.timeofpoetry.GlobalApplication;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.FragmentSignInBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities.SignInViewModel;

import javax.inject.Inject;

public class SignInFragment extends Fragment {

    private FragComponent component;
    @Inject
    SignInViewModel.SignInViewModelFactory viewModelFactory;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSignInBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        component = ((SignActivity) getActivity())
                .getComponent()
                .plus(new FragModule());
        component.inject(this);
        SignInViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(SignInViewModel.class);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }
}
