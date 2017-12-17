package com.mobiwarez.laki.seapp.ui.starter

import com.mobiwarez.laki.seapp.base.BaseView
import com.mobiwarez.laki.seapp.base.ScopedPresenter
import com.mobiwarez.laki.seapp.ui.models.ToyViewModel

/**
 * Created by Laki on 25/10/2017.
 */
interface StarterContract {

    interface View: BaseView


    interface Presenter: ScopedPresenter {

        fun startGiving(feedId: Int)

        fun startReceiving()

    }

}