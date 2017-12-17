package com.mobiwarez.laki.seapp.ui.toys.receivedToys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mobiwarez.laki.seapp.R
import kotlinx.android.synthetic.main.activity_received_toys.*

class ReceivedToysActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received_toys)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val manager = supportFragmentManager
        if (manager.findFragmentByTag(ReceivedToysFragment.TAG) == null) {
            manager.beginTransaction().add(R.id.received_items_container, ReceivedToysFragment.newInstance(), ReceivedToysFragment.TAG).commit()
        }

    }

}
