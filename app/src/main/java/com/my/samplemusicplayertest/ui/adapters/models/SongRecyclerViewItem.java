package com.my.samplemusicplayertest.ui.adapters.models;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Size;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.my.samplemusicplayertest.glide.audiocover.AudioFileCover;
import com.my.samplemusicplayertest.ui.adapters.helpers.CacheHelper;
import com.my.mediaplayer.model.Song;

public class SongRecyclerViewItem extends BaseRecyclerViewItem {

    private Song m_vItem;

    public SongRecyclerViewItem(Song song) {
        super(song == null ? "" : song.getTitle(), ItemType.SONG);

        this.m_vItem = song;
    }

    public String getFilePath() {
        return this.m_vItem.getData();
    }

    public Song getSong() {
        return this.m_vItem;
    }

    @Override
    public void onCache(Context context) {
        if (CacheHelper.getInstance() == null)
            CacheHelper.createInstance(context);

        if (CacheHelper.getInstance().isCached(getHashCode()))
            return;

        Size small = CacheHelper.getInstance().getSize(CacheHelper.IMAGE_SIZE.SMALL);
        Size medium = CacheHelper.getInstance().getSize(CacheHelper.IMAGE_SIZE.MEDIUM);

        new Thread(() -> {
            RequestBuilder<Drawable> builder = Glide.with(context)
                    .load(new AudioFileCover(this.getFilePath()))
                    .signature(new ObjectKey(this.getId()))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .skipMemoryCache(false)
                    .centerCrop()
                    .encodeQuality(80)
                    .set(Downsampler.DECODE_FORMAT, DecodeFormat.PREFER_RGB_565)
                    .addListener(new RequestListener<>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    });


            builder.preload(small.getWidth(), small.getHeight());
            builder.preload(medium.getWidth(), medium.getHeight());
        }).start();
        CacheHelper.getInstance().setIsCached(getHashCode(), true);
    }

    @Override
    public int getHashCode() {

        int result = String.valueOf(this.m_vItem.getId()).hashCode();
        result = 31 * result * this.m_vItem.getTitle().hashCode();
        result = 31 * result * this.m_vItem.getData().hashCode();

        return result;
    }

    @Override
    public int getId() {
        return (int)this.m_vItem.getId();
    }
}
