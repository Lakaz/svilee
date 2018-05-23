package com.mobiwarez.laki.sville.extensions

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mobiwarez.laki.sville.R

/**
 * Created by Laki on 01/11/2017.
 */

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun ImageView.loadImg(imageUrl: String) {
    if (TextUtils.isEmpty(imageUrl)) {
        //Glide.with(context).load(R.mipmap.ic_launcher).into(this)
    } else {
        Glide.with(context).load(imageUrl).into(this)
    }
}

fun ImageView.loadAvatar(imageUrl: String) {
    if (TextUtils.isEmpty(imageUrl) || imageUrl == "no_image") {
        Glide.with(context).load(R.drawable.ic_account_circle_black_36dp).into(this)
    } else {
        Glide.with(context).load(imageUrl).into(this)
    }

}
