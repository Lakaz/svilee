package com.mobiwarez.domain.domain.models

/**
 * Created by Laki on 19/10/2017.
 */
data class Toy (
        val toyName: String,
        val toyDescription: String,
        val toyCategory: String?,
        val toyImagePath: String,
        val status: Int,
        val timePosted: String
)