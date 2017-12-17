package com.mobiwarez.laki.seapp.ui.toys.list;

import android.arch.lifecycle.ViewModel;

import com.mobiwarez.laki.seapp.ui.models.ToyViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laki on 02/11/2017.
 */

public class ToysListViewModel extends ViewModel {

    private List<ToyViewModel> toyViewModels = new ArrayList<>();

    private List<ToyViewModel> getToyViewModels() {
        return toyViewModels;
    }

    private void addItems(List<ToyViewModel> toyViewModels) {
        this.toyViewModels.addAll(toyViewModels);
    }

}
