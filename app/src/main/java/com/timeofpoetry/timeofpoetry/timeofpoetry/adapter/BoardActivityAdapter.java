package com.timeofpoetry.timeofpoetry.timeofpoetry.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.BoardItemBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.naviView.BoardActivityViewModel;

import java.util.List;

/**
 * view/naviView/BoardActivity recycler를 위한 어뎁터
 */

public class BoardActivityAdapter extends RecyclerView.Adapter<BoardActivityAdapter.ViewHolder> {

    private List<PoetryClass.BoardIdItem> mValues;
    private BoardActivityViewModel mViewModel;

    public BoardActivityAdapter(List<PoetryClass.BoardIdItem> items, BoardActivityViewModel viewModel) {
        mValues = items;
        this.mViewModel = viewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.board_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mBinding.setItem(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        BoardItemBinding mBinding;

        ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
            mBinding.setViewModel(mViewModel);
            setIsRecyclable(false);
            mBinding.expand.setInRecyclerView(true);
        }
    }
}
