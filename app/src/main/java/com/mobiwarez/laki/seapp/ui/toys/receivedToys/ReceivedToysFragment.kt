package com.mobiwarez.laki.seapp.ui.toys.receivedToys


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobiwarez.data.receivedToys.db.model.ReceivedToyModel
import com.mobiwarez.laki.seapp.R
import com.mobiwarez.laki.seapp.contactsmanager.ContactsManager
import com.mobiwarez.laki.seapp.fragments.RxBaseFragment
import com.mobiwarez.laki.seapp.ui.chat.FireChatActivity
import com.mobiwarez.laki.seapp.ui.imageview.ImageDetailActivity
import com.mobiwarez.laki.seapp.ui.toys.sharedtoys.SharedToysFragment
import com.mobiwarez.laki.seapp.util.DatabaseManger
import com.mobiwarez.laki.seapp.util.StringConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_received_toys.*


/**
 * A simple [Fragment] subclass.
 * Use the [ReceivedToysFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReceivedToysFragment : RxBaseFragment(), ReceivedToysAdapter.ReceivedItemListener, ReceivedToysContract.View {


    private var adapter: ReceivedToysAdapter? = null
    private var receivedLayoutManager: RecyclerView.LayoutManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        fetchItems()
    }

    private fun fetchItems() {
        val manager = DatabaseManger(context)
        val subcription = manager.getReceivedItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            items ->
                                if (items.isEmpty()){
                                    showMessages("You have not received any items as yet.")
                                }else{
                                    showReceivedToys(items.reversed())
                                }

                        },
                        {
                            err -> showMessages("Failed to load items")
                        }
                )

        subscriptions.add(subcription)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_received_toys, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeRecyclerView()
    }

    override fun showReceivedToys(items: List<ReceivedToyModel>) {
        adapter?.onItemsUpdate(items)
    }

    override fun showMessages(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun initializeRecyclerView() {
        if (received_toys_recycler!!.adapter == null) {
            adapter = ReceivedToysAdapter( this)
            received_toys_recycler!!.adapter = adapter
        } else {
            adapter = received_toys_recycler!!.adapter as ReceivedToysAdapter
        }
        receivedLayoutManager = LinearLayoutManager(null)
        received_toys_recycler!!.layoutManager = receivedLayoutManager
    }


    override fun showFullImage(imageUrl: String) {
        if (!TextUtils.isEmpty(imageUrl) && imageUrl != "no_image") {
            val intent = Intent(context, ImageDetailActivity::class.java)
            intent.putExtra(StringConstants.IMAGE_URL_KEY, imageUrl)
            startActivity(intent)
        }
    }


    override fun startChat(model: ReceivedToyModel) {

        val contactsManager = ContactsManager(context)
        val subscription = contactsManager.getChatRoom(model.giverUuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            room -> if (room != "no chat room"){
                            showChatRoom(room, model.giverUuid, model.giverName)
                        }else{

                            showMessages("Failed to start chat")
                        }
                        },
                        {
                            showMessages("Failed to start chat")
                        }
                )

        subscriptions.add(subscription)




    }

    private fun showChatRoom(room: String, giverUuid: String, giverName: String) {
        val intent = Intent(activity, FireChatActivity::class.java)
        intent.putExtra("chatRoom", room)
        intent.putExtra("uuid", giverUuid)
        intent.putExtra("name", giverName)
        startActivity(intent)
    }

    override fun unClaim(model: ReceivedToyModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    companion object {

        val TAG = ReceivedToysFragment::class.java.simpleName
        fun newInstance(): ReceivedToysFragment = ReceivedToysFragment()
    }

}// Required empty public constructor
