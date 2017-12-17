package com.mobiwarez.laki.seapp.ui.toys.receivedToys

import com.mobiwarez.data.receivedToys.db.model.ReceivedToyModel
import com.mobiwarez.laki.seapp.BasePresenter

/**
 * Created by Laki on 04/11/2017.
 */
interface ReceivedToysContract {
    interface View {
        fun showReceivedToys(items: List<ReceivedToyModel>)

        fun showMessages(message: String)
    }

    interface Presenter: BasePresenter {

        fun fetchReceivedToys()

    }
}