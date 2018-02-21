package com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timeofpoetry.timeofpoetry.timeofpoetry.GlobalApplication;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.FragmentLyricsBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.footer.LyricsViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

public class LyricsFragment extends Fragment {

    private FragComponent component;
    @Inject public LyricsViewModel.LyricsViewModelFactory viewModelFactory;

    public LyricsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final FragmentLyricsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lyrics, container, false);
        component = ((PlayerActivity) getActivity())
                .getComponent()
                .plus(new FragModule());
        component.inject(this);
        LyricsViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(LyricsViewModel.class);

        viewModel.getLyrics().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(s != null){
                    binding.lyricsText.setText(s);
                    binding.loading.setVisibility(View.GONE);
                    binding.scrollView.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.getCurrent().observe(this, new Observer<PoetryClass.Poem>() {
            @Override
            public void onChanged(@Nullable PoetryClass.Poem poem) {
                if(poem == null || binding.getPoem() != null && binding.getPoem().getDatabaseId() == poem.getDatabaseId()){
                    return;
                }
                binding.setPoem(poem);
                binding.loading.setVisibility(View.VISIBLE);
                binding.scrollView.setVisibility(View.GONE);
                viewModel.lyricsLoad();
            }
        });

        return binding.getRoot();
    }
}
