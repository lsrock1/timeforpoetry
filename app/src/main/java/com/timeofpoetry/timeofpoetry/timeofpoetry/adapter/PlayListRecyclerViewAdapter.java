package com.timeofpoetry.timeofpoetry.timeofpoetry.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.PlayListItemBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.footer.PlayListViewModel;

public class PlayListRecyclerViewAdapter extends RecyclerView.Adapter<PlayListRecyclerViewAdapter.ViewHolder> {

    private PlayListViewModel mViewModel;

    public PlayListRecyclerViewAdapter(PlayListViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.play_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mBinding.setPoem(mViewModel.getItem(position));
        holder.mBinding.setDisplayOption(mViewModel.getDisplayById(mViewModel.getItem(position)));
    }

    @Override
    public int getItemCount() {
        return mViewModel.getItemCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        PlayListItemBinding mBinding;

        ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
            mBinding.setViewModel(mViewModel);
        }
    }
}
