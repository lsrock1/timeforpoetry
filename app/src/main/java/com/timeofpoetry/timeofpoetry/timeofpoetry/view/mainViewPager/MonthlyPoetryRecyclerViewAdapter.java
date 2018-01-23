package com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainViewPager;

import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.FragmentPoemBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.PoetryItemBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainViewPager.MonthlyPoetryViewModel;

import java.util.ArrayList;

public class MonthlyPoetryRecyclerViewAdapter extends RecyclerView.Adapter<MonthlyPoetryRecyclerViewAdapter.ViewHolder> {

    private MonthlyPoetryViewModel viewModel;

    MonthlyPoetryRecyclerViewAdapter(MonthlyPoetryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poetry_item, parent, false);
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

        PoetryItemBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
            binding.setViewModel(viewModel);
        }
    }
}
