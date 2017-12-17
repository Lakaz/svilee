package com.mobiwarez.domain.domain.intereactions.type

//import rx.Completable;

import io.reactivex.Completable

interface CompletableUseCase {

    fun execute(): Completable
}
