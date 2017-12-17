package com.mobiwarez.laki.seapp.ui.toys.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mobiwarez.laki.seapp.R;

public class ItemListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Items Offered");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());


        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        String age = intent.getStringExtra("age");

        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentByTag(ItemListFragment.Companion.getTAG()) == null){
            ItemListFragment itemListFragment = ItemListFragment.Companion.newInstance(age, category);
            manager.beginTransaction().add(R.id.list_container, itemListFragment, ItemListFragment.Companion.getTAG()).commit();
        }
    }

}
