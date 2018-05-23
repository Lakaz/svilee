package com.mobiwarez.laki.sville.job

import android.util.Log
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import com.mobiwarez.data.account.db.model.AccountModel
import com.mobiwarez.laki.sville.data.accountManager.AccountsManager

class UpdateCreditsJob : Job() {


    override fun onRunJob(params: Job.Params): Job.Result {

        val result = updateCredits()

        return if (result){
            Result.SUCCESS
        }else{
            Result.RESCHEDULE
        }
    }

    private fun updateCredits(): Boolean {

        Log.d("CREDIT_JOB", "updating credits")
        val manager = AccountsManager(context)
        val accounts = manager.getBareAccounts()

        if (accounts.isEmpty()) {
            Log.d("CREDIT_JOB", "set up new account")
            val newAccount = AccountModel()
            newAccount.credits = "10"
            newAccount.rewardPoints = "0"
            manager.addNewBareAccount(newAccount)
            return true
        }else {
            val account = accounts[0]
            account.credits = "5"
            manager.updateBareAccount(account)
            return true
        }
    }

    companion object {

        val UPDATE_CREDITS_TAG = "UPDATE_CREDITS_JOB"

        val sec = 1000L
        val minute = sec * 60
        val hour = 60 * minute
        val day = 24 * hour
        val week = 7 * day


        fun buildUpdateCreditsJob(): Int {

            Log.d("CREDIT_JOB", "starting updating credits")

/*
            return JobRequest.Builder(UpdateCreditsJob.UPDATE_CREDITS_TAG)
                    .startNow()
                    .build()
                    .schedule()
*/

            return JobRequest.Builder(UpdateCreditsJob.UPDATE_CREDITS_TAG)
                    .startNow()
                    .setPeriodic(week)
                    .setExecutionWindow(2_000L, 6_000L)
                    .setBackoffCriteria(1_000L, JobRequest.BackoffPolicy.EXPONENTIAL)
                    .build()
                    .schedule()

        }
    }
}
