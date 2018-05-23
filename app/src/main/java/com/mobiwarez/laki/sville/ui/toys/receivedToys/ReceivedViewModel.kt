package com.mobiwarez.laki.sville.ui.toys.receivedToys

import android.arch.lifecycle.ViewModel
import android.graphics.drawable.Drawable
import com.mobiwarez.data.comments.db.model.CommentModel
import com.mobiwarez.data.receivedToys.db.model.ReceivedToyModel

class ReceivedViewModel: ViewModel() {

    var receivedToyModel: ReceivedToyModel? = null
    var commentModel: CommentModel? = null
    var selectedDrawable: Drawable? = null
}