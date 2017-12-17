package com.mobiwarez.domain.domain.intereactions.type

import io.reactivex.Observable

/**
 * Created by Laki on 19/10/2017.
 */

interface UseCase<T> {
    fun execute(): Observable<T>
}
