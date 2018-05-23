package com.mobiwarez.laki.sville.dagger.application.module;

import com.mobiwarez.laki.sville.ui.mapper.ToyViewModeMapper;
import com.mobiwarez.laki.sville.ui.mapper.ToyViewModeMapperImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Laki on 24/10/2017.
 */


@Module
public class RealMappersModule {

    @Provides
    @Singleton
    ToyViewModeMapper provideToyViewModeMapper()  {
        return new ToyViewModeMapperImpl();
    }

    interface Exposes {
        ToyViewModeMapper toyViewModeMapper();
    }

}
