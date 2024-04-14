package com.my.samplemusicplayertest.ui.adapters.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.samplemusicplayertest.ui.adapters.viewholders.BaseViewHolder;
import com.my.samplemusicplayertest.ui.adapters.viewholders.SongViewHolder;
import com.my.samplemusicplayertest.R;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;

public class BaseViewHelper {
    private final static LinkedHashMap<Class<?>, Integer> m_vLayoutIds = new LinkedHashMap<>();

    static {
        m_vLayoutIds.put(SongViewHolder.class, R.layout.item_library_song_view);
    }

    private static int getLayoutId (Class<?> viewHolder) {
        return m_vLayoutIds.getOrDefault(viewHolder, -1);
    }

    public static <T extends BaseViewHolder> T onCreateViewHolder (Class<T> viewHolder, ViewGroup parent) {
        try {
            View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewHolder), parent, false);
            Constructor<?> constructor = viewHolder.getDeclaredConstructor(View.class);
            return  (T) constructor.newInstance(view);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
