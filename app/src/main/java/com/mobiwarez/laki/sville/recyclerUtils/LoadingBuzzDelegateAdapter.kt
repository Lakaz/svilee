package com.mobiwarez.laki.sville.recyclerUtils

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.extensions.inflate


/**
 * Created by Laki on 01/11/2017.
 */
class LoadingBuzzDelegateAdapter: ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder  = LoadingViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
    }

    class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.loading_item))


}