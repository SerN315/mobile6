package com.my.samplemusicplayertest.theme;

import android.animation.ArgbEvaluator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.palette.graphics.Palette;

import com.my.samplemusicplayertest.theme.interfaces.PaletteStateListener;

import java.util.HashMap;

public class AsyncPaletteBuilder {

    public interface Action {
        void onValueAnimated(int value);
    }

    private final int ANIM_DURATION = 650;

    private enum Vibrant_Type {
        VIBRANT, VIBRANT_DARK, VIBRANT_LIGHT, MUTED, MUTED_DARK
    }


    private HashMap<Vibrant_Type, ValueAnimator> mAnimators = new HashMap<>();
    private HashMap<Vibrant_Type, Integer> mDefColors = new HashMap<>();
    private HashMap<Vibrant_Type, Integer> mPrevColors = new HashMap<>();


    private final PaletteStateListener mStateListener;

    public AsyncPaletteBuilder(PaletteStateListener stateListener) {
        this.mStateListener = stateListener;

        this.mDefColors.put(Vibrant_Type.VIBRANT, Color.parseColor("#424242"));
        this.mPrevColors.put(Vibrant_Type.VIBRANT, this.mDefColors.get(Vibrant_Type.VIBRANT));

        this.mDefColors.put(Vibrant_Type.VIBRANT_DARK, Color.parseColor("#424242"));
        this.mPrevColors.put(Vibrant_Type.VIBRANT_DARK, this.mDefColors.get(Vibrant_Type.VIBRANT_DARK));

        this.mDefColors.put(Vibrant_Type.MUTED, Color.parseColor("#424242"));
        this.mPrevColors.put(Vibrant_Type.MUTED, this.mDefColors.get(Vibrant_Type.MUTED));

        this.mDefColors.put(Vibrant_Type.MUTED_DARK, Color.parseColor("#424242"));
        this.mPrevColors.put(Vibrant_Type.MUTED_DARK, this.mDefColors.get(Vibrant_Type.MUTED_DARK));
    }

    private ValueAnimator getColorAnimator(int fromColor, int toColor) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(
                (TypeEvaluator) new ArgbEvaluator(),
                new Object[] {Integer.valueOf(fromColor), Integer.valueOf(toColor)});
        valueAnimator.setDuration(ANIM_DURATION);
        return valueAnimator;
    }

    private void onStartColorAnimation(Vibrant_Type type, Action action, int colorFrom, int colorTo) {
        ValueAnimator animator;
        if (this.mAnimators.containsKey(type) && this.mAnimators.get(type) != null) {
            animator = this.mAnimators.get(type);
            animator.end();
            animator.removeAllUpdateListeners();
            animator.removeAllListeners();
        }

        animator = getColorAnimator(colorFrom, colorTo);

        this.mAnimators.put(type, animator);

        animator.addUpdateListener(valueAnimator -> {
            action.onValueAnimated((int)valueAnimator.getAnimatedValue());
        });

        animator.start();
    }

    public void onStartAnimation(Bitmap art) {
        if (art != null) {
            Palette.from(art).generate(palette -> {
                onStartColorAnimation(Vibrant_Type.VIBRANT, value -> {
                    AsyncPaletteBuilder.this.mStateListener.onUpdateVibrantColor(value);
                    AsyncPaletteBuilder.this.mPrevColors.put(Vibrant_Type.VIBRANT, value);
                }, this.mPrevColors.get(Vibrant_Type.VIBRANT), palette.getVibrantColor(mDefColors.get(Vibrant_Type.VIBRANT)));

                onStartColorAnimation(Vibrant_Type.VIBRANT_DARK, value -> {
                    AsyncPaletteBuilder.this.mStateListener.onUpdateVibrantDarkColor(value);
                    AsyncPaletteBuilder.this.mPrevColors.put(Vibrant_Type.VIBRANT_DARK, value);
                }, this.mPrevColors.get(Vibrant_Type.VIBRANT_DARK), palette.getDarkVibrantColor(mDefColors.get(Vibrant_Type.VIBRANT_DARK)));

                onStartColorAnimation(Vibrant_Type.MUTED, value -> {
                    AsyncPaletteBuilder.this.mStateListener.onUpdateMutedColor(value);
                    AsyncPaletteBuilder.this.mPrevColors.put(Vibrant_Type.MUTED, value);
                }, this.mPrevColors.get(Vibrant_Type.MUTED), palette.getMutedColor(mDefColors.get(Vibrant_Type.MUTED)));

                onStartColorAnimation(Vibrant_Type.MUTED_DARK, value -> {
                    AsyncPaletteBuilder.this.mStateListener.onUpdateMutedDarkColor(value);
                    AsyncPaletteBuilder.this.mPrevColors.put(Vibrant_Type.MUTED_DARK, value);
                }, this.mPrevColors.get(Vibrant_Type.MUTED_DARK), palette.getDarkMutedColor(mDefColors.get(Vibrant_Type.MUTED_DARK)));
            });
        }
        else {
            onStartColorAnimation(Vibrant_Type.VIBRANT, value -> {
                AsyncPaletteBuilder.this.mStateListener.onUpdateVibrantColor(value);
                AsyncPaletteBuilder.this.mPrevColors.put(Vibrant_Type.VIBRANT, value);
            }, this.mPrevColors.get(Vibrant_Type.VIBRANT), this.mDefColors.get(Vibrant_Type.VIBRANT));

            onStartColorAnimation(Vibrant_Type.VIBRANT_DARK, value -> {
                AsyncPaletteBuilder.this.mStateListener.onUpdateVibrantDarkColor(value);
                AsyncPaletteBuilder.this.mPrevColors.put(Vibrant_Type.VIBRANT_DARK, value);
            }, this.mPrevColors.get(Vibrant_Type.VIBRANT_DARK), mDefColors.get(Vibrant_Type.VIBRANT_DARK));

            onStartColorAnimation(Vibrant_Type.MUTED, value -> {
                AsyncPaletteBuilder.this.mStateListener.onUpdateMutedColor(value);
                AsyncPaletteBuilder.this.mPrevColors.put(Vibrant_Type.MUTED, value);
            }, this.mPrevColors.get(Vibrant_Type.MUTED), mDefColors.get(Vibrant_Type.MUTED));

            onStartColorAnimation(Vibrant_Type.MUTED_DARK, value -> {
                AsyncPaletteBuilder.this.mStateListener.onUpdateMutedDarkColor(value);
                AsyncPaletteBuilder.this.mPrevColors.put(Vibrant_Type.MUTED_DARK, value);
            }, this.mPrevColors.get(Vibrant_Type.MUTED_DARK), mDefColors.get(Vibrant_Type.MUTED_DARK));
        }
    }
}
