package com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainViewPager;

import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.FragmentPoemBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.NowPoetryItemBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainViewPager.NowPoetryViewModel;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by sangroklee on 2017. 11. 7..
 */

public class NowPoetryRecyclerViewAdapter extends RecyclerView.Adapter<NowPoetryRecyclerViewAdapter.ViewHolder>{

    private NowPoetryViewModel viewModel;

    NowPoetryRecyclerViewAdapter(NowPoetryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.now_poetry_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.binding.setPoem(viewModel.getItem(position));
    }

    @Override
    public int getItemCount() {
        return viewModel.getItemCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        NowPoetryItemBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
            binding.setViewModel(viewModel);
        }
    }
}
