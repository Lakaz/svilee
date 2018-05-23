package com.mobiwarez.laki.sville.ui.toys.sharedtoys;

import com.mobiwarez.data.givenToys.db.model.GivenToyModel;
import com.mobiwarez.laki.sville.base.BaseView;
import com.mobiwarez.laki.sville.base.ScopedPresenter;
import com.mobiwarez.laki.sville.ui.models.ToyViewModel;

import java.util.List;

/**
 * Created by Laki on 26/10/2017.
 */

public interface SharedToysContract {

    interface View {

        void showItems(List<GivenToyModel> toyViewModels);

        void showLoading();

        void setItemStatus();

        void showMessage(String message);

        void showUpdateButton();

        void showEditCategory();

        void showEditAgeGroup();

        void showEditDescription();

    }


    interface Presenter {

        void loadItems();

        void deleteItem();

        void createNewItem();

        void updateItem();

        void editCategory();

        void editAgeGroup();

        void editImage();
    }
}
