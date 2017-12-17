package com.mobiwarez.domain.domain.intereactions.toy

import com.mobiwarez.domain.domain.intereactions.type.CompletableUseCaseWithParameter
import com.mobiwarez.domain.domain.models.Toy
import com.mobiwarez.domain.domain.repository.ToyRepository
import io.reactivex.Completable

/**
 * Created by Laki on 19/10/2017.
 */
class AddNewToyUseCase(val toyRepository: ToyRepository) : CompletableUseCaseWithParameter<Toy>{

    override fun execute(parameter: Toy): Completable {
        return toyRepository.createNewToy(parameter)
    }
}