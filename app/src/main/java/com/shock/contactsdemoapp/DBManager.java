package com.shock.contactsdemoapp;

import android.content.Context;

import java.sql.SQLException;

/**
 * Created by shahid on 19/8/16.
 */
public class DBManager {

    private static DBManager dbManager;
    private final DataBaseHelper databaseHelper;

    private DBManager(Context context) {
        databaseHelper = new DataBaseHelper(context);
    }

    public static synchronized DBManager getInstance(Context context) {

        if (dbManager == null) {
            dbManager = new DBManager(context);
        }
        return dbManager;
    }

    public void insertFavourite(Contact contact) {
        try {
            databaseHelper.getContactDao().createOrUpdate(contact);
            for (PhoneNumber phoneNumber : contact.getPhoneNumbers()) {
                databaseHelper.getPhoneNumberDao().createOrUpdate(phoneNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
