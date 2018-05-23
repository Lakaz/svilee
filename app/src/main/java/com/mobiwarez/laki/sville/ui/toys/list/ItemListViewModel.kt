package com.mobiwarez.laki.sville.ui.toys.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.drawable.Drawable
import com.mobiwarez.domain.domain.models.Toy
import com.mobiwarez.laki.sville.ui.models.ToyViewModel

/**
 * Created by Laki on 02/11/2017.
 */
class ItemListViewModel: ViewModel() {

    val toys = mutableListOf<ToyViewModel>()

    var selectedGiver: String? = null
    var selectedModel: ToyViewModel? = null

    var selectedAvatarDawable: Drawable? = null

    fun addItems(items: List<ToyViewModel>) {
        toys.addAll(items)
    }

    fun getItemsListed(): List<ToyViewModel>{
        return toys
    }

}