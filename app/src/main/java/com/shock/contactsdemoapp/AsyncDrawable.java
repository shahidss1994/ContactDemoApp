package com.shock.contactsdemoapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * Created by Kunal S on 8/16/2016.
 */
public class AsyncDrawable extends BitmapDrawable {
    private final WeakReference<ImageDecodeAsyncTask> mImageDecodeAsyncTaskWeakReference;

    public AsyncDrawable(ImageDecodeAsyncTask imageDecodeAsyncTask, Resources res, Bitmap bitmap) {
        super(res,bitmap);
        mImageDecodeAsyncTaskWeakReference = new WeakReference<>(imageDecodeAsyncTask);
    }

    public ImageDecodeAsyncTask getImageDecodeAsyncTask(){
        return mImageDecodeAsyncTaskWeakReference.get();
    }
}
