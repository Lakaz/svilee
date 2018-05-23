package com.mobiwarez.laki.sville.connections;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

//import com.example.laki.myapplication.backend.myApi.model.Zache;
//import com.mobiwarez.sache.models.Connection;
//import com.mobiwarez.sache.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

//import com.google.appengine.repackaged.com.google.io.protocol.proto.RPC_ServiceDescriptor;

/**
 * Created by Laki on 12/07/2017.
 */

public class ConnectionsLocalDataSource {

    private static ConnectionsLocalDataSource INSTANCE;

    private ConnectionsDBhelper mDbHelper;

    private Context context;

    // Prevent direct instantiation.
    private ConnectionsLocalDataSource(@NonNull Context context) {
        //checkNotNull(context);
        this.context = context;
        mDbHelper = new ConnectionsDBhelper(context);
    }

    public static ConnectionsLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ConnectionsLocalDataSource(context);
        }
        return INSTANCE;
    }

    public void saveConnection(Zache zache, String chatroom) {
        checkNotNull(zache);

        if (getConnetion(zache.getUserEmail()) == null) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_CONNECTION_ID, "llll");
            values.put(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_DISPLAY_NAME, zache.getUserName());
            values.put(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_EMAIL, zache.getUserEmail());
            values.put(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_PHOTO_URL, zache.getAvatarUrl());
            values.put(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_CHATROOM, chatroom);

            db.insert(ConnectionsPersistenceContract.ConnectionEntry.TABLE_NAME, null, values);

            db.close();
        }


    }

    public String getChatRoom(String connectionEmail) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_CONNECTION_ID,
                ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_CHATROOM,
                ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_DISPLAY_NAME,
                ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_EMAIL,
                ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_PHOTO_URL
        };

        String selection = ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_EMAIL + " LIKE ?";
        String[] selectionArgs = { connectionEmail };

        Cursor c = db.query(ConnectionsPersistenceContract.ConnectionEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        Zache zache = null;

        String chatroom = null;

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();

            String connectionId = c.getString(c.getColumnIndexOrThrow(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_CONNECTION_ID));
            chatroom = c.getString(c.getColumnIndexOrThrow(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_CHATROOM));
            String displayName = c.getString(c.getColumnIndexOrThrow(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_DISPLAY_NAME));
            String email = c.getString(c.getColumnIndexOrThrow(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_EMAIL));
            String photoUrl = c.getString(c.getColumnIndexOrThrow(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_PHOTO_URL));

/*
            zache.setUserEmail(email);
            if(photoUrl != null) {
                zache.setAvatarUrl(photoUrl);
            }

            else {
                zache.setAvatarUrl("no_photo");
            }
            zache.setUserName(displayName);
*/

        }
        if (c != null) {
            c.close();
        }

        db.close();

        return chatroom;

    }

    public Zache getConnetion(String connectionEmail) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_CONNECTION_ID,
                ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_CHATROOM,
                ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_DISPLAY_NAME,
                ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_EMAIL,
                ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_PHOTO_URL
        };

        String selection = ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_EMAIL + " LIKE ?";
        String[] selectionArgs = { connectionEmail };

        Cursor c = db.query(ConnectionsPersistenceContract.ConnectionEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        Zache zache = null;

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();

            String connectionId = c.getString(c.getColumnIndexOrThrow(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_CONNECTION_ID));
            String chatroom = c.getString(c.getColumnIndexOrThrow(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_CHATROOM));
            String displayName = c.getString(c.getColumnIndexOrThrow(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_DISPLAY_NAME));
            String email = c.getString(c.getColumnIndexOrThrow(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_EMAIL));
            String photoUrl = c.getString(c.getColumnIndexOrThrow(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_PHOTO_URL));

            zache.setUserEmail(email);
            zache.setAvatarUrl(photoUrl);
            zache.setUserName(displayName);

        }
        if (c != null) {
            c.close();
        }

        db.close();

        return zache;
    }


    public List<Connection> getAllConnections() {
        List<Connection> connections = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();


        String[] projection = {
                ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_CONNECTION_ID,
                ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_CHATROOM,
                ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_DISPLAY_NAME,
                ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_EMAIL,
                ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_PHOTO_URL
        };



        Cursor c = db.query(ConnectionsPersistenceContract.ConnectionEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                connections.add(createConnection(c));
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        return connections;
    }


    private Connection createConnection(Cursor c) {
        String connectionId = c.getString(c.getColumnIndexOrThrow(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_CONNECTION_ID));
        String chatroom = c.getString(c.getColumnIndexOrThrow(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_CHATROOM));
        String displayName = c.getString(c.getColumnIndexOrThrow(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_DISPLAY_NAME));
        String email = c.getString(c.getColumnIndexOrThrow(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_EMAIL));
        String photoUrl = c.getString(c.getColumnIndexOrThrow(ConnectionsPersistenceContract.ConnectionEntry.COLUMN_NAME_PHOTO_URL));

        Connection connection = new Connection(displayName);
        connection.setAvatarUrl(photoUrl);
        connection.setChatRoom(chatroom);
        connection.setEmailAddress(email);

        return connection;

    }

}
