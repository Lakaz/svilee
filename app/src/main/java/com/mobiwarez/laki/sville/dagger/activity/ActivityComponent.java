package com.mobiwarez.laki.sville.dagger.activity;

import com.mobiwarez.laki.sville.dagger.activity.DaggerActivityComponent;
import com.mobiwarez.laki.sville.dagger.activity.module.ActivityModule;
import com.mobiwarez.laki.sville.dagger.application.ApplicationComponent;
import com.mobiwarez.laki.sville.dagger.activity.module.ActivityModule;
import com.mobiwarez.laki.sville.dagger.application.ApplicationComponent;

import dagger.Component;
//import oxim.digital.reedly.dagger.activity.module.ActivityModule;
//import oxim.digital.reedly.dagger.application.ApplicationComponent;

@ActivityScope
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ActivityModule.class
        }
)
public interface ActivityComponent extends ActivityComponentInjects, ActivityComponentExposes {

    final class Initializer {

        static public ActivityComponent init(final DaggerActivity daggerActivity, final ApplicationComponent applicationComponent) {
            return DaggerActivityComponent.builder()
                                          .applicationComponent(applicationComponent)
                                          .activityModule(new ActivityModule(daggerActivity))
                                          .build();
        }

        private Initializer() {
        }
    }
}
