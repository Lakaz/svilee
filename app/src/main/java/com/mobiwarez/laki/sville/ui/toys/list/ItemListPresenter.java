package com.mobiwarez.laki.sville.ui.toys.list;

import android.location.Location;

import com.mobiwarez.laki.sville.base.BasePresenter;
import com.mobiwarez.laki.sville.ui.models.ToyViewModel;
import com.mobiwarez.laki.sville.ui.toys.itemcategories.ItemCategoryContract;

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
