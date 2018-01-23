package com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer;

import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryModelData;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.FragmentPoemBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.footer.MyPlayListViewModel;

public class MyPlayListRecyclerViewAdapter extends RecyclerView.Adapter<MyPlayListRecyclerViewAdapter.ViewHolder> {

    private MyPlayListViewModel viewModel;

    MyPlayListRecyclerViewAdapter(MyPlayListFragment m, MyPlayListViewModel model) {
        viewModel = model;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_poem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.binding.setPoem(viewModel.getItem(position));
        holder.binding.setDisplayOption(viewModel.getDisplayById(viewModel.getItem(position)));
    }

    @Override
    public int getItemCount() {
        return viewModel.getItemCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        FragmentPoemBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
            binding.setViewModel(viewModel);
        }
    }
}
