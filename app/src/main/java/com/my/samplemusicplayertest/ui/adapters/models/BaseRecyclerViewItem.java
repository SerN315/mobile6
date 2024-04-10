package com.my.samplemusicplayertest.ui.adapters.models;

import android.content.Context;

public abstract class BaseRecyclerViewItem {
    public enum ItemType {
        SONG,
        SONG_QUEUE,
    }

    private final String m_vTitle;
    private final ItemType m_vItemType;

    public BaseRecyclerViewItem(String title, ItemType itemType) {
        this.m_vTitle = title;
        this.m_vItemType = itemType;
    }

    public String getTitle() { return this.m_vTitle; }

    public ItemType getItemType() { return this.m_vItemType; }

    public abstract void onCache(Context context);

    public abstract int getHashCode();

    public abstract int getId();
}
