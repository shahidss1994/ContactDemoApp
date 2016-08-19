package com.shock.contactsdemoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by shahid on 19/8/16.
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "demo.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    private Dao<Contact, Integer> contactDao = null;
    private Dao<PhoneNumber, Integer> phoneNumberDao = null;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Contact.class);
            TableUtils.createTable(connectionSource, PhoneNumber.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Contact.class, true);
            TableUtils.dropTable(connectionSource, PhoneNumber.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<PhoneNumber, Integer> getPhoneNumberDao() throws SQLException {
        if (phoneNumberDao == null) {
            phoneNumberDao = getDao(PhoneNumber.class);
        }
        return phoneNumberDao;
    }

    public Dao<Contact, Integer> getContactDao() throws SQLException {
        if (contactDao == null)
            contactDao = getDao(Contact.class);
        return contactDao;
    }

    @Override
    public void close() {
        super.close();
        contactDao = null;
        phoneNumberDao = null;
    }
}
