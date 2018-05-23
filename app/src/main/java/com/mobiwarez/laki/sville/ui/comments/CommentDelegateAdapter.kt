package com.mobiwarez.laki.sville.ui.comments

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.extensions.getFriendlyTime
import com.mobiwarez.laki.sville.extensions.inflate
import com.mobiwarez.laki.sville.extensions.loadAvatar
import com.mobiwarez.laki.sville.extensions.loadImg
import com.mobiwarez.laki.sville.models.CommentsViewModel
import com.mobiwarez.laki.sville.recyclerUtils.ViewType
import com.mobiwarez.laki.sville.recyclerUtils.ViewTypeDelegateAdapter
import kotlinx.android.synthetic.main.list_item_comment.view.*
import java.text.SimpleDateFormat


class CommentDelegateAdapter: ViewTypeDelegateAdapter {


    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return CommentViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as CommentDelegateAdapter.CommentViewHolder
        holder.bind(item as CommentsViewModel)

    }


    inner class CommentViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.list_item_comment)) {

        fun bind(commentsViewModel: CommentsViewModel) {
            itemView.commentor_name.text = commentsViewModel.commentorName
            itemView.comment_text_tv.text = commentsViewModel.comment
            itemView.commentor_image.loadAvatar(commentsViewModel.commentedAvatarUrl)

            val time = SimpleDateFormat("yyyyMMdd_HHmmss").parse(commentsViewModel.timePosted).time


            itemView.comment_time_tv.text = "commented "+time.getFriendlyTime()
        }

    }
}