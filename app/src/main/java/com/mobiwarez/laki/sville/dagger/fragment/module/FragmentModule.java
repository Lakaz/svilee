package com.mobiwarez.laki.sville.dagger.fragment.module;

import com.mobiwarez.laki.sville.dagger.fragment.DaggerFragment;

import dagger.Module;
//import oxim.digital.reedly.dagger.fragment.DaggerFragment;

@Module
public class FragmentModule {

    private final DaggerFragment fragment;

    public FragmentModule(final DaggerFragment fragment) {
        this.fragment = fragment;
    }


}
