package com.mobiwarez.domain.domain.intereactions.type


import io.reactivex.Single

interface SingleUseCase<T> {

    fun execute(): Single<T>
}
