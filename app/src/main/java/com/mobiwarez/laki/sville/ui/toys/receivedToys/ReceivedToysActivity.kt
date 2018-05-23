package com.mobiwarez.laki.sville.ui.toys.receivedToys

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.ui.toys.list.ItemListViewModel
import kotlinx.android.synthetic.main.activity_received_toys.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ReceivedToysActivity : AppCompatActivity() {

    private var viewModel: ReceivedViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received_toys)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this).get(ReceivedViewModel::class.java)


        val manager = supportFragmentManager
        if (manager.findFragmentByTag(ReceivedToysFragment.TAG) == null) {
            manager.beginTransaction()
                    .add(R.id.received_items_container, ReceivedToysFragment.newInstance(), ReceivedToysFragment.TAG)
                    .commit()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }


    @Subscribe
    fun writeReview(writeReviewMessage: WriteReviewMessage){
        supportActionBar?.title = "Review"

    }

    @Subscribe
    fun showComments(showCommentsMessage: ShowCommentsMessage){
        viewModel?.selectedDrawable?.let { supportActionBar?.setIcon(it) }
        supportActionBar?.title = viewModel?.commentModel?.commentedName + " comments"
    }

    @Subscribe
    fun undo(undoMessage: UndoMessage){
        supportActionBar?.title = "Gifts you received"
        supportActionBar?.setIcon(null)
    }

}
