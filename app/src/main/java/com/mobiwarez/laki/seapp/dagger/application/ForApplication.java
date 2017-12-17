package com.mobiwarez.laki.seapp.dagger.application;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by Laki on 19/10/2017.
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ForApplication {
}
