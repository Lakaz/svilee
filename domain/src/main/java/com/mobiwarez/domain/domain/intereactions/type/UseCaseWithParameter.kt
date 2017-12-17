package com.mobiwarez.domain.domain.intereactions.type


import io.reactivex.Observable

interface UseCaseWithParameter<P, R> {

    fun execute(parameter: P): Observable<R>
}
