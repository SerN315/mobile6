package com.my.samplemusicplayertest.glide;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.my.samplemusicplayertest.glide.audiocover.AudioFileCover;
import com.my.samplemusicplayertest.glide.audiocover.AudioFileCoverLoader;

import java.io.InputStream;

@GlideModule
public class SampleGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.prepend(
                AudioFileCover.class,
                InputStream.class,
                new AudioFileCoverLoader.Factory());
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);

        builder.setLogLevel(Log.VERBOSE);
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
