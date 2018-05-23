package com.mobiwarez.laki.sville.ui.account

import com.mobiwarez.data.account.db.model.AccountModel

interface AccountContract {

    interface View {
        fun showAccount(accountModel: AccountModel)

        fun showError(message: String)
    }

    interface Presenter {
        fun getAccountInfo()
    }
}