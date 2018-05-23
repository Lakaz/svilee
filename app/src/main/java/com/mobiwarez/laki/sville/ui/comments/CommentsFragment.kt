package com.mobiwarez.laki.sville.ui.comments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobiwarez.data.comments.db.model.CommentModel
import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.data.commentsManager.CommentsManager
import com.mobiwarez.laki.sville.fragments.RxBaseFragment
import com.mobiwarez.laki.sville.models.CommentsViewModel
import com.mobiwarez.laki.sville.recyclerUtils.InfiniteScrollListener
import com.mobiwarez.laki.sville.ui.toys.list.ItemsListAdapter
import com.mobiwarez.laki.sville.util.showToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_item_list.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_UUID = "uuid"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CommentsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CommentsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CommentsFragment : RxBaseFragment() {
    // TODO: Rename and change types of parameters
    private var commentUUID: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null


    private var commentsRecycler: RecyclerView? = null
    private var commentsLayoutManager: RecyclerView.LayoutManager? = null
    private var commentsAdapter: CommentAdapter? = null
    private var itemListAdapter: CommentAdapter? = null

    private var age:String? = null
    private var category:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            commentUUID = it.getString(ARG_UUID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_comments, container, false)
        commentsRecycler = view.findViewById(R.id.comments_recycler)
        //initializeRecyclerView()
        return view
    }


    override fun onResume() {
        super.onResume()
        commentUUID?.let { fetchComments(it) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        commentsRecycler.apply {
            this!!.setHasFixedSize(true)
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout
            val decorator = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            //addItemDecoration(decorator)
            clearOnScrollListeners()
            addOnScrollListener(InfiniteScrollListener({commentUUID?.let { fetchComments(it) }}, linearLayout))
        }

        initAdapter()

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


    private fun initializeRecyclerView() {

        if (commentsRecycler!!.adapter == null) {
            commentsAdapter = CommentAdapter()
            commentsRecycler!!.adapter = commentsAdapter
        } else {
            commentsAdapter = commentsRecycler!!.adapter as CommentAdapter
        }

        commentsLayoutManager = LinearLayoutManager(context)

        commentsRecycler!!.layoutManager = commentsLayoutManager

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
/*
        if (context is OnFragmentInteractionListener) {
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

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        val TAG = CommentsFragment::class.java.simpleName
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CommentsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(uuid: String) =
                CommentsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_UUID, uuid)
                    }
                }
    }
}
