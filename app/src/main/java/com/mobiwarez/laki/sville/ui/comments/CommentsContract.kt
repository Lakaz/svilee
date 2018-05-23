package com.mobiwarez.laki.sville.ui.comments

interface CommentsContract {

    interface View {
        fun showProfile()

        fun showComments()
    }

    interface Presenter {
        fun loadComments()

        fun addComment()
    }

}