package com.shock.contactsdemoapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shahid on 19/8/16.
 */
public class ContactsFragment extends Fragment implements ContactResult {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "ContactsFragment";
    private static ContactsFragment fragment;
    private ProgressDialog progressDialog;
    MyItemTouchHelper myItemTouchHelper;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public ContactsFragment() {
    }

    public static ContactsFragment newInstance() {
        if (fragment == null)
            fragment = new ContactsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, rootView);
        progressDialog = new ProgressDialog(getContext());
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            runContactAsyncTask();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_CONTACTS)) {

            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, 0);
            }
        }
        myItemTouchHelper = new MyItemTouchHelper(getContext(), recyclerView);
        return rootView;
    }

    public void runContactAsyncTask() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        new ContactAysncTaskLoader(getContext(), this).execute();
    }

    private void setAdapter(List<Contact> contactList) {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        ContactsAdapter contactsAdapter = new ContactsAdapter(getContext(), contactList);
        recyclerView.setAdapter(contactsAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            runContactAsyncTask();
        }
    }

    @Override
    public void onContactCompleted(List<Contact> contactList) {
        setAdapter(contactList);
    }
}
