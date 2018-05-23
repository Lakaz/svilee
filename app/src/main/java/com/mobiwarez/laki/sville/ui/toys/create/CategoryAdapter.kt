package com.mobiwarez.laki.sville.ui.toys.create

import android.content.ClipData
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.analytics.FirebaseAnalytics
import com.mobiwarez.data.givenToys.db.model.GivenToyModel
import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.ui.toys.models.CategoryModel
import java.util.*

class CategoryAdapter(val context: Context, private val listenerDrag: dragListener) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){


    private var categories: MutableList<CategoryModel> = LinkedList()


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_category, parent, false)
        return CategoryAdapter.CategoryViewHolder(itemView, context, listenerDrag)

    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder?, position: Int) {
        holder?.bind(categories[position])
    }

    fun onItemUpdate(models: List<CategoryModel>) {
        this.categories = models.toMutableList()
        notifyDataSetChanged()
    }

    interface dragListener{
        fun setDraggeImage(image: ImageView, view: View)
    }

    class CategoryViewHolder(view: View, context: Context, private val listener: dragListener): RecyclerView.ViewHolder(view){

        private var image = view.findViewById<ImageView>(R.id.category_image)
        private var categoryName = view.findViewById<TextView>(R.id.category_name_tv)
        private var firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        fun bind(categoryModel: CategoryModel){
            image.setImageResource(categoryModel.imageId)
            categoryName.text = categoryModel.categoryName

            itemView.setOnLongClickListener { v ->

                val bundle = Bundle()
                bundle.putString("category_of_gift_offered", categoryModel.categoryName)
                firebaseAnalytics.logEvent("gift_category_dragged", bundle)
                listener.setDraggeImage(image, itemView)
                val data = ClipData.newPlainText(categoryModel.categoryName,categoryModel.categoryName)
                val shadowBuilder = MyDragShadowBuilder(image)
                v?.startDrag(data, shadowBuilder, image, 0)
                true
            }

        }


    }


    private class MyDragShadowBuilder// Defines the constructor for myDragShadowBuilder
    (v: View) : View.DragShadowBuilder(v) {

        private var shadow: Drawable

        init {

            // Creates a draggable image that will fill the Canvas provided by the system.
            shadow = v.findViewById<ImageView>(R.id.category_image).drawable
        }// Stores the View parameter passed to myDragShadowBuilder.

        // Defines a callback that sends the drag shadow dimensions and touch point back to the
        // system.
        override fun onProvideShadowMetrics(size: Point, touch: Point) {
            // Defines local variables
            val width: Int
            val height: Int

            // Sets the width of the shadow to half the width of the original View
            width = view.width * 2

            // Sets the height of the shadow to half the height of the original View
            height = view.height * 2

            // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
            // Canvas that the system will provide. As a result, the drag shadow will fill the
            // Canvas.
            shadow.setBounds(0, 0, width, height)

            //view.layoutParams.height = height
            //view.layoutParams.width = width


            // Sets the size parameter's width and height values. These get back to the system
            // through the size parameter.
            size.set(width, height)

            // Sets the touch point's position to be in the middle of the drag shadow
            touch.set(width / 2, height / 2)
        }

        // Defines a callback that draws the drag shadow in a Canvas that the system constructs
        // from the dimensions passed in onProvideShadowMetrics().
        override fun onDrawShadow(canvas: Canvas) {

            // Draws the ColorDrawable in the Canvas passed in from the system.
            shadow.draw(canvas)
            //view.draw(canvas)
        }

    }

}