package com.my.samplemusicplayertest.ui.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.my.samplemusicplayertest.ui.adapters.helpers.BaseViewHelper;
import com.my.samplemusicplayertest.ui.adapters.models.BaseRecyclerViewItem;
import com.my.samplemusicplayertest.ui.adapters.viewholders.BaseViewHolder;
import com.my.samplemusicplayertest.ui.adapters.viewholders.SongQueueViewHolder;

public class QueueRecyclerViewAdapter extends SimpleBaseRecyclerViewAdapter {

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseRecyclerViewItem.ItemType itemType = BaseRecyclerViewItem.ItemType.values()[viewType];

        switch (itemType) {
            case SONG_QUEUE:
                return BaseViewHelper.onCreateViewHolder(SongQueueViewHolder.class, parent);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBindViewHolder(this.m_vItems.get(position));
    }
}
