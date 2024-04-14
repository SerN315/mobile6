package com.my.samplemusicplayertest.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.my.samplemusicplayertest.ui.adapters.viewholders.BaseViewHolder;
import com.my.samplemusicplayertest.ui.adapters.models.BaseRecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleBaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    public List<BaseRecyclerViewItem> m_vItems;

    public SimpleBaseRecyclerViewAdapter() {
        this.m_vItems = new ArrayList<>();
    }

    @Override
    public long getItemId(int position) {
        return this.m_vItems.get(position).getHashCode();
    }

    @Override
    public int getItemCount() {
        return this.m_vItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.m_vItems.get(position).getItemType().ordinal();
    }
}
