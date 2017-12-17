package com.mobiwarez.domain.domain.intereactions.toy

import com.mobiwarez.domain.domain.intereactions.type.SingleUseCase
import com.mobiwarez.domain.domain.models.Toy
import com.mobiwarez.domain.domain.repository.ToyRepository
import io.reactivex.Single

/**
 * Created by Laki on 21/10/2017.
 */
class GetToysUseCase(val toyRepository: ToyRepository): SingleUseCase<List<Toy>> {
    override fun execute(): Single<List<Toy>> {
        return toyRepository.getToysFromLocal()
    }

}