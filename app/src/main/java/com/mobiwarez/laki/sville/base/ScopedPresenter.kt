package com.mobiwarez.laki.sville.base

/**
 * Created by Laki on 19/10/2017.
 */
interface ScopedPresenter {

    fun start()

    fun activate()

    fun deactivate()

    fun destroy()

    fun back()
}