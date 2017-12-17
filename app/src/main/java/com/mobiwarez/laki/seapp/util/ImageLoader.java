package com.mobiwarez.laki.seapp.util;

import android.support.annotation.DrawableRes;
import android.widget.ImageView;

public interface ImageLoader {

    void loadImage(String url, ImageView target, @DrawableRes int placeholderDrawable, @DrawableRes int errorDrawable);

    void loadImage(String url, ImageView target);
}
