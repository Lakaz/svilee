package com.mobiwarez.laki.seapp.ui.toys.sharedtoys;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mobiwarez.laki.seapp.R;
import com.mobiwarez.laki.seapp.base.BaseActivity;
import com.mobiwarez.laki.seapp.base.ScopedPresenter;
import com.mobiwarez.laki.seapp.dagger.activity.ActivityComponent;
import com.mobiwarez.laki.seapp.ui.toys.create.CreateToyFragment;
import com.mobiwarez.laki.seapp.util.ActivityUtils;

import javax.inject.Inject;

public class SharedToysActivity extends AppCompatActivity {

/*
    @Inject
    FragmentManager fragmentManager;

    @Inject
    ActivityUtils activityUtils;

    @Inject
    SharedToysActivityPresenter presenter;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_toys);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Items you gave");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
/*
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
*/

        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentByTag(SharedToysFragment.Companion.getTAG()) == null) {
            manager.beginTransaction().add(R.id.sharedtoyscontainer, SharedToysFragment.Companion.newInstance(), SharedToysFragment.Companion.getTAG()).commit();
        }

    }


}
