package com.mobiwarez.laki.seapp.dagger;

import com.mobiwarez.laki.seapp.dagger.activity.ActivityComponent;
import com.mobiwarez.laki.seapp.dagger.activity.DaggerActivity;
import com.mobiwarez.laki.seapp.dagger.application.ApplicationComponent;
import com.mobiwarez.laki.seapp.dagger.application.SeeAppApplication;
import com.mobiwarez.laki.seapp.dagger.fragment.DaggerFragment;
import com.mobiwarez.laki.seapp.dagger.fragment.FragmentComponent;

/**
 * Created by Laki on 19/10/2017.
 */

public final class ComponentFactory {

    private ComponentFactory() {
    }

    public static ApplicationComponent createApplicationComponent(final SeeAppApplication seeAppApplication) {
        return ApplicationComponent.Initializer.init(seeAppApplication);
    }

    public static ActivityComponent createActivityComponent(final DaggerActivity daggerActivity, final SeeAppApplication seeAppApplication) {
        return ActivityComponent.Initializer.init(daggerActivity, seeAppApplication.getApplicationComponent());
    }

    public static FragmentComponent createFragmentComponent(final DaggerFragment daggerFragment, final ActivityComponent activityComponent) {
        return FragmentComponent.Initializer.init(daggerFragment, activityComponent);
    }


}
