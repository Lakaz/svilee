package com.mobiwarez.laki.seapp.ui.toys.sharedtoys

import com.mobiwarez.laki.seapp.ui.models.ToyViewModel

/**
 * Created by Laki on 27/11/2017.
 */
class StatusResponse (
    val isTaken: Boolean,
    var takerName: String? = null,
    var takerUUID: String? = null,
    var timeTaken: String? = null
)