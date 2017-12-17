package com.mobiwarez.laki.seapp.ui.toys.create;

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
import com.mobiwarez.laki.seapp.ui.MainPresenter;
import com.mobiwarez.laki.seapp.ui.starter.DashboardFragment;
import com.mobiwarez.laki.seapp.util.ActivityUtils;

import javax.inject.Inject;

public class NewToActivity extends AppCompatActivity implements PhotoFragment.Proceed {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Give Item");
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentByTag(PhotoFragment.Companion.getTAG()) == null){
            PhotoFragment photoFragment = PhotoFragment.Companion.newInstance();
            fragmentManager.beginTransaction().add(R.id.new_toy_container, photoFragment, PhotoFragment.Companion.getTAG()).commit();
        }


    }


    @Override
    public void moveToDetails() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        CreateToyFragment createToyFragment = CreateToyFragment.Companion.newInstance();
        fragmentManager.beginTransaction().replace(R.id.new_toy_container, createToyFragment, CreateToyFragment.Companion.getTAG()).addToBackStack(PhotoFragment.Companion.getTAG()).commit();

    }
}
