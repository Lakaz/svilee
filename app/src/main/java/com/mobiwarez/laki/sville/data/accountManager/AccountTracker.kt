package com.mobiwarez.laki.sville.data.accountManager

import android.content.Context
import io.reactivex.Observable

class AccountTracker(val context: Context) {


    fun decreaseCredits(): Observable<String>{

        return Observable.create {
            subscriber ->
            try {
                val manager = AccountsManager(context)
                val accounts = manager.getBareAccounts()
                if (accounts.isEmpty()){

                }else{
                    val account = accounts[0]
                    val credits = account.credits.toInt()
                    if (credits > 0) {
                        account.credits = (credits - 1).toString()
                        manager.updateBareAccount(account)
                    }else{
                        val points = account.rewardPoints.toInt()
                        if (points > 0) {
                            account.rewardPoints = (points - 1).toString()
                            manager.updateBareAccount(account)
                        }
                    }
                }

            }catch (e: Exception) {
                subscriber.onError(Throwable(e))
            }
        }

    }

    fun awardPoint(): Observable<String> {
        val manager = AccountsManager(context)

        return Observable.create{
            subscriber ->
            try {
                val accounts = manager.getBareAccounts()

                if (accounts.isEmpty()){
                }else{
                    val account = accounts[0]
                    val points = account.rewardPoints.toInt()
                    account.rewardPoints = (points + 1).toString()
                    manager.updateBareAccount(account)
                }

                subscriber.onNext("done")
                subscriber.onComplete()
            }catch (e: Exception){
                subscriber.onError(Throwable(e))
            }

        }

    }

    fun canReceive(): Observable<Boolean>{
        val manager = AccountsManager(context)
        return Observable.create {
            subscriber ->
            try {
                val accounts = manager.getBareAccounts()
                if (accounts.isEmpty()) {

                } else {
                    val account = accounts[0]
                    val credits = account.credits.toInt()
                    val points = account.rewardPoints.toInt()
                    val response = (credits > 0) or (points > 0)
                    subscriber.onNext(response)
                }
            }catch (e: Exception){
                subscriber.onError(Throwable(e))
            }

        }

    }

}