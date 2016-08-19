package com.shock.contactsdemoapp;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.List;

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

    public boolean insertFavourite(Contact contact) {
        try {
            if (databaseHelper.getContactDao().queryForEq("id", contact.getId()).size() == 0) {
                Dao.CreateOrUpdateStatus createOrUpdateStatus = databaseHelper.getContactDao().createOrUpdate(contact);
                for (PhoneNumber phoneNumber : contact.getPhoneNumbers()) {
                    databaseHelper.getPhoneNumberDao().createOrUpdate(phoneNumber);
                }
                return createOrUpdateStatus.isCreated() == false ? createOrUpdateStatus.isUpdated() : createOrUpdateStatus.isCreated();
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Contact> getFavouriteContactList() {
        try {
            List<Contact> contactList = databaseHelper.getContactDao().queryForAll();
            for (Contact contact : contactList) {
                contact.setPhoneNumbers(getFavouritePhoneNumber(contact.getId()));
            }
            return contactList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PhoneNumber> getFavouritePhoneNumber(int favouriteId) {
        try {
            return databaseHelper.getPhoneNumberDao().queryForEq("favourite_id", favouriteId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updatePhotoUri(int id, String uri) {
        try {
            UpdateBuilder<Contact, Integer> updateBuilder = databaseHelper.getContactDao().updateBuilder();
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("photo_uri", uri);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
