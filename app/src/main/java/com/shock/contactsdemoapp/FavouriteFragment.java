package com.shock.contactsdemoapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by shahid on 19/8/16.
 */
public class FavouriteFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static FavouriteFragment fragment;

    public FavouriteFragment() {
    }

    public static FavouriteFragment newInstance() {
        if (fragment == null)
            fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        return rootView;
    }
}
