package com.mobiwarez.laki.sville.ui.starter

import com.mobiwarez.laki.sville.base.BaseView
import com.mobiwarez.laki.sville.base.ScopedPresenter
import com.mobiwarez.laki.sville.ui.models.ToyViewModel

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