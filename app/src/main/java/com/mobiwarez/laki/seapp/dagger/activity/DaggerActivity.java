package com.mobiwarez.laki.seapp.dagger.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mobiwarez.laki.seapp.dagger.ComponentFactory;
import com.mobiwarez.laki.seapp.dagger.application.SeeAppApplication;

//import oxim.digital.reedly.dagger.ComponentFactory;
//import oxim.digital.reedly.dagger.application.ReedlyApplication;

public abstract class DaggerActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject(getActivityComponent());
    }

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = ComponentFactory.createActivityComponent(this, getSeeApplication());
        }
        return activityComponent;
    }

    private SeeAppApplication getSeeApplication() {
        return ((SeeAppApplication) getApplication());
    }

    protected abstract void inject(final ActivityComponent activityComponent);
}
