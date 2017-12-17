package com.mobiwarez.laki.seapp.ui.toys.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mobiwarez.domain.domain.models.Toy
import com.mobiwarez.laki.seapp.ui.models.ToyViewModel

/**
 * Created by Laki on 02/11/2017.
 */
class ItemListViewModel: ViewModel() {

    val toys = mutableListOf<ToyViewModel>()

    fun addItems(items: List<ToyViewModel>) {
        toys.addAll(items)
    }

    fun getItemsListed(): List<ToyViewModel>{
        return toys
    }

}