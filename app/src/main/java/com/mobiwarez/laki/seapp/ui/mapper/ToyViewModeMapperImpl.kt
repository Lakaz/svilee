package com.mobiwarez.laki.seapp.ui.mapper

import android.location.Location
import com.mobiwarez.domain.domain.models.Toy
import com.mobiwarez.laki.seapp.ui.models.ToyViewModel

/**
 * Created by Laki on 21/10/2017.
 */
class ToyViewModeMapperImpl: ToyViewModeMapper {
    override fun mapToyToViewModel(toy: Toy): ToyViewModel {
        val po = Location("")
        val toyViewModel = ToyViewModel(
                "hjuy",
                "gg",
                toy.toyName,
                toy.toyCategory!!,
                "child",
                toy.toyImagePath,
                "available",
                toy.timePosted,
                "cool",
                "hhhh",
                "mnjhh",
                9.99f,
                8.00f,
                "here",
                "now",
                "uu",
                po
        )

        return toyViewModel
    }

    override fun mapFeedsToViewModels(toys: List<Toy>): List<ToyViewModel> {
        return toys.map { it -> mapToyToViewModel(it) }
    }
}