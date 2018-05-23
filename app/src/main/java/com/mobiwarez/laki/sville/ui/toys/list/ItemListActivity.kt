package com.mobiwarez.laki.sville.ui.toys.list

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View

import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.ui.comments.CommentsFragment
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.activity_item_list.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ItemListActivity : AppCompatActivity() {

    private var viewModel:ItemListViewModel? = null
    var toolbar: Toolbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = "Gifts Offered"
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this).get(ItemListViewModel::class.java)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }


        val intent = intent
        val category = intent.getStringExtra("category")
        val age = intent.getStringExtra("age")

        val manager = supportFragmentManager
        if (manager.findFragmentByTag(ItemListFragment.TAG) == null) {
            val itemListFragment = ItemListFragment.newInstance(age, category)
            manager.beginTransaction().add(R.id.list_container, itemListFragment, ItemListFragment.TAG).commit()
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
    fun showReviews(showReviewMessage: ShowReviewMessage){
        fab.hide()

        supportActionBar?.title = "  "+viewModel?.selectedModel?.giverName+"  Reviews"
        viewModel?.selectedAvatarDawable?.let { supportActionBar?.setIcon(it) }
        //supportActionBar?.subtitle = "   Reviews"
        //supportActionBar?.setIcon()
        val commentsFragment = CommentsFragment.newInstance(viewModel?.selectedGiver!!)
        supportFragmentManager.beginTransaction()
                .replace(R.id.list_container, commentsFragment, CommentsFragment.TAG)
                .addToBackStack(ItemListFragment.TAG)
                .commit()
    }

    @Subscribe
    fun undoChanges(hideReviewMessage: HideReviewMessage){
        supportActionBar?.title = "Gifts Offered"
        //supportActionBar?.subtitle = null
        supportActionBar?.setIcon(null)
        fab.show()
    }

}
