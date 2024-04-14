package com.my.samplemusicplayertest.views;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import com.my.samplemusicplayertest.ui.adapters.StateFragmentAdapter;
import com.my.samplemusicplayertest.ui.fragments.bottomview.FragmentLyrics;
import com.my.samplemusicplayertest.ui.fragments.bottomview.FragmentQueue;
import com.my.samplemusicplayertest.ui.fragments.bottomview.FragmentRelated;
import com.realgear.readable_bottom_bar.ReadableBottomBar;
import com.my.samplemusicplayertest.R;

public class MediaPlayerBottomView {
    private View m_vRootView;

    private ReadableBottomBar m_vNavigationBar;
    private ViewPager2 m_vViewPager;

    public MediaPlayerBottomView(View rootView, FragmentManager fragmentManager, Lifecycle lifecycle) {
        this.m_vRootView = rootView;

        this.m_vNavigationBar = findViewById(R.id.bottom_view_navigation_bar);
        this.m_vViewPager = findViewById(R.id.bottom_view_viewpager);

        StateFragmentAdapter adapter = new StateFragmentAdapter(fragmentManager, lifecycle);

        adapter.addFragment(FragmentLyrics.class);
        adapter.addFragment(FragmentQueue.class);
        adapter.addFragment(FragmentRelated.class);

        this.m_vViewPager.setAdapter(adapter);
        this.m_vViewPager.setOffscreenPageLimit(3);
        this.m_vNavigationBar.setupWithViewPager2(this.m_vViewPager);
    }


    public <T extends View> T findViewById(@IdRes int id) {
        return this.m_vRootView.findViewById(id);
    }
}
