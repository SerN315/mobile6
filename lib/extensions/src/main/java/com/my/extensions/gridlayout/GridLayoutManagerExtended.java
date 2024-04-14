package com.my.extensions.gridlayout;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

public class GridLayoutManagerExtended extends GridLayoutManager {

    public RecyclerView.SmoothScroller m_vSmoothScoller;

    public GridLayoutManagerExtended(Context context, int spanCount) {
        super(context, spanCount);

        setItemPrefetchEnabled(true);
        setSmoothScrollbarEnabled(true);
        setInitialPrefetchItemCount(10);
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return false;
    }

    @Override
    protected void calculateExtraLayoutSpace(@NonNull RecyclerView.State state, @NonNull int[] extraLayoutSpace) {
        super.calculateExtraLayoutSpace(state, extraLayoutSpace);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return true;
    }
}
