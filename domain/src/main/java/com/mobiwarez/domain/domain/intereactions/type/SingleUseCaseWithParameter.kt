package com.mobiwarez.domain.domain.intereactions.type


import io.reactivex.Single

interface SingleUseCaseWithParameter<P, R> {

    fun execute(parameter: P): Single<R>
}
