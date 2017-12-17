package com.mobiwarez.laki.seapp.ui.models

import android.location.Location
import com.mobiwarez.laki.seapp.recyclerUtils.AdapterConstants
import com.mobiwarez.laki.seapp.recyclerUtils.ViewType

/**
 * Created by Laki on 21/10/2017.
 */
data class ToyViewModel (
        val toyId: String,
        val toyname: String?,
        val toyDescription: String,
        val toyCategory: String,
        val toyAgeGroup: String,
        val toyImageUrl: String?,
        val toyStatus: String,
        val timePosted: String,
        val giverName: String,
        val toyLocation: String?,
        val avatarUrl: String?,
        val latitude: Float,
        val longitude: Float,
        val pickLocation: String?,
        val pickTime: String?,
        val giverUUID: String,
        val viewedLocation: Location
): ViewType {
    override fun getViewType() = AdapterConstants.ITEM
}