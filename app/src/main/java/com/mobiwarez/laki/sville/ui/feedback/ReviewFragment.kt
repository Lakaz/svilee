package com.mobiwarez.laki.sville.ui.feedback

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import com.google.firebase.analytics.FirebaseAnalytics
import com.mobiwarez.data.comments.db.model.CommentModel
import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.data.commentsManager.CommentsDatabaseManager
import com.mobiwarez.laki.sville.fragments.RxBaseFragment
import com.mobiwarez.laki.sville.job.PostCommentJob
import com.mobiwarez.laki.sville.prefs.GetUserData
import com.mobiwarez.laki.sville.ui.feedback.reviewcomment.ReviewCommentFragment
import com.mobiwarez.laki.sville.ui.toys.receivedToys.ReceivedViewModel
import com.mobiwarez.laki.sville.ui.toys.receivedToys.ShowCommentsMessage
import com.mobiwarez.laki.sville.util.IdGenerator
import com.mobiwarez.laki.sville.util.showToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_review.*
import org.greenrobot.eventbus.EventBus

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_UUID = "uuid"
private const val ARG_NAME = "name"
private const val ARG_AVATAR = "avatar"

class ReviewFragment : RxBaseFragment() {
    // TODO: Rename and change types of parameters
    private var name: String? = null
    private var uuid: String? = null
    private var avatar: String? = null

    private var listener: OnFragmentInteractionListener? = null
    private var assignedRating: Int? = null
    private var reviewComment: String? = null

    private var viewModel: ReceivedViewModel? = null

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        arguments?.let {
            name = it.getString(ARG_NAME)
            avatar = it.getString(ARG_AVATAR)
            uuid = it.getString(ARG_UUID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
/*
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
*/
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(activity).get(ReceivedViewModel::class.java)


        review_ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { ratingBar, v, b ->
            firebaseAnalytics.logEvent("rating_bar_clicked", null)
            assignedRating = ratingBar.rating.toInt()
        }

        name?.let { commented_name_tv.text = it }


        review_comment_et.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                firebaseAnalytics.logEvent("thanks_note_written", null)
                reviewComment = s?.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        review_submit_btn.setOnClickListener{
            firebaseAnalytics.logEvent("submit_review_clicked", null)
            submitReview()}
    }

    private fun submitReview() {

        if (!TextUtils.isEmpty(reviewComment)) {
            val comment = createComment()

            viewModel?.commentModel = comment

            val commentsDatabaseManager = CommentsDatabaseManager(context)

            val subscription = commentsDatabaseManager.addComment(comment)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { result ->
                                if (result) {
                                    Log.d("COMMENT", "saved comment")
                                    PostCommentJob.buildPostCommentJob(comment.commentId)
                                } else {

                                }
                            },
                            {
                                activity.showToast("failed to create comment")
                            }
                    )


            subscriptions.add(subscription)

            EventBus.getDefault().post(ShowCommentsMessage())

            val reviewCommentFragment = ReviewCommentFragment.newInstance(comment.commentedUUID)
            activity.supportFragmentManager.beginTransaction().replace(R.id.received_items_container, reviewCommentFragment,
                    ReviewCommentFragment.TAG).commit()
            //activity.showToast("Your comment is being posted")
            //activity.onBackPressed()
        }else{
            activity.showToast("Please write a comment")
        }

    }

    private fun createComment(): CommentModel {
        val commentModel = CommentModel()
        commentModel.comment = reviewComment
        commentModel.commentId = IdGenerator.getCommentId(context)
        commentModel.commentedName = name
        commentModel.commentedAvatarUrl = avatar
        commentModel.commentedUUID = uuid
        commentModel.commentorName = GetUserData.getUserDisplayName(context)
        commentModel.commentorAvataUrl = GetUserData.getUserPhotoUrl(context)
        commentModel.timeCommented = IdGenerator.getTimeStamp()

        return commentModel
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        val TAG = ReviewFragment::class.java.simpleName
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReviewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(uuid: String, uname: String, avatar: String) = ReviewFragment()
                .apply {
                    arguments = Bundle().apply {
                        putString(ARG_NAME, uname)
                        putString(ARG_AVATAR, avatar)
                        putString(ARG_UUID, uuid)
                    }

                }
    }
}
