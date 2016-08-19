package com.shock.contactsdemoapp;

/**
 * Created by 12mm on 19-08-2016.
 */

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {
    /*private final int PICK_PHOTO = 1;

    Bitmap mBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Defining OnClick listener for the photo
        OnClickListener photoClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image*//**//*");
                startActivityForResult(intent, PICK_PHOTO);
            }
        };

        // Defining OnClick listener for the Add Contact Button
        OnClickListener addClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {

                // Getting reference to Name EditText
                EditText etName = (EditText) findViewById(R.id.et_name);

                // Getting reference to Mobile EditText
                EditText etMobile = (EditText) findViewById(R.id.et_mobile);

                ArrayList<ContentProviderOperation> ops =
                        new ArrayList<ContentProviderOperation>();

                int rawContactID = ops.size();

                // Adding insert operation to operations list
                // to insert a new raw contact in the table ContactsContract.RawContacts
                ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(RawContacts.ACCOUNT_NAME, null)
                        .build());

                // Adding insert operation to operations list
                // to insert display name in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(StructuredName.DISPLAY_NAME, etName.getText().toString())
                        .build());

                // Adding insert operation to operations list
                // to insert Mobile Number in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                        .withValue(Phone.NUMBER, etMobile.getText().toString())
                        .withValue(Phone.TYPE, CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (mBitmap != null) {    // If an image is selected successfully
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 75, stream);

                    // Adding insert operation to operations list
                    // to insert Photo in the table ContactsContract.Data
                    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                            .withValue(ContactsContract.Data.IS_SUPER_PRIMARY, 1)
                            .withValue(ContactsContract.Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, stream.toByteArray())
                            .build());

                    try {
                        stream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    // Executing all the insert operations as a single database transaction
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                    Toast.makeText(getBaseContext(), "Contact is successfully added", Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
                }
            }
        };

        // Creating a button click listener for the "Add Contact" button
        OnClickListener contactsClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Creating an intent to open Android's Contacts List
                Intent contacts = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);

                // Starting the activity
                startActivity(contacts);
            }
        };

        // Getting reference to ImageView
        ImageButton ibPhoto = (ImageButton) findViewById(R.id.ib_photo);

        // Getting Reference to Add Contact Button
        Button btnAdd = (Button) findViewById(R.id.btn_add);

        // Getting Reference to Contact List Button
        Button btnList = (Button) findViewById(R.id.btn_list);

        // Setting OnClick Listener for the photo
        ibPhoto.setOnClickListener(photoClickListener);

        // Setting OnClick Listener of the Add Contact button
        btnAdd.setOnClickListener(addClickListener);

        // Setting OnClick Listener for the Contacts List button
        btnList.setOnClickListener(contactsClickListener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    // Getting the uri of the picked photo
                    Uri selectedImage = data.getData();

                    InputStream imageStream = null;
                    try {
                        // Getting InputStream of the selected image
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    // Creating bitmap of the selected image from its inputstream
                    mBitmap = BitmapFactory.decodeStream(imageStream);

                    // Getting reference to ImageView
                    ImageButton ibPhoto = (ImageButton) findViewById(R.id.ib_photo);

                    // Setting Bitmap to ImageButton
                    ibPhoto.setImageBitmap(mBitmap);
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }*/
}