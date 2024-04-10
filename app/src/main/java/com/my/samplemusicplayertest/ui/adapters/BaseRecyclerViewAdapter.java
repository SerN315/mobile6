package com.my.samplemusicplayertest.ui.adapters;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.my.samplemusicplayertest.ui.adapters.models.BaseRecyclerViewItem;
import com.my.samplemusicplayertest.ui.adapters.viewholders.BaseViewHolder;
import com.my.samplemusicplayertest.utils.UIThreadExecutor;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter extends PagedListAdapter<BaseRecyclerViewItem, BaseViewHolder> {

    public enum ViewType {
        LIST, GRID
    }

    ViewType m_vLayoutViewType;

    RecyclerView m_vRecyclerView;

    public UIThreadExecutor m_vMainThread;

    public List<BaseViewHolder> m_vBaseViewHolders;


    private static final DiffUtil.ItemCallback<BaseRecyclerViewItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull BaseRecyclerViewItem oldItem, @NonNull BaseRecyclerViewItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull BaseRecyclerViewItem oldItem, @NonNull BaseRecyclerViewItem newItem) {
            return oldItem.getHashCode() == newItem.getHashCode();
        }
    };

    public BaseRecyclerViewAdapter(RecyclerView recyclerView) {
        super(DIFF_CALLBACK);

        this.m_vRecyclerView = recyclerView;
        this.m_vMainThread = new UIThreadExecutor();
        this.m_vBaseViewHolders = new ArrayList<>();
    }

    public void onUpdateItemsLayout() {
        for (BaseViewHolder viewHolder : m_vBaseViewHolders) {
            viewHolder.onInitializeView(this.m_vLayoutViewType);
        }

        for (BaseViewHolder viewHolder : m_vBaseViewHolders) {
            viewHolder.onReloadData();
        }
    }

    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (!holder.isViewsCreated()) {
            holder.onCreateViews();
        }

        holder.onBindViewHolder(this.getItem(position));
    }

    public void setAdapterViewType(ViewType viewType) {
        if (this.m_vLayoutViewType == viewType)
            return;

        this.m_vLayoutViewType = viewType;
        this.onUpdateItemsLayout();
    }

    public void setItems(PagedList<BaseRecyclerViewItem> items) {
        this.submitList(items);
    }

    @Override
    public long getItemId(int position) {
        return this.getItem(position).getHashCode();
    }

    @Override
    public int getItemViewType(int position) {
        return this.getItem(position).getItemType().ordinal();
    }

    public ViewType getViewType() {
        return this.m_vLayoutViewType;
    }
}
