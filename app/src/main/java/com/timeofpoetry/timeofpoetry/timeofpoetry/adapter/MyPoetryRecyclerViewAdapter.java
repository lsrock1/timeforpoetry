package com.timeofpoetry.timeofpoetry.timeofpoetry.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.PoetryItemNoMapleBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainView.MyPoetryViewModel;

public class MyPoetryRecyclerViewAdapter extends RecyclerView.Adapter<MyPoetryRecyclerViewAdapter.ViewHolder> {

    private MyPoetryViewModel viewModel;

    public MyPoetryRecyclerViewAdapter(MyPoetryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poetry_item_no_maple, parent, false);
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

        PoetryItemNoMapleBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
            binding.setViewModel(viewModel);
        }
    }
}
