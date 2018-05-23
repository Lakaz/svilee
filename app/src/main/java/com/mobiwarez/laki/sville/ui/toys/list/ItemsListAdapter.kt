package com.mobiwarez.laki.sville.ui.toys.list

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mobiwarez.laki.sville.recyclerUtils.AdapterConstants
import com.mobiwarez.laki.sville.recyclerUtils.LoadingBuzzDelegateAdapter
import com.mobiwarez.laki.sville.recyclerUtils.ViewType
import com.mobiwarez.laki.sville.recyclerUtils.ViewTypeDelegateAdapter
import com.mobiwarez.laki.sville.ui.models.ToyViewModel

/**
 * Created by Laki on 02/11/2017.
 */
class ItemsListAdapter(listener: ItemDelegateAdapter.onViewSelectedListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var items: ArrayList<ViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val loadingItem = object : ViewType {
        override fun getViewType() = AdapterConstants.LOADING
    }

    init {
        delegateAdapters.put(AdapterConstants.LOADING, LoadingBuzzDelegateAdapter())
        delegateAdapters.put(AdapterConstants.ITEM, ItemDelegateAdapter(listener))
        items = ArrayList()
        items.add(loadingItem)
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder = delegateAdapters.get(viewType).onCreateViewHolder(parent!!)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder!!, items[position])
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()

    fun addItems(news: List<ToyViewModel>) {
        // first remove loading and notify
        val initPosition = items.size - 1
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)

        // insert news and the loading at the end of the list
        items.addAll(news)
        items.add(loadingItem)
        notifyItemRangeChanged(initPosition, items.size + 1 /* plus loading item */)
    }

    fun addItems(news: List<ToyViewModel>, hasmore: Boolean) {
        // first remove loading and notify
        val initPosition = items.size - 1
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)

        // insert news and the loading at the end of the list
        items.addAll(news)
        if (hasmore) {
            items.add(loadingItem)
            notifyItemRangeChanged(initPosition, items.size + 1 /* plus loading item */)
        }

        else {
            notifyItemRangeChanged(initPosition, items.size /* plus loading item */)
        }
    }


    fun clearAndAddItems(news: List<ToyViewModel>, hamore: Boolean) {
        items.clear()
        notifyItemRangeRemoved(0, getLastPosition())

        items.addAll(news)
        if (hamore) {
            items.add(loadingItem)
            //notifyItemRangeInserted(0, items.size + 1)
        }
        else {
            //notifyItemRangeInserted(0, items.size)
        }
    }



    fun clearAndAddItems(news: List<ToyViewModel>) {
        items.clear()
        notifyItemRangeRemoved(0, getLastPosition())

        items.addAll(news)
        items.add(loadingItem)
        notifyItemRangeInserted(0, items.size)
    }

    fun removeLoadingItem() {
        val lastPosition = items.size - 1
        items.removeAt(lastPosition)
        notifyItemRemoved(lastPosition)
    }

    fun getItems(): List<ToyViewModel> =
            items
                    .filter { it.getViewType() == AdapterConstants.ITEM }
                    .map { it as ToyViewModel }


    private fun getLastPosition() = if (items.lastIndex == -1) 0 else items.lastIndex

}