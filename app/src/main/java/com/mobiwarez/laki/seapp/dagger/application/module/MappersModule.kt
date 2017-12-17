package com.mobiwarez.laki.seapp.dagger.application.module

import com.mobiwarez.laki.seapp.ui.mapper.ToyViewModeMapper
import com.mobiwarez.laki.seapp.ui.mapper.ToyViewModeMapperImpl

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
