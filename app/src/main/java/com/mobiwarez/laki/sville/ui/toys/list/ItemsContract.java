package com.mobiwarez.laki.sville.ui.toys.list;

import com.mobiwarez.laki.sville.base.BaseView;
import com.mobiwarez.laki.sville.base.ScopedPresenter;
import com.mobiwarez.laki.sville.ui.models.ToyViewModel;

import java.util.List;

/**
 * Created by Laki on 27/10/2017.
 */

public class ItemsContract {

    public interface View {

        void showToys(List<ToyViewModel> toyViewModels);

        void showMessage(String message);

        void showNoItems();

        void showLoadingError();

        void showRefreshing();

        void stopRefreshing();
    }

    public interface Presenter {

        void receiveItem(int itemId);

        void fetchItems(String ageGroup, String category);

    }

}
