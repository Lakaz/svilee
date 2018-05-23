package com.mobiwarez.laki.sville.ui.toys.receivedToys

import com.mobiwarez.data.receivedToys.db.model.ReceivedToyModel
import com.mobiwarez.laki.sville.BasePresenter

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