package com.mobiwarez.laki.seapp.ui.mapper

import com.mobiwarez.domain.domain.models.Toy
import com.mobiwarez.laki.seapp.ui.models.ToyViewModel

/**
 * Created by Laki on 21/10/2017.
 */
interface ToyViewModeMapper {

    fun mapToyToViewModel(toy: Toy): ToyViewModel

    fun mapFeedsToViewModels(toys: List<Toy>): List<ToyViewModel>

}