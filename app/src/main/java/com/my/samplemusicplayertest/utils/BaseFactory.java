package com.my.samplemusicplayertest.utils;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.my.samplemusicplayertest.ui.adapters.models.BaseRecyclerViewItem;

public class BaseFactory extends DataSource.Factory<Integer, BaseRecyclerViewItem> {
    private final BaseDataSource m_vDataSource;

    public BaseFactory(BaseDataSource dataSource) {
        this.m_vDataSource = dataSource;
    }

    @NonNull
    @Override
    public DataSource<Integer, BaseRecyclerViewItem> create() {
        return this.m_vDataSource;
    }
}
