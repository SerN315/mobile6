package com.my.samplemusicplayertest.ui.adapters.helpers;

import android.content.Context;
import android.graphics.Point;
import android.util.Size;
import android.view.WindowManager;

import com.my.samplemusicplayertest.utils.ViewsUtil;
import com.my.samplemusicplayertest.R;

import java.util.LinkedHashMap;

public class CacheHelper {
    private static CacheHelper m_vInstance;

    private LinkedHashMap<Integer, Boolean> m_vCachedData;

    private int m_vWidth;
    private int m_vHeight;

    private float m_vScaleFactor = 0.9F;

    public enum IMAGE_SIZE {
        SMALL, MEDIUM
    }

    private Context m_vContext;

    private CacheHelper(Context context) {
        this.m_vCachedData = new LinkedHashMap<>();
        this.m_vContext = context;
    }

    public void setIsCached(int signature, boolean isCached) {
        if(m_vCachedData.containsKey(signature)) {
            m_vCachedData.replace(signature, isCached);
        }
        else {
            m_vCachedData.put(signature, isCached);
        }
    }

    public boolean isCached(int signature) {
        if(!m_vCachedData.containsKey(signature)) {
            m_vCachedData.put(signature, false);
        }

        return m_vCachedData.get(signature);
    }

    public Size getSize(IMAGE_SIZE imageSize) {
        int w_h = 0;
        switch (imageSize) {
            case SMALL:
                w_h = ViewsUtil.dp2px(m_vContext, R.dimen.item_library_song_art_size);
                break;

            case MEDIUM:
                WindowManager windowManager = (WindowManager) m_vContext.getSystemService(Context.WINDOW_SERVICE);
                Point windowSize = new Point();
                windowManager.getDefaultDisplay().getSize(windowSize);
                w_h = (windowSize.x / 3) - (ViewsUtil.dp2px(m_vContext, R.dimen.item_library_song_padding) * 2);
                break;
        }

        return new Size(w_h, w_h);
    }

    public void setDimension(int width, int height) {
        this.m_vWidth = (int)(width * m_vScaleFactor);
        this.m_vHeight = (int)(height * m_vScaleFactor);
    }

    public void clear() {
        this.m_vCachedData.clear();
    }

    public static CacheHelper createInstance(Context context) {
        m_vInstance = new CacheHelper(context);
        return m_vInstance;
    }

    public static CacheHelper getInstance() {
        return m_vInstance;
    }
}
