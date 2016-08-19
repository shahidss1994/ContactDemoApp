package com.shock.contactsdemoapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shahid on 19/8/16.
 */
public class FavouriteFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private FavouriteFragment fragment;
    DBManager dbManager;
    private GridLayoutManager gridLayoutManager;
    List<Contact> contactList;
    private FavouriteAdapter favouriteAdapter;
    private final int PICK_PHOTO = 1;
    Contact contact;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.root)
    RelativeLayout root;

    public FavouriteFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dbManager = DBManager.getInstance(context);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        contactList = new ArrayList<>();
        LocalBroadcastManager.getInstance(context).registerReceiver(updateFavouriteContacts, new IntentFilter("update_favourite"));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(updateFavouriteContacts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, rootView);
        recyclerView.setLayoutManager(gridLayoutManager);
        new AsyncFavouriteContact().execute();
        return rootView;
    }

    public void setRecyclerView(List<Contact> contactList) {
        this.contactList.clear();
        this.contactList.addAll(contactList);
        sort(true);
        if (this.contactList != null && this.contactList.size() > 0) {
            if (this.contactList.size() == 1 || this.contactList.size() % 2 == 1) {
                Contact contact = new Contact();
                this.contactList.add(contact);
            }
            if (favouriteAdapter == null) {
                favouriteAdapter = new FavouriteAdapter(getContext(), this.contactList, this);
                recyclerView.setAdapter(favouriteAdapter);
            } else {
                favouriteAdapter.notifyDataSetChanged();
            }
        } else {

        }
    }

    public void saveContact(Contact contact) {
        this.contact = contact;
    }

    public void StorageMsg() {
        Snackbar.make(root, "Please provide storage permission from settings", Snackbar.LENGTH_SHORT).show();
    }

    public class AsyncFavouriteContact extends AsyncTask<Void, Void, List<Contact>> {

        @Override
        protected List<Contact> doInBackground(Void... voids) {
            return dbManager.getFavouriteContactList();
        }

        @Override
        protected void onPostExecute(List<Contact> contactList) {
            super.onPostExecute(contactList);
            if (contactList != null && contactList.size() > 0) {
                setRecyclerView(contactList);
            }
        }
    }

    BroadcastReceiver updateFavouriteContacts = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Contact contact = (Contact) intent.getSerializableExtra("contact_data");
            if (contactList.size() == 0) {
                contactList.add(contact);
                sort(true);
                Contact contact1 = new Contact();
                contactList.add(contact1);
            } else {
                if (contactList.get(contactList.size() - 1).getId() <= 0) {
                    contactList.remove(contactList.size() - 1);
                    contactList.add(contact);
                    sort(true);
                } else {
                    contactList.add(contact);
                    sort(true);
                    Contact contact1 = new Contact();
                    contactList.add(contact1);
                }
            }
            setNewData();
        }
    };

    public void sort(boolean asc) {
        Collections.sort(contactList, new Comparator<Contact>() {
            @Override
            public int compare(Contact contact, Contact t1) {
                return contact.getName().compareTo(t1.getName());
            }
        });
    }

    public void setNewData() {
        favouriteAdapter = new FavouriteAdapter(getContext(), contactList, this);
        recyclerView.setAdapter(favouriteAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_PHOTO:
                if (resultCode == getActivity().RESULT_OK) {
                    // Getting the uri of the picked photo
                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Setting picture please wait...");
                    progressDialog.show();
                    Uri selectedImage = data.getData();
                    final int rawContactId = contact.getPhoneNumbers().get(0).getRawContactId();
                    int contactId = contact.getId();

                    InputStream imageStream = null;
                    Bitmap mBitmap = null;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    try {
                        // Getting InputStream of the selected image
                        imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                        mBitmap = BitmapFactory.decodeStream(imageStream);
                        if (mBitmap != null) {    // If an image is selected successfully
                            mBitmap.compress(Bitmap.CompressFormat.PNG, 75, stream);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    ArrayList<ContentProviderOperation> ops = new ArrayList<>();

                    /*ops.add(ContentProviderOperation.newUpdate(ContactsContract.Contacts.CONTENT_URI)
                            .withSelection(ContactsContract.Contacts._ID + "=?", new String[]{String.valueOf(contactId)})
                            .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, stream.toByteArray())
                            .withValue(ContactsContract.CommonDataKinds.Phone.PHOTO_URI, "content://com.android.contacts/contacts/" + contactId + "/photo")
                            .build());*/

                    ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                            .withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=?", new String[]{String.valueOf(rawContactId)})
                            .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, stream.toByteArray())
                            .withValue(ContactsContract.CommonDataKinds.Phone.PHOTO_URI, "content://com.android.contacts/contacts/" + contactId + "/photo")
                            .build());

                    try {
                        ContentProviderResult[] contentResolver = getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                        dbManager.updatePhotoUri(contactId, String.valueOf(selectedImage));
                        new AsyncFavouriteContact().execute();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (OperationApplicationException e) {
                        e.printStackTrace();
                    }
                    progressDialog.hide();
                    Snackbar.make(root, "Picture added to your contact", Snackbar.LENGTH_SHORT).show();
                }
        }
    }

    public void writeDisplayPhoto(long rawContactId, byte[] photo) {
        Uri rawContactPhotoUri = Uri.withAppendedPath(
                ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, rawContactId),
                ContactsContract.RawContacts.DisplayPhoto.CONTENT_DIRECTORY);
        try {
            AssetFileDescriptor fd =
                    getActivity().getContentResolver().openAssetFileDescriptor(rawContactPhotoUri, "rw");
            OutputStream os = fd.createOutputStream();
            os.write(photo);
            os.close();
            fd.close();
        } catch (IOException e) {
            // Handle error cases.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

        }
    }

    public boolean requestPermisssionForExternalStorage() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_CONTACTS}, 1);
            }
            return false;
        }
    }

    public void showMsgImageviewClick(String msg) {
        Snackbar.make(root, msg, Snackbar.LENGTH_SHORT).show();
    }
}
