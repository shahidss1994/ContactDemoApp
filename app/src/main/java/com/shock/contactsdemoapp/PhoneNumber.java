package com.shock.contactsdemoapp;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by shahid on 19/8/16.
 */
public class PhoneNumber {

    @DatabaseField(id = true)
    private int id;

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
}
