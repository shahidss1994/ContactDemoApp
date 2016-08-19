package com.shock.contactsdemoapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by Kunal S on 8/11/2016.
 */
public class ImageDecodeAsyncTask extends AsyncTask<String,Void,Bitmap> {

    private final WeakReference<ImageView> mImageViewWeakReference;
    public String mImagePath;

    public ImageDecodeAsyncTask(ImageView imageView){
        mImageViewWeakReference= new WeakReference<>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        mImagePath=strings[0];
        Bitmap image= BitmapFactory.decodeFile(strings[0]);
        Bitmap thumbnail= ThumbnailUtils.extractThumbnail(image,150,150);
        return thumbnail;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(isCancelled()){
            bitmap=null;
        }
        if(mImageViewWeakReference!=null && bitmap!=null){
            final ImageView imageView=mImageViewWeakReference.get();
            final ImageDecodeAsyncTask imageDecodeAsyncTask= ContactsAdapter.getImageDecodeAsyncTask(imageView);
            if(this== imageDecodeAsyncTask||imageView!=null){
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
