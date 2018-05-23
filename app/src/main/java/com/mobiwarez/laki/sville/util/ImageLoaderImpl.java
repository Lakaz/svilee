package com.mobiwarez.laki.sville.util;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public final class ImageLoaderImpl implements ImageLoader {

    private final Context context;

    public ImageLoaderImpl(final Context context) {
        this.context = context;
    }

    @Override
    public void loadImage(final String url, final ImageView target, @DrawableRes final int placeholderDrawable, @DrawableRes final int errorDrawable) {
        Glide.with(context)
             .load(url)
             .into(target);
    }

    @Override
    public void loadImage(String url, ImageView target) {
        Glide.with(context)
                .load(url)
                .into(target);
    }
}
