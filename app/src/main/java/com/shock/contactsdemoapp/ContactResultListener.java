package com.shock.contactsdemoapp;

import java.util.List;

/**
 * Created by shahid on 19/8/16.
 */
public interface ContactResultListener {
    public void onContactCompleted(List<Contact> contactList);
}
