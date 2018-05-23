package com.mobiwarez.laki.sville.job

import android.util.Log
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import com.evernote.android.job.util.support.PersistableBundleCompat
import com.mobiwarez.data.comments.db.model.CommentModel
import com.mobiwarez.laki.sville.data.commentsManager.CommentsDatabaseManager
import com.mobiwarez.laki.sville.data.commentsManager.CommentsManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PostCommentJob: Job(){

    override fun onRunJob(params: Params?): Result {


        val extras = params?.getExtras()
        val commentId = extras?.getString("COMMENT_ID", "")
        val comment = getComment(commentId!!)

        var done = true

        comment?.let {
            val manager = CommentsManager()

            val subscription = manager.updateComment(it)



            subscription.subscribeOn(Schedulers.io())
            subscription.observeOn(AndroidSchedulers.mainThread())
            subscription.subscribe(
                    {
                        response -> done = true
                        Log.d("COMMENT", "posted comment")
                    },
                    {
                        err -> done = false
                        Log.d("COMMENT", "failed to post comment")
                    })

        }

        if (done){
            return Result.SUCCESS
        }else{
            return Result.RESCHEDULE
        }



    }

    private fun getComment(id: String) : CommentModel? {

        val commentsDatabaseManager = CommentsDatabaseManager(context)
        return commentsDatabaseManager.simpleGetComment(id)

    }

    companion object {

        val POST_COMMENT_TAG = "POST_COMMENT_JOB"

        fun buildPostCommentJob(id: String): Int {

            val extras = PersistableBundleCompat()

            extras.putString("COMMENT_ID", id)

            Log.d("COMMENT", "post job started comment")

            return JobRequest.Builder(PostCommentJob.POST_COMMENT_TAG)
                    .setExecutionWindow(2_000L, 6_000L)
                    .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                    .setBackoffCriteria(10_000L, JobRequest.BackoffPolicy.EXPONENTIAL)
                    .setExtras(extras)
                    .setRequirementsEnforced(true)
                    .build()
                    .schedule()

        }
    }

}
