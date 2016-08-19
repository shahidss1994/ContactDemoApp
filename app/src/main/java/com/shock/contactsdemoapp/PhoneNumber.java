package com.shock.contactsdemoapp;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by shahid on 19/8/16.
 */
@DatabaseTable(tableName = "phone_number")
public class PhoneNumber {

    @DatabaseField(columnName = "id", generatedId = true)
    private int id;

    @DatabaseField(columnName = "raw_contact_id")
    private int rawContactId;

    @DatabaseField(columnName = "favourite_id")
    private int favouriteId;

    @DatabaseField(columnName = "phone_number")
    private String phoneNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFavouriteId() {
        return favouriteId;
    }

    public void setFavouriteId(int favouriteId) {
        this.favouriteId = favouriteId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRawContactId() {
        return rawContactId;
    }

    public void setRawContactId(int rawContactId) {
        this.rawContactId = rawContactId;
    }
}
