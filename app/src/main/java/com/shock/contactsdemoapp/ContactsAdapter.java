package com.shock.contactsdemoapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shahid on 19/8/16.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    List<Contact> contactList;
    Context context;
    int swipedPosition = -1;
    SwipeListener swipeListener;

    public ContactsAdapter(Context context, List<Contact> contactList, SwipeListener swipeListener) {
        this.context = context;
        this.contactList = contactList;
        this.swipeListener = swipeListener;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        if (swipedPosition != -1 && swipedPosition == position) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.left_to_right_enter_row);
            holder.mView.startAnimation(animation);
            swipedPosition = -1;
        } else {
        }
        holder.tvName.setText(contact.getName());
        if (contact.getPhotoUri() != null && !contact.getPhotoUri().isEmpty()) {
            //loadBitmap(contact.getPhotoUri(), holder.imgContact);
           /* holder.imgContact.setImageURI(null);
            holder.imgContact.setImageURI(Uri.parse(contact.getPhotoUri()));*/
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(Uri.parse(contact.getPhotoUri()));
                holder.tvContactText.setBackground(Drawable.createFromStream(inputStream, contact.getPhotoUri()));
            } catch (FileNotFoundException e) {
                holder.tvContactText.setBackground(ContextCompat.getDrawable(context, R.drawable.circle));
                holder.tvContactText.setText(String.valueOf(contact.getName().charAt(0)).toUpperCase());
            }
            holder.tvContactText.setText("");
        } else {
            // holder.imgContact.setImageResource(R.drawable.contact_placeholder);
            holder.tvContactText.setBackground(ContextCompat.getDrawable(context, R.drawable.circle));
            holder.tvContactText.setText(String.valueOf(contact.getName().charAt(0)).toUpperCase());
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        /*@BindView(R.id.img_contact)
        RoundedImageView imgContact;*/

        @BindView(R.id.tv_contact_text)
        TextView tvContactText;

        @BindView(R.id.tv_name)
        TextView tvName;

        public ContactsViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }

    public static ImageDecodeAsyncTask getImageDecodeAsyncTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getImageDecodeAsyncTask();
            }
        }
        return null;
    }

    private void loadBitmap(String imagePath, ImageView imageView) {
        if (cancelPotentialWork(imagePath, imageView)) {
            final ImageDecodeAsyncTask task = new ImageDecodeAsyncTask(imageView);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(task, context.getResources(), BitmapFactory.decodeResource(context.getResources(), R.drawable.contact_placeholder));
            imageView.setImageDrawable(asyncDrawable);
            task.execute(imagePath);
        }
    }

    private boolean cancelPotentialWork(String imagePath, ImageView imageView) {
        final ImageDecodeAsyncTask imageDecodeAsyncTask = getImageDecodeAsyncTask(imageView);

        if (imageDecodeAsyncTask != null) {
            final String imageDecodeData = imageDecodeAsyncTask.mImagePath;
            if (imageDecodeData == "" || imageDecodeData != imagePath) {
                imageDecodeAsyncTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    public void addToFavourite(int position, RecyclerView.ViewHolder viewHolder) {
        notifyDataSetChanged();
        swipedPosition = position;
        swipeListener.onSwipe(contactList.get(position));
    }
}
