package com.mobiwarez.laki.sville.ui.toys.create

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View

import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.base.BaseActivity
import com.mobiwarez.laki.sville.base.ScopedPresenter
import com.mobiwarez.laki.sville.dagger.activity.ActivityComponent
import com.mobiwarez.laki.sville.ui.MainPresenter
import com.mobiwarez.laki.sville.ui.starter.DashboardFragment
import com.mobiwarez.laki.sville.util.ActivityUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

import javax.inject.Inject

class NewToActivity : AppCompatActivity(), PhotoFragment.Proceed {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_to)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Give Item"
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragmentManager = supportFragmentManager


        if (fragmentManager.findFragmentByTag(NewCreateToyFragment.TAG) == null) {
            val newCreateToyFragment = NewCreateToyFragment.newInstance()
            fragmentManager.beginTransaction().add(R.id.new_toy_container, newCreateToyFragment, NewCreateToyFragment.TAG).commit()
        }

/*
        if (fragmentManager.findFragmentByTag(PhotoFragment.TAG) == null) {
            val photoFragment = PhotoFragment.newInstance()
            fragmentManager.beginTransaction().add(R.id.new_toy_container, photoFragment, PhotoFragment.TAG).commit()
        }
*/


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

    override fun moveToDetails() {
        val fragmentManager = supportFragmentManager
        val createToyFragment = CreateToyFragment.newInstance()
        fragmentManager.beginTransaction().replace(R.id.new_toy_container, createToyFragment, CreateToyFragment.TAG).addToBackStack(PhotoFragment.TAG).commit()

    }

    @Subscribe
    fun move(message: MoveToDetailsMessage){
        val fragmentManager = supportFragmentManager
        val createToyFragment = CreateToyFragment.newInstance()
        fragmentManager.beginTransaction().replace(R.id.new_toy_container, createToyFragment, CreateToyFragment.TAG).addToBackStack(NewCreateToyFragment.TAG).commit()

    }
}
