package com.shock.contactsdemoapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shahid on 19/8/16.
 */
public class ContactsFragment extends Fragment implements ContactResultListener, SwipeListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "ContactsFragment";
    private static ContactsFragment fragment;
    private ProgressDialog progressDialog;
    MyItemTouchHelper myItemTouchHelper;
    DBManager dbManager;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.root)
    RelativeLayout root;

    private LinearLayoutManager linearLayoutManager;

    public ContactsFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dbManager = DBManager.getInstance(context);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        dbManager = DBManager.getInstance(getContext());
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
        recyclerView.setLayoutManager(linearLayoutManager);
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
        ContactsAdapter contactsAdapter = new ContactsAdapter(getContext(), contactList, this);
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


    @Override
    public void onSwipe(Contact contact) {
        boolean isInserted = dbManager.insertFavourite(contact);
        if (isInserted) {
            Intent intent = new Intent("update_favourite");
            intent.putExtra("contact_data", contact);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            Log.d(TAG, "onSwipe: " + contact.getId() + " -> " + contact.getPhoneNumbers().get(0));
            Snackbar.make(root, "Contact added to your favourite", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(root, "Contact already exists in your favourite", Snackbar.LENGTH_SHORT).show();
        }
    }
}
