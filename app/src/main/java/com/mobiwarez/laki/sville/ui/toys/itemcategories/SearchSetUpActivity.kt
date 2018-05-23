package com.mobiwarez.laki.sville.ui.toys.itemcategories

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.mobiwarez.laki.sville.R

import kotlinx.android.synthetic.main.activity_search_set_up.*

class SearchSetUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_set_up)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val manager = supportFragmentManager

        if (manager.findFragmentByTag(ItemCategoryFragment.TAG) == null) {
            val itemCategoryFragment = ItemCategoryFragment.newInstance()
            manager.beginTransaction().add(R.id.set_up_container, itemCategoryFragment, ItemCategoryFragment.TAG).commit()
        }
    }


}
