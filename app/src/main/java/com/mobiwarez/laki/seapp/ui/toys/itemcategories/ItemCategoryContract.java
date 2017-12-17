package com.mobiwarez.laki.seapp.ui.toys.itemcategories;

import com.mobiwarez.laki.seapp.base.BaseView;
import com.mobiwarez.laki.seapp.base.ScopedPresenter;

/**
 * Created by Laki on 27/10/2017.
 */

public interface ItemCategoryContract {

    interface View {
        void showMessage(String message);

        void showAgeGroups();
    }

    interface Presenter {
        void moveToItemList(String ageGroup, String category);

        void setLocation();

    }
}
