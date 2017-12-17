package com.mobiwarez.data.toy.converter

import com.mobiwarez.data.toy.db.model.ToyModel
import com.mobiwarez.data.toy.service.model.ApiToy
import com.mobiwarez.domain.domain.models.Toy

/**
 * Created by Laki on 21/10/2017.
 */
interface ToyModelConveter {

    fun domainToModel(toy: Toy): ToyModel

    fun modelToDomain(toyModel: ToyModel): Toy

    fun apiToModel(apiToy: ApiToy): ToyModel

}