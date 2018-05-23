package com.mobiwarez.laki.sville.models

/**
 * Created by Laki on 01/11/2017.
 */
data class ItemViewModel (
        val itemId: String,
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
        val longitude: Float

)