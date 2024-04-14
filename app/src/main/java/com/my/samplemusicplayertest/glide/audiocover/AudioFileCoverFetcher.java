package com.my.samplemusicplayertest.glide.audiocover;

import android.media.MediaMetadataRetriever;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AudioFileCoverFetcher implements DataFetcher<InputStream> {

    private final AudioFileCover m_vModel;
    private InputStream m_vStream;

    public AudioFileCoverFetcher(AudioFileCover model) {
        this.m_vModel = model;
        this.m_vStream = null;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(this.m_vModel.getFilePath());
            byte[] data = retriever.getEmbeddedPicture();

            if (data != null && data.length > 0)
                this.m_vStream = new ByteArrayInputStream(data);

            callback.onDataReady(this.m_vStream);
        }
        catch (Exception ex) {
            callback.onLoadFailed(ex);
        }
        finally {
            try {
                retriever.release();
            }
            catch (IOException ignore) { }
        }
    }

    @Override
    public void cleanup() {
        if (this.m_vStream != null) {
            try {
                this.m_vStream.close();
            }
            catch (Exception ignore) { }
        }
    }

    @Override
    public void cancel() {

    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }
}
