package com.mobiwarez.laki.sville.fragments

import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Laki on 01/11/2017.
 */

open class RxBaseFragment : Fragment() {

    protected var subscriptions = CompositeDisposable()

    override fun onResume() {
        super.onResume()
        subscriptions = CompositeDisposable()
    }

    override fun onPause() {
        super.onPause()

        if (!subscriptions.isDisposed) {
            subscriptions.clear()
        }

        subscriptions.clear()

    }

}