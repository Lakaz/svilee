package com.mobiwarez.laki.sville.ui.account

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.ui.toys.receivedToys.ReceivedToysFragment

import kotlinx.android.synthetic.main.activity_accounts.*

class AccountsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)
        toolbar.title = "Account"
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val manager = supportFragmentManager
        if (manager.findFragmentByTag(ReceivedToysFragment.TAG) == null) {
            manager.beginTransaction().add(R.id.accounts_container, AccountFragment.newInstance(), AccountFragment.TAG).commit()
        }


    }

}
