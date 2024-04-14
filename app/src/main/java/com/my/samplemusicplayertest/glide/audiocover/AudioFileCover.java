package com.my.samplemusicplayertest.glide.audiocover;

import androidx.annotation.Nullable;

import java.util.Objects;

public class AudioFileCover {
    private String m_vPath;

    public AudioFileCover(String path) {
        this.m_vPath = path;
    }

    public String getFilePath() {
        return this.m_vPath;
    }

    @Override
    public int hashCode() {
        return this.m_vPath.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof AudioFileCover))
            return false;

        return Objects.equals(((AudioFileCover)obj).m_vPath, this.m_vPath);
    }
}
