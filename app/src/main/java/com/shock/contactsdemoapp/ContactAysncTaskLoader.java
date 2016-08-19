package com.shock.contactsdemoapp;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by shahid on 19/8/16.
 */
public class ContactAysncTaskLoader extends AsyncTask<Void, Void, List<Contact>> {

    Context context;
    ContactResultListener contactResultListener;

    public ContactAysncTaskLoader(Context context, ContactResultListener contactResultListener) {
        this.context = context;
        this.contactResultListener = contactResultListener;
    }

    private static final String TAG = ContactAysncTaskLoader.class.getSimpleName();

    @Override
    protected List<Contact> doInBackground(Void... voids) {
        List<Contact> contactList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                Contact contact = new Contact();
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                contact.setId(Integer.parseInt(id));

                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contact.setName(name);
                String photoUri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                contact.setPhotoUri(photoUri);
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    List<PhoneNumber> phoneNumbers = new ArrayList<>();
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        PhoneNumber phoneNumber = new PhoneNumber();
                        phoneNumber.setRawContactId((int) pCur.getLong(pCur.getColumnIndex(ContactsContract.RawContacts._ID)));
                        phoneNumber.setFavouriteId(Integer.parseInt(id));
                        phoneNumber.setPhoneNumber(phoneNo);
                        phoneNumbers.add(phoneNumber);
                    }
                    contact.setPhoneNumbers(phoneNumbers);
                    pCur.close();
                } else {
                    continue;
                }
                contactList.add(contact);
            }
        }
        return contactList;
    }

    @Override
    protected void onPostExecute(List<Contact> contacts) {
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact contact, Contact t1) {
                return contact.getName().compareTo(t1.getName());
            }
        });
        contactResultListener.onContactCompleted(contacts);
    }
}
