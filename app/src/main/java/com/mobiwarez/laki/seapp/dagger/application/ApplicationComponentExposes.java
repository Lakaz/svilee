package com.mobiwarez.laki.seapp.dagger.application;

import com.mobiwarez.laki.seapp.dagger.application.module.ApplicationModule;
import com.mobiwarez.laki.seapp.dagger.application.module.DataModule;
import com.mobiwarez.laki.seapp.dagger.application.module.MappersModule;
import com.mobiwarez.laki.seapp.dagger.application.module.ServiceModule;
import com.mobiwarez.laki.seapp.dagger.application.module.ThreadingModule;
import com.mobiwarez.laki.seapp.dagger.application.module.UseCaseModule;
import com.mobiwarez.laki.seapp.dagger.application.module.UtilsModule;

/**
 * Created by Laki on 19/10/2017.
 */

public interface ApplicationComponentExposes extends ApplicationModule.Exposes,
                                                     ThreadingModule.Exposes,
                                                     UtilsModule.Exposes,
                                                     UseCaseModule.Exposes,
                                                     DataModule.Exposes,
                                                     MappersModule.Exposes,
                                                     ServiceModule.Exposes {
}
