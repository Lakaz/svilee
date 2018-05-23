package com.mobiwarez.laki.sville.ui.toys.itemcategories;

import com.mobiwarez.laki.sville.base.BasePresenter;
import com.mobiwarez.laki.sville.ui.router.Router;
import com.mobiwarez.laki.sville.ui.router.Router;

import javax.inject.Inject;

/**
 * Created by Laki on 27/10/2017.
 */

public class ItemCategoryPresenter implements ItemCategoryContract.Presenter {

    private ItemCategoryContract.View view;

    @Inject
    Router router;



    public ItemCategoryPresenter(ItemCategoryContract.View view) {
        this.view = view;
    }

    @Override
    public void moveToItemList(String ageGroup, String category) {
        //router.showToysScreen(category, ageGroup);
    }

    @Override
    public void setLocation() {

    }
}
