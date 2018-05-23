package com.mobiwarez.laki.sville.ui.contacts;

import com.mobiwarez.data.contacts.db.model.ContactModel;

import java.util.List;

/**
 * Created by Laki on 04/11/2017.
 */

public class ContactsContract {

    interface View {

        void showContacts(List<ContactModel> contactModels);

        void showMessage(String message);

        void showError(String message);
    }

    interface Presenter {

    }
}
