package com.mobiwarez.laki.sville.models

data class CommentViewModel(val commentId: String,
                   val commentorName: String,
                   val commentedName: String,
                   val commentorAvatorUrl: String,
                   val commentedAvatarUrl: String,
                   val commentedUUID: String,
                   val comment: String) {
}