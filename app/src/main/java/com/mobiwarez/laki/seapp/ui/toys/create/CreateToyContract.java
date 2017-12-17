package com.mobiwarez.laki.seapp.ui.toys.create;

import android.net.Uri;

import com.mobiwarez.laki.seapp.base.BasePresenter;
import com.mobiwarez.laki.seapp.base.BaseView;
import com.mobiwarez.laki.seapp.base.ScopedPresenter;

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
