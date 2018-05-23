package com.mobiwarez.laki.sville.data.accountManager

import android.content.Context
import com.mobiwarez.data.account.db.definition.AccountDatabaseFactory
import com.mobiwarez.data.account.db.model.AccountModel
import io.reactivex.Observable

class AccountsManager(val context: Context) {

    private val accountDao = AccountDatabaseFactory.getInstance(context).accountDao()

    fun addNewBareAccount(accountModel: AccountModel){
        accountDao.insertAll(accountModel)
    }

    fun addNewAccount(accountModel: AccountModel): Observable<Boolean>{
        return Observable.create{
            subscription ->
            try {
                accountDao.insertAll(accountModel)
                subscription.onNext(true)
                subscription.onComplete()
            }catch (e: Exception){
                subscription.onError(Throwable(e))
            }
        }
    }



    fun getAccounts(): Observable<List<AccountModel>> {
        return Observable.create{
            subscription ->
            try {
                val list = accountDao.all
                subscription.onNext(list)
                subscription.onComplete()
            }catch (e:Exception){
                subscription.onError(Throwable(e))
            }
        }
    }

    fun getBareAccounts(): List<AccountModel> {
        return accountDao.all
    }

    fun updateBareAccount(accountModel: AccountModel) {
        accountDao.updateAccount(accountModel)
    }

}