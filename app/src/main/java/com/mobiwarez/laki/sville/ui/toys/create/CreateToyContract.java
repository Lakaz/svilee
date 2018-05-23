package com.mobiwarez.laki.sville.ui.toys.create;

import android.net.Uri;

import com.mobiwarez.laki.sville.base.BasePresenter;
import com.mobiwarez.laki.sville.base.BaseView;
import com.mobiwarez.laki.sville.base.ScopedPresenter;

/**
 * Created by Laki on 25/10/2017.
 */

public interface CreateToyContract {

    interface View {

        void showPhoto(Uri uri);

        void showPresentLocation(String address);

        void showAddresses();

        void showMessages(String message);

        void showCategories();

        void showAgeGroups();
    }

    interface Presenter {

        void loadPhotoFromLibraray();

        void takePictureFromCamera();

        void addNewToy();

        void acquireLocation();

        void acquireAdresses();
    }
}
