package com.my.samplemusicplayertest.theme.interfaces;

public interface PaletteStateListener {
    void onUpdateVibrantColor(int vibrantColor);
    void onUpdateVibrantDarkColor(int vibrantDarkColor);
    void onUpdateVibrantLightColor(int vibrantLightColor);

    void onUpdateMutedColor(int mutedColor);

    void onUpdateMutedDarkColor(int mutedDarkColor);
}
