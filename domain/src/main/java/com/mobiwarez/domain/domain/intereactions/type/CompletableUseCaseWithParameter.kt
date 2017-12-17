package com.mobiwarez.domain.domain.intereactions.type


import io.reactivex.Completable

interface CompletableUseCaseWithParameter<P> {

    fun execute(parameter: P): Completable
}
