package com.timeofpoetry.timeofpoetry.timeofpoetry.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.PoetryItemWithMapleBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.mainView.NowPoetryViewModel;

/**
 * Created by sangroklee on 2017. 11. 7..
 */

public class NowPoetryRecyclerViewAdapter extends RecyclerView.Adapter<NowPoetryRecyclerViewAdapter.ViewHolder>{

    private NowPoetryViewModel mViewModel;

    public NowPoetryRecyclerViewAdapter(NowPoetryViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poetry_item_with_maple, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mBinding.setPoem(mViewModel.getItem(position));
    }

    @Override
    public int getItemCount() {
        return mViewModel.getItemCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        PoetryItemWithMapleBinding mBinding;

        ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
            mBinding.setViewModel(mViewModel);
        }
    }
}
