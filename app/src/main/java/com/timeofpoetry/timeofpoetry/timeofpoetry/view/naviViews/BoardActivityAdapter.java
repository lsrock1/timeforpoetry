package com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviViews;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.BoardItemBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.naviViews.BoardActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class BoardActivityAdapter extends RecyclerView.Adapter<BoardActivityAdapter.ViewHolder> {

    private List<PoetryClass.BoardIdItem> values;
    private BoardActivityViewModel viewModel;

    BoardActivityAdapter(List<PoetryClass.BoardIdItem> items, BoardActivityViewModel viewModel) {
        values = items;
        this.viewModel = viewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.board_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.binding.setItem(values.get(position));
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        BoardItemBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
            binding.setViewModel(viewModel);
            setIsRecyclable(false);
            binding.expand.setInRecyclerView(true);
        }
    }
}
