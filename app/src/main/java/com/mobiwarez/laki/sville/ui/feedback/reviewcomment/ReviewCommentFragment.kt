package com.mobiwarez.laki.sville.ui.feedback.reviewcomment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import com.mobiwarez.data.comments.db.model.CommentModel

import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.data.commentsManager.CommentsDatabaseManager
import com.mobiwarez.laki.sville.data.commentsManager.CommentsManager
import com.mobiwarez.laki.sville.fragments.RxBaseFragment
import com.mobiwarez.laki.sville.job.PostCommentJob
import com.mobiwarez.laki.sville.models.CommentsViewModel
import com.mobiwarez.laki.sville.prefs.GetUserData
import com.mobiwarez.laki.sville.recyclerUtils.InfiniteScrollListener
import com.mobiwarez.laki.sville.ui.comments.CommentAdapter
import com.mobiwarez.laki.sville.ui.comments.CommentsFragment
import com.mobiwarez.laki.sville.ui.toys.receivedToys.ReceivedViewModel
import com.mobiwarez.laki.sville.util.IdGenerator
import com.mobiwarez.laki.sville.util.showToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_review_comment.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_UUID = "uuid"
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ReviewCommentFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ReviewCommentFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ReviewCommentFragment : RxBaseFragment() {
    // TODO: Rename and change types of parameters
    private var commentUUID: String? = null
    private var writtenComent: String? = null

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private var commentsRecycler: RecyclerView? = null
    private var commentsLayoutManager: RecyclerView.LayoutManager? = null
    private var commentsAdapter: CommentAdapter? = null
    private var itemListAdapter: CommentAdapter? = null

    private var viewModel: ReceivedViewModel? = null

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        arguments?.let {
            commentUUID = it.getString(ARG_UUID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_review_comment, container, false)
        commentsRecycler = view.findViewById(R.id.review_comments_recycler)
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        comment_et.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                firebaseAnalytics.logEvent("wrote_comment", null)
                writtenComent = s?.toString()
                if ((s == null) or TextUtils.isEmpty(writtenComent)){
                    DrawableCompat.setTint(send_comment_image.drawable, ContextCompat.getColor(context, R.color.greylight))

                }else{
                    DrawableCompat.setTint(send_comment_image.drawable, ContextCompat.getColor(context, R.color.colorAccent))

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        DrawableCompat.setTint(send_comment_image.drawable, ContextCompat.getColor(context, R.color.greylight))

        send_comment_image.setOnClickListener {
            if (!TextUtils.isEmpty(writtenComent)){
                firebaseAnalytics.logEvent("submit_comment_clicked", null)
                comment_et.text = null
                val comment = createComment()



                val commViewModel = createCommentViewModel(comment)

                showComments(listOf(commViewModel))

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

            }
        }

        viewModel = ViewModelProviders.of(activity).get(ReceivedViewModel::class.java)


        commentsRecycler.apply {
            this!!.setHasFixedSize(true)
            val linearLayout = LinearLayoutManager(context)
            val decorator = DividerItemDecoration(context, linearLayout.orientation)
            layoutManager = linearLayout
            clearOnScrollListeners()
            //addItemDecoration(decorator)
            addOnScrollListener(InfiniteScrollListener({commentUUID?.let { fetchComments(it) }}, linearLayout))
        }

        initAdapter()

        val commentsViewModel = createCommentViewModel(viewModel?.commentModel!!)
        showComments(listOf(commentsViewModel))

    }

    private fun createComment(): CommentModel {
        val commentModel = CommentModel()
        commentModel.comment = writtenComent
        commentModel.commentId = IdGenerator.getCommentId(context)
        commentModel.commentedName = viewModel?.receivedToyModel?.giverName
        commentModel.commentedAvatarUrl = viewModel?.receivedToyModel?.avatarUrl
        commentModel.commentedUUID = viewModel?.receivedToyModel?.giverUuid
        commentModel.commentorName = GetUserData.getUserDisplayName(context)
        commentModel.commentorAvataUrl = GetUserData.getUserPhotoUrl(context)
        commentModel.timeCommented = IdGenerator.getTimeStamp()

        return commentModel
    }


    private fun createCommentViewModel(comm: CommentModel): CommentsViewModel{

        val commentsViewModel = CommentsViewModel(
                comm?.commentId!!,
                comm?.commentorName,
                comm.commentedName,
                comm.commentorAvataUrl,
                comm.commentedAvatarUrl,
                comm.commentedUUID,
                comm.comment,
                comm.timeCommented
        )

        return commentsViewModel
    }

    override fun onResume() {
        super.onResume()
        commentUUID?.let { fetchComments(it) }
    }


    private fun initAdapter() {
        if (commentsRecycler!!.adapter == null) {
            itemListAdapter = CommentAdapter()
            commentsRecycler!!.adapter = itemListAdapter
        }

        /*       if(!viewModel?.getItemsListed()?.isEmpty()!!){
                   val lists = viewModel?.getItemsListed()

                   lists?.let {
                       //isLoaded = true
                       (item_list_recyclerView.adapter!! as ItemsListAdapter).addItems(it)
                   }

               }
       */
    }

    fun showComments(comments: List<CommentsViewModel>){
        itemListAdapter?.addItems(comments)
    }


    private fun fetchComments(uuid: String){
        val manager = CommentsManager()
        val subscription = manager.getComments(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            results -> if (results.listings!!.isNotEmpty()){
                            showComments(results.listings)
                        }else{
                            activity.showToast("No comments")
                        }
                        },
                        {
                            err -> activity.showToast("failed to fetch comments")
                        }
                )

        subscriptions.add(subscription)
    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
 /*       if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
*/
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

        val TAG = ReviewCommentFragment::class.java.simpleName
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReviewCommentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(uuid: String) =

                ReviewCommentFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_UUID, uuid)
                    }
                }

    }
}
