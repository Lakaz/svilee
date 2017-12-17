package com.mobiwarez.laki.seapp.ui.toys.list;

import android.location.Location;

import com.mobiwarez.laki.seapp.base.BasePresenter;
import com.mobiwarez.laki.seapp.ui.models.ToyViewModel;
import com.mobiwarez.laki.seapp.ui.toys.itemcategories.ItemCategoryContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laki on 27/10/2017.
 */

public class ItemListPresenter  implements ItemsContract.Presenter {

    private ItemsContract.View view;

    public ItemListPresenter(ItemsContract.View view) {
        this.view = view;
    }

    @Override
    public void receiveItem(int itemId) {

    }

    @Override
    public void fetchItems(String ageGroup, String category) {

    }
}
