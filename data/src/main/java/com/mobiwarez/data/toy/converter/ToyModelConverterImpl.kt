package com.mobiwarez.data.toy.converter

import com.mobiwarez.data.toy.db.model.ToyModel
import com.mobiwarez.data.toy.service.model.ApiToy
import com.mobiwarez.domain.domain.models.Toy

/**
 * Created by Laki on 21/10/2017.
 */
class ToyModelConverterImpl: ToyModelConveter {
    override fun domainToModel(toy: Toy): ToyModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun modelToDomain(toyModel: ToyModel): Toy {
        return Toy(
                toyModel.toyName,
                toyModel.toyDescription,
                toyModel.toyCategory,
                toyModel.toyUrl,
                toyModel.toyGiven,
                toyModel.timePosted
        )
    }

    override fun apiToModel(apiToy: ApiToy): ToyModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}