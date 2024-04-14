package com.my.samplemusicplayertest.ui.adapters.viewholders;

import android.os.Handler;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.my.samplemusicplayertest.R;
import com.my.samplemusicplayertest.ui.adapters.BaseRecyclerViewAdapter;
import com.my.samplemusicplayertest.ui.adapters.models.BaseRecyclerViewItem;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    BaseRecyclerViewAdapter.ViewType m_vViewType;

    private boolean m_vIsViewsCreated = false;

    public ShimmerFrameLayout m_vShimmerEffect;

    public Handler m_vUIHandler;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);

        this.m_vShimmerEffect = findViewById(R.id.shimmer_effect);
        this.m_vShimmerEffect.startShimmer();
        this.m_vUIHandler = new Handler(itemView.getContext().getMainLooper());
    }

    public abstract void onInitializeView(BaseRecyclerViewAdapter.ViewType viewType);
    public void onCreateViews() {
        this.m_vIsViewsCreated = true;
    }
    public abstract void onBindViewHolder(BaseRecyclerViewItem viewItem);
    public abstract void onReloadData();

    public boolean isViewsCreated() {
        return this.m_vIsViewsCreated;
    }

    public <T extends android.view.View> T findViewById(@IdRes int id) {
        return this.itemView.findViewById(id);
    }
}
