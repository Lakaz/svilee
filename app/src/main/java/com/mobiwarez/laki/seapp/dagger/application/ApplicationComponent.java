package com.mobiwarez.laki.seapp.dagger.application;

import com.mobiwarez.laki.seapp.dagger.application.module.ApplicationModule;
import com.mobiwarez.laki.seapp.dagger.application.module.DataModule;
import com.mobiwarez.laki.seapp.dagger.application.module.MappersModule;
import com.mobiwarez.laki.seapp.dagger.application.module.RealDataModule;
import com.mobiwarez.laki.seapp.dagger.application.module.RealMappersModule;
import com.mobiwarez.laki.seapp.dagger.application.module.RealUseCaseModule;
import com.mobiwarez.laki.seapp.dagger.application.module.ServiceModule;
import com.mobiwarez.laki.seapp.dagger.application.module.ThreadingModule;
import com.mobiwarez.laki.seapp.dagger.application.module.UseCaseModule;
import com.mobiwarez.laki.seapp.dagger.application.module.UtilsModule;

import javax.inject.Singleton;

import dagger.Component;


/**
 * Created by Laki on 19/10/2017.
 */

@Singleton
@Component(
    modules = {
            ApplicationModule.class,
            ThreadingModule.class,
            RealDataModule.class,
            UtilsModule.class,
            RealUseCaseModule.class,
            RealMappersModule.class,
            ServiceModule.class

    }
)
public interface ApplicationComponent extends ApplicationComponentInjects, ApplicationComponentExposes {

    final class Initializer {

        static public ApplicationComponent init(final SeeAppApplication seeAppApplication) {
            return DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(seeAppApplication))
                    .build();
        }

        private Initializer() {
        }

    }


}
