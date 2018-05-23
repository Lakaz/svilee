package com.mobiwarez.laki.sville.ui.router

interface Router{
    fun closeScreen()

    fun goBack()

    fun showChatScreen()

    fun showToysScreen(category: String, ageGroup: String)

    fun showSearchSetUpScreen()

    fun showAddNewToyScreen()

    fun hideAddNewToyScreen()

    fun showZoneScreen()

    fun showGivenToysScreen()

    fun showReceivedToysScreen()

    fun showReceiveSetUpScreen()

}
