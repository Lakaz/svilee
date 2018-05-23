package com.mobiwarez.laki.sville.dagger.application.module

import com.mobiwarez.laki.sville.ui.mapper.ToyViewModeMapper
import com.mobiwarez.laki.sville.ui.mapper.ToyViewModeMapperImpl

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class MappersModule {

    @Provides
    @Singleton
    fun provideToyViewModeMapper(): ToyViewModeMapper {
        return ToyViewModeMapperImpl()
    }

    interface Exposes {
        fun toyViewModeMapper(): ToyViewModeMapper
    }


}
