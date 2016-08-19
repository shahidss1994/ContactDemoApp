package com.shock.contactsdemoapp;

import android.view.View;

import java.io.Serializable;

/**
 * Created by 12mm on 19-08-2016.
 */
public class DetailView implements Serializable {

    private boolean isOpen;
    private View view;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
