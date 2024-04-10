package com.my.samplemusicplayertest.ui.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.samplemusicplayertest.ui.adapters.helpers.BaseViewHelper;
import com.my.samplemusicplayertest.ui.adapters.models.BaseRecyclerViewItem;
import com.my.samplemusicplayertest.ui.adapters.viewholders.BaseViewHolder;
import com.my.samplemusicplayertest.ui.adapters.viewholders.SongViewHolder;
import com.my.samplemusicplayertest.threads.UIThread;

import java.util.ArrayList;
import java.util.List;

public class LibraryRecyclerViewAdapter extends BaseRecyclerViewAdapter {
    private String TAG = getClass().getSimpleName();

    public LibraryRecyclerViewAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseRecyclerViewItem.ItemType itemType = BaseRecyclerViewItem.ItemType.values()[viewType];

        switch (itemType) {
            case SONG:
                BaseViewHolder viewHolder = BaseViewHelper.onCreateViewHolder(SongViewHolder.class, parent);
                viewHolder.onInitializeView(this.m_vLayoutViewType);
                this.m_vBaseViewHolders.add(viewHolder);
                return viewHolder;

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        holder.itemView.setOnClickListener((v) -> {
            int i = position;
            UIThread.getInstance().getMediaPlayerThread().getCallback().onClickPlay(i, getQueue());
        });
    }

    private List<Integer> getQueue() {
        List<Integer> results = new ArrayList<>();
        if (getCurrentList() != null) {
            for (BaseRecyclerViewItem item : getCurrentList()) {
                results.add(item.getId());
            }
        }
        return results;
    }
}
