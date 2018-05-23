package com.mobiwarez.laki.sville.ui.comments

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mobiwarez.data.comments.db.model.CommentModel
import com.mobiwarez.laki.sville.models.CommentsViewModel
import com.mobiwarez.laki.sville.recyclerUtils.AdapterConstants
import com.mobiwarez.laki.sville.recyclerUtils.LoadingBuzzDelegateAdapter
import com.mobiwarez.laki.sville.recyclerUtils.ViewType
import com.mobiwarez.laki.sville.recyclerUtils.ViewTypeDelegateAdapter
import java.util.*

class CommentAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var items: ArrayList<ViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val loadingItem = object : ViewType {
        override fun getViewType() = AdapterConstants.LOADING
    }

    init {
        delegateAdapters.put(AdapterConstants.LOADING, LoadingBuzzDelegateAdapter())
        delegateAdapters.put(AdapterConstants.COMMENT, CommentDelegateAdapter())
        items = ArrayList()
        items.add(loadingItem)
    }



    private var comments: MutableList<CommentModel> = LinkedList()


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder = delegateAdapters.get(viewType).onCreateViewHolder(parent!!)


    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int) = items[position].getViewType()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder!!, items[position])
    }


    fun addItems(news: List<CommentsViewModel>) {
        // first remove loading and notify
        val initPosition = items.size - 1
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)

        // insert news and the loading at the end of the list
        items.addAll(news)
        items.add(loadingItem)
        notifyItemRangeChanged(initPosition, items.size + 1 /* plus loading item */)
    }

    private fun getLastPosition() = if (items.lastIndex == -1) 0 else items.lastIndex


}