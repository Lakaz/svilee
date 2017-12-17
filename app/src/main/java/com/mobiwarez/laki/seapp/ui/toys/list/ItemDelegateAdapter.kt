package com.mobiwarez.laki.seapp.ui.toys.list

import android.location.Location
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.mobiwarez.laki.seapp.R
import com.mobiwarez.laki.seapp.extensions.*
import com.mobiwarez.laki.seapp.recyclerUtils.ViewType
import com.mobiwarez.laki.seapp.recyclerUtils.ViewTypeDelegateAdapter
import com.mobiwarez.laki.seapp.ui.models.ToyViewModel
import kotlinx.android.synthetic.main.list_toy_item.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Laki on 01/11/2017.
 */
class ItemDelegateAdapter (val viewActions: onViewSelectedListener): ViewTypeDelegateAdapter {

    interface onViewSelectedListener {
        fun receiveItem(model: ToyViewModel)
        fun showFullImage(imageUrl: String)
    }


    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as ItemViewHolder
        holder.bind(item as ToyViewModel)
    }


    inner class ItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
            parent.inflate(R.layout.list_toy_item)) {

        fun bind(toyViewModel: ToyViewModel){

            val itemLocation = Location("")
            itemLocation.longitude = toyViewModel.longitude.toDouble()
            itemLocation.latitude = toyViewModel.latitude.toDouble()

            val dist = itemLocation.distanceTo(toyViewModel.viewedLocation)


            val time = SimpleDateFormat("yyyyMMdd_HHmmss").parse(toyViewModel.timePosted).time

            val recent = time.getRecent()

            if (recent) itemView.upload_status.visibility = View.VISIBLE

            itemView.toy_description.text = toyViewModel.toyname
            itemView.toygiver_name.text = toyViewModel.giverName
            if (toyViewModel.toyLocation != "no place") itemView.toy_location.text = toyViewModel.toyLocation
            if (toyViewModel.toyDescription != "no description")itemView.item_description.text = toyViewModel.toyDescription
            itemView.time_posted.text = time.getFriendlyTime()
            itemView.toy_image.loadImg(toyViewModel.toyImageUrl!!)
            itemView.user_avatar.loadAvatar(toyViewModel.avatarUrl!!)
            itemView.distance_tv.text = dist.toString()+" km away"

            itemView.receive_btn.setOnClickListener{viewActions.receiveItem(toyViewModel)}
            itemView.toy_image.setOnClickListener { _ -> if (!TextUtils.isEmpty(toyViewModel.toyImageUrl) && toyViewModel.toyImageUrl != "no_image") viewActions.showFullImage(toyViewModel.toyImageUrl)}
        }

    }

}