package com.shock.contactsdemoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 12mm on 19-08-2016.
 */
public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteHolder> {

    List<Contact> contactList;
    Context context;
    List<View> viewList;
    int maxPosition = -1;
    private final int PICK_PHOTO = 1;
    FavouriteFragment favouriteFragment;

    public FavouriteAdapter(Context context, List<Contact> contactList, FavouriteFragment favouriteFragment) {
        this.context = context;
        this.contactList = contactList;
        viewList = new ArrayList<>();
        this.favouriteFragment = favouriteFragment;
    }

    @Override
    public FavouriteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_item, parent, false);
        return new FavouriteHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavouriteHolder holder, final int position) {
        if (maxPosition < position) {
            maxPosition = position;
            viewList.add(holder.rlRoot);
        } else {
            viewList.set(position, holder.rlRoot);
        }
        final Contact contact = contactList.get(position);
        if (contact.getId() <= 0) {
            holder.rlFav.setVisibility(View.GONE);
            holder.rlDetailView.setVisibility(View.GONE);
        } else {
            holder.rlDetailView.setVisibility(View.GONE);
            holder.rlFav.setVisibility(View.VISIBLE);
            if (contact.getPhotoUri() != null && !contact.getPhotoUri().isEmpty()) {
                try {
                    InputStream inputStream = context.getContentResolver().openInputStream(Uri.parse(contact.getPhotoUri()));
                    holder.tvContactText.setBackground(Drawable.createFromStream(inputStream, contact.getPhotoUri()));
                    holder.tvContactText.setText("");
                } catch (FileNotFoundException e) {
                    holder.tvContactText.setBackgroundColor(ContextCompat.getColor(context, R.color.img_bg));
                    holder.tvContactText.setText(String.valueOf(contact.getName().charAt(0)).toUpperCase());
                }
            } else {
                holder.tvContactText.setBackgroundColor(ContextCompat.getColor(context, R.color.img_bg));
                holder.tvContactText.setText(String.valueOf(contact.getName().charAt(0)).toUpperCase());
            }
            holder.tvName.setText(contact.getName());
            holder.imgLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View viewToShow;
                    if (position == 0 || position % 2 == 0) {
                        viewToShow = viewList.get(position + 1);
                    } else {
                        viewToShow = viewList.get(position - 1);
                    }
                    final RelativeLayout rlDetailView = ButterKnife.findById(viewToShow, R.id.rl_detail_view);
                    toggleView(rlDetailView);
                    ImageView imgClose = ButterKnife.findById(viewToShow, R.id.img_close);
                    imgClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toggleView(rlDetailView);
                        }
                    });
                    TextView tvDvName = ButterKnife.findById(viewToShow, R.id.tv_dv_name);
                    tvDvName.setText(contact.getName());
                    TextView tvDvPhoneNumber = ButterKnife.findById(viewToShow, R.id.tv_dv_phone_number);
                    tvDvPhoneNumber.setText(contact.getPhoneNumbers().get(0).getPhoneNumber());
                }
            });
            holder.tvContactText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    View alertView = LayoutInflater.from(context).inflate(R.layout.dialog_chooser, null);
                    final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setView(alertView);
                    final AlertDialog alertDialog = alertBuilder.create();

                    ImageView galleryImageView = ButterKnife.findById(alertView, R.id.gallery);
                    galleryImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            if (favouriteFragment.requestPermisssionForExternalStorage()) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                favouriteFragment.saveContact(contact);
                                favouriteFragment.getActivity().startActivityForResult(intent, PICK_PHOTO);
                            } else {
                                favouriteFragment.StorageMsg();
                            }
                        }
                    });
                    ImageView facebookImageView = ButterKnife.findById(alertView, R.id.facebook);
                    facebookImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            favouriteFragment.showMsgImageviewClick("FaceBook");
                        }
                    });
                    ImageView selfieImageView = ButterKnife.findById(alertView, R.id.selfie);
                    selfieImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            favouriteFragment.showMsgImageviewClick("Selfie");
                        }
                    });
                    ImageView velfieImageView = ButterKnife.findById(alertView, R.id.velfie);
                    velfieImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            favouriteFragment.showMsgImageviewClick("Velfie");
                        }
                    });
                    ImageView closeImageView = ButterKnife.findById(alertView, R.id.img_close);
                    closeImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void toggleView(final View view) {
        Animation animation = AnimationUtils.loadAnimation(context,
                view.getVisibility() == View.VISIBLE ? R.anim.slide_down : R.anim.slide_up);
        view.startAnimation(animation);
        if (view.getVisibility() == View.VISIBLE) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.GONE);
                }
            }, animation.getDuration());
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public class FavouriteHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_root)
        RelativeLayout rlRoot;

        @BindView(R.id.rl_fav)
        RelativeLayout rlFav;

        @BindView(R.id.rl_detail_view)
        RelativeLayout rlDetailView;

        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.img_like)
        ImageView imgLike;

        @BindView(R.id.tv_contact_text)
        TextView tvContactText;

        public FavouriteHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
