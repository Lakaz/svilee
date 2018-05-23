package com.mobiwarez.laki.sville.connections;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Laki on 12/07/2017.
 */

public class ConnectionsDBhelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "connections.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ConnectionsPersistenceContract.ConnectionEntry.TABLE_NAME + " (" +
                    ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                    ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_CONNECTION_ID + TEXT_TYPE + COMMA_SEP +
                    ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_DISPLAY_NAME + TEXT_TYPE + COMMA_SEP +
                    ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                    ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_CHATROOM + TEXT_TYPE + COMMA_SEP +
                    ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_PHOTO_URL + TEXT_TYPE +
                    " )";

    public ConnectionsDBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
