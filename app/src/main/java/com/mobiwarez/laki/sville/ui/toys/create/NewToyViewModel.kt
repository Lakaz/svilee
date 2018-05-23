package com.mobiwarez.laki.sville.ui.toys.create

import android.arch.lifecycle.ViewModel
import android.location.Location

/**
 * Created by Laki on 26/10/2017.
 */

class NewToyViewModel : ViewModel() {

    var category: String? = null
    var ageGroup: String? = null
    var pictureUrl: String? = null
    var address: String? = null
    var description: String? = null
    var title: String? = null
    var pickUpTime: String? = null
    var pickUpLocation: String? = null
    var location: Location? = null
}
