package com.mobiwarez.laki.sville.models

import com.mobiwarez.laki.sville.recyclerUtils.AdapterConstants
import com.mobiwarez.laki.sville.recyclerUtils.ViewType

data class CommentsViewModel(
        val commentId: String,
        val commentorName: String,
        val commentedName: String,
        val commentorAvatorUrl: String,
        val commentedAvatarUrl: String,
        val commentedUUID: String,
        val comment: String,
        val timePosted: String
):ViewType {
    override fun getViewType(): Int = AdapterConstants.COMMENT
}