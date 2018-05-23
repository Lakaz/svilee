package com.mobiwarez.laki.sville.ui.toys.receivedToys

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mobiwarez.data.receivedToys.db.model.ReceivedToyModel
import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.extensions.loadAvatar
import com.mobiwarez.laki.sville.extensions.loadImg
import java.util.*

/**
 * Created by Laki on 11/11/2017.
 */

class ReceivedToysAdapter(private val listener: ReceivedItemListener) : RecyclerView.Adapter<ReceivedToysAdapter.ReceivedToysViewHolder>() {

    private var receivedToyModelList: MutableList<ReceivedToyModel> = LinkedList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedToysViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_received, parent, false)
        return ReceivedToysAdapter.ReceivedToysViewHolder(itemView, listener)

    }

    override fun onBindViewHolder(holder: ReceivedToysViewHolder, position: Int) {
        holder.setItem(receivedToyModelList!![position])
    }

    override fun getItemCount(): Int = receivedToyModelList!!.size

    fun onItemsUpdate(viewModels: List<ReceivedToyModel>) {
        this.receivedToyModelList = viewModels.toMutableList()
        notifyDataSetChanged()
    }


    interface ReceivedItemListener{
        fun startChat(model: ReceivedToyModel)

        fun unClaim(model: ReceivedToyModel)

        fun showFullImage(imageUrl: String)

        fun reviewGiver(model: ReceivedToyModel, imageView: ImageView)
    }

    class ReceivedToysViewHolder(itemView: View, private var listener: ReceivedItemListener) : RecyclerView.ViewHolder(itemView) {

        private var titleTextView: TextView = itemView.findViewById(R.id.itemDescription)
        private val pickUpLoactionTextView: TextView = itemView.findViewById(R.id.pick_up_location)
        private val pickUpTimeTextView: TextView = itemView.findViewById(R.id.pick_up_time)
        private val giverNameTextView: TextView = itemView.findViewById(R.id.giver_name)
        private val itemImageView: ImageView = itemView.findViewById(R.id.received_image)
        private val chatImageView: ImageView = itemView.findViewById(R.id.chatImage)
        private val giverImage: ImageView = itemView.findViewById(R.id.avatar)
        private val reviewIcon: ImageView = itemView.findViewById(R.id.review_image)


        fun setItem(model: ReceivedToyModel) {
            titleTextView.text = model.toyName
            if (model.toyPickUpLocation != "no pickup location")pickUpLoactionTextView.text = "Pick up location: "+model.toyPickUpLocation
            if (model.toyPickUpTime != "no pickup time")pickUpTimeTextView.text = "Pick up time: "+model.toyPickUpTime
            giverNameTextView.text = model.giverName
            itemImageView.loadImg(model.toyUrl)
            chatImageView.setOnClickListener{ _ -> listener.startChat(model)}
            giverImage.loadAvatar(model.avatarUrl)
            reviewIcon.setOnClickListener{ _ -> listener.reviewGiver(model, giverImage)}

            itemImageView.setOnClickListener { _ -> listener.showFullImage(model.toyUrl) }
        }
    }
}
