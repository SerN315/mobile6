package com.my.samplemusicplayertest.views.panels;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.my.samplemusicplayertest.threads.UIThread;
import com.my.samplemusicplayertest.ui.adapters.StateFragmentAdapter;
import com.my.samplemusicplayertest.ui.fragments.FragmentHome;
import com.my.samplemusicplayertest.ui.fragments.FragmentLibrary;
import com.my.samplemusicplayertest.utils.BackEventHandler;
import com.realgear.multislidinguppanel.BasePanelView;
import com.realgear.multislidinguppanel.MultiSlidingUpPanelLayout;
import com.realgear.readable_bottom_bar.ReadableBottomBar;
import com.my.samplemusicplayertest.R;


public class RootNavigationBarPanel extends BasePanelView {

    private ViewPager2 rootViewPager;
    private ReadableBottomBar rootNavigationBar;

    private Runnable m_vBackEvent = () -> {
        rootNavigationBar.selectTabAt(0, true);
    };

    private Runnable m_vPanelBackEvent = this::collapsePanel;

    public RootNavigationBarPanel(@NonNull Context context, MultiSlidingUpPanelLayout panelLayout) {
        super(context, panelLayout);

        getContext().setTheme(R.style.Theme_SampleMusicPlayerTest);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_root_navigation_bar, this, true);
    }

    @Override
    public void onCreateView() {
        this.setPanelState(MultiSlidingUpPanelLayout.COLLAPSED);
        this.setSlideDirection(MultiSlidingUpPanelLayout.SLIDE_DOWN);

        this.setPeakHeight(getResources().getDimensionPixelSize(R.dimen.navigation_bar_height));
    }

    @Override
    public void onBindView() {
        rootViewPager = getMultiSlidingUpPanel().findViewById(R.id.root_view_pager);
        rootNavigationBar = findViewById(R.id.root_navigation_bar);

        StateFragmentAdapter adapter = new StateFragmentAdapter(getSupportFragmentManager(), getLifecycle());

        adapter.addFragment(FragmentHome.class);
        adapter.addFragment(FragmentLibrary.class);

        rootViewPager.setAdapter(adapter);
        rootViewPager.setOffscreenPageLimit(2);
        rootNavigationBar.setupWithViewPager2(rootViewPager);

        rootViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position != 0) {
                    BackEventHandler.getInstance().addBackEvent(m_vBackEvent);
                }
                else {
                    BackEventHandler.getInstance().removeBackEvent(m_vBackEvent);
                }
            }
        });

        rootNavigationBar.setOnTabSelectListener(new ReadableBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int prevIndex, @Nullable ReadableBottomBar.Tab prevTab, int newIndex, @NonNull ReadableBottomBar.Tab newTab) {
                if (newTab.getId() == R.id.tab_settings) {
                    expandPanel();
                }
                else {
                    if (getPanelState() == MultiSlidingUpPanelLayout.EXPANDED) {
                        collapsePanel();
                    }
                }
            }


            @Override
            public void onTabReselected(int i, @NonNull ReadableBottomBar.Tab tab) {

            }
        });
    }

    @Override
    public void onPanelStateChanged(int panelSate) {
        UIThread.getInstance().onPanelStateChanged(this.getClass(), panelSate);

        if (panelSate == MultiSlidingUpPanelLayout.COLLAPSED) {
            this.setSlideDirection(MultiSlidingUpPanelLayout.SLIDE_DOWN);
            if (rootViewPager != null) {
                int index = rootViewPager.getCurrentItem();
                rootNavigationBar.selectTabAt(index, true);
            }
            BackEventHandler.getInstance().removeBackEvent(this.m_vPanelBackEvent);
        }

        if (panelSate == MultiSlidingUpPanelLayout.EXPANDED) {
            this.setSlideDirection(MultiSlidingUpPanelLayout.SLIDE_VERTICAL);
            BackEventHandler.getInstance().addBackEvent(this.m_vPanelBackEvent);
        }
    }
}
