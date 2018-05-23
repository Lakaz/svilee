package com.mobiwarez.laki.sville.ui.toys.sharedtoys

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.mobiwarez.data.givenToys.db.model.GivenToyModel
import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.extensions.loadImg
import com.mobiwarez.laki.sville.util.ImageLoader
import com.mobiwarez.laki.sville.util.ImageLoaderImpl

import java.util.LinkedList
import android.support.graphics.drawable.VectorDrawableCompat
import android.graphics.drawable.Drawable
import android.support.v7.content.res.AppCompatResources
import android.os.Build
import com.mobiwarez.laki.sville.extensions.getFriendlyTime
import java.text.SimpleDateFormat


/**
 * Created by Laki on 27/10/2017.
 */

class SharedToysAdapter(val context: Context, private val editToy: EditToy) : RecyclerView.Adapter<SharedToysAdapter.SharedToysViewHolder>() {

    private var givenToyModels: MutableList<GivenToyModel> = LinkedList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedToysViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_shared_toy, parent, false)
        return SharedToysViewHolder(itemView, context, editToy )

    }

    override fun onBindViewHolder(holder: SharedToysViewHolder, position: Int) {
        holder.setItem(givenToyModels[position])
    }

    override fun getItemCount(): Int = givenToyModels.size

    fun onToysUpdate(viewModels: List<GivenToyModel>) {
        this.givenToyModels = viewModels.toMutableList()
        notifyDataSetChanged()
    }

    fun updateItem(toyModel: GivenToyModel, position: Int) {
        this.givenToyModels[position] = toyModel
        notifyItemChanged(position)
    }

    fun deleteItem(pos: Int) {
        givenToyModels.removeAt(pos)
        notifyItemRemoved(pos)
    }

    interface EditToy {
        fun editAgeGroup(givenToyModel: GivenToyModel, position: Int)
        fun editCategory(givenToyModel: GivenToyModel, position: Int)
        fun editDescription(givenToyModel: GivenToyModel, position: Int)
        fun editImage(givenToyModel: GivenToyModel, position: Int)
        fun updateModel(givenToyModel: GivenToyModel, position: Int)
        fun deleteModel(givenToyModel: GivenToyModel, position: Int)
        fun editTitle(givenToyModel: GivenToyModel, position: Int)
        fun editPickLocation(givenToyModel: GivenToyModel, position: Int)
        fun editPickTime(givenToyModel: GivenToyModel, position: Int)
        fun showFullImage(imageUrl: String)
        fun checkItemStatus(givenToyModel: GivenToyModel, position: Int)
    }


    class SharedToysViewHolder(itemView: View, val context: Context, private val editToy: EditToy) : RecyclerView.ViewHolder(itemView) {

        private var constraintLayout: ConstraintLayout = itemView.findViewById(R.id.container)

        private var categoryText: TextView = itemView.findViewById(R.id.shared_categoryText)
        private var ageGroupText: TextView = itemView.findViewById(R.id.shared_ageGroupText)
        private var itemDescription: TextView = itemView.findViewById(R.id.shared_descriptionText)
        private var itemTitle: TextView = itemView.findViewById(R.id.shared_title)
        private var itemPickLocation: TextView = itemView.findViewById(R.id.shared_pickTime)
        private var itemPickTime: TextView = itemView.findViewById(R.id.shared_pickLocation)

        private var toyImage: ImageView = itemView.findViewById(R.id.shared_toy_imageview)
        private var editImage: ImageView = itemView.findViewById(R.id.shared_image_edit)
        private var deleteImage: ImageView = itemView.findViewById(R.id.shared_deleteButton)
        private var updateImage: ImageView = itemView.findViewById(R.id.shared_updateupdate_button)
        private var status: TextView = itemView.findViewById(R.id.shared_statusText)

        private var timeRecevedTv: TextView = itemView.findViewById(R.id.time_taken)

        private var toyViewModel: GivenToyModel? = null


        fun setItem(toyViewModel: GivenToyModel) {

            itemTitle.text = toyViewModel.itemTitle


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val rightDrawable = AppCompatResources
                        .getDrawable(context, R.drawable.ic_edit_accent_24dp)
                itemTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,rightDrawable, null)
                itemDescription.setCompoundDrawablesWithIntrinsicBounds(null, null,rightDrawable, null)
                itemPickLocation.setCompoundDrawablesWithIntrinsicBounds(null, null,rightDrawable, null)
                itemPickTime.setCompoundDrawablesWithIntrinsicBounds(null, null,rightDrawable, null)
                categoryText.setCompoundDrawablesWithIntrinsicBounds(null, null,rightDrawable, null)
                ageGroupText.setCompoundDrawablesWithIntrinsicBounds(null, null,rightDrawable, null)
            } else {
                //Safely create our VectorDrawable on pre-L android versions.
                val leftDrawable = VectorDrawableCompat
                        .create(context.resources, R.drawable.ic_edit_accent_24dp, null)
                itemTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, leftDrawable, null)
                itemDescription.setCompoundDrawablesWithIntrinsicBounds(null, null,leftDrawable, null)
                itemPickLocation.setCompoundDrawablesWithIntrinsicBounds(null, null,leftDrawable, null)
                itemPickTime.setCompoundDrawablesWithIntrinsicBounds(null, null,leftDrawable, null)
                categoryText.setCompoundDrawablesWithIntrinsicBounds(null, null,leftDrawable, null)
                ageGroupText.setCompoundDrawablesWithIntrinsicBounds(null, null,leftDrawable, null)

            }

            if (!TextUtils.isEmpty(toyViewModel.toyImagePath) && toyViewModel.toyImagePath != "no_image") {
                toyImage.loadImg(toyViewModel.toyImagePath)
            } else {
                //toyImage.visibility = View.GONE
            }

            if (!TextUtils.isEmpty(toyViewModel.pickUpLocation) && toyViewModel.pickUpLocation != "no pickup location") {
                itemPickLocation.text = toyViewModel.pickUpLocation
            }

            if (!TextUtils.isEmpty(toyViewModel.pickUpTime) && toyViewModel.pickUpTime != "no pickup time") {
                itemPickTime.text = toyViewModel.pickUpTime
            }

            if (toyViewModel.isToySynced == 0) constraintLayout.setBackgroundColor(context.resources?.getColor(R.color.darkGrey)!!)

            this.toyViewModel = toyViewModel



            val age = toyViewModel.toyAgeGroup

            when (age) {
                "baby" -> "Baby: 0 to 2 years"
                "toddler" -> "Toddler: 3 to 5 years"
                "kid" -> "Kid: 5 years+"
            }


            if(!TextUtils.isEmpty(toyViewModel.timeReceived)){
                val time = SimpleDateFormat("yyyyMMdd_HHmmss").parse(toyViewModel.timeReceived).time
                timeRecevedTv.text = "Taken: "+time.getFriendlyTime()

            }


            categoryText.text = toyViewModel.toyCategory
            ageGroupText.text = age

            if(toyViewModel.toyDescription != "no description") itemDescription.text = toyViewModel.toyDescription

            updateImage.setOnClickListener { _ -> editToy.updateModel(toyViewModel, this.adapterPosition) }
            deleteImage.setOnClickListener { _ -> editToy.deleteModel(toyViewModel, this.adapterPosition) }
            editImage.setOnClickListener { _ -> editToy.editImage(toyViewModel, this.adapterPosition) }

            if (toyViewModel.isToyGiven == 0 && toyViewModel.isToySynced == 1) {
                status.text = "Available, Checking ..."
                editToy.checkItemStatus(toyViewModel, this.adapterPosition)
            } else if(toyViewModel.isToyGiven == 1) {
                status.text = "Taken by: "+toyViewModel.receiverName
            }else {
                status.text = "Uploading..."
            }

            itemPickTime.setOnClickListener { _ -> editToy.editPickLocation(toyViewModel, this.adapterPosition) }
            itemTitle.setOnClickListener { _ -> editToy.editTitle(toyViewModel, this.adapterPosition) }
            itemDescription.setOnClickListener { _ -> editToy.editDescription(toyViewModel, this.adapterPosition) }
            itemPickLocation.setOnClickListener { _ -> editToy.editPickLocation(toyViewModel, this.adapterPosition) }
            categoryText.setOnClickListener { _ -> editToy.editCategory(toyViewModel, this.adapterPosition) }
            ageGroupText.setOnClickListener { _ -> editToy.editAgeGroup(toyViewModel, this.adapterPosition) }

            toyImage.setOnClickListener { _ ->  if (!TextUtils.isEmpty(toyViewModel.toyImagePath) && toyViewModel.toyImagePath != "no_image") editToy.showFullImage(toyViewModel.toyImagePath)}

        }
    }

}
