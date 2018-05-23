package com.mobiwarez.laki.sville.connections;

import android.provider.BaseColumns;

/**
 * Created by Laki on 12/07/2017.
 */

public class ConnectionsPersistenceContract {

    private ConnectionsPersistenceContract() {}

    /* Inner class that defines the table contents */
    public static abstract class ConnectionEntry implements BaseColumns {
        public static final String TABLE_NAME = "connections";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_CONNECTION_ID = "connectionid";
        public static final String COLUMN_NAME_PHOTO_URL = "photo";
        public static final String COLUMN_NAME_DISPLAY_NAME = "displayname";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_CHATROOM = "chatroom";
    }

}
