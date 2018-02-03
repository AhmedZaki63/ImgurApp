package com.example.ahmed.imgurapp;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.imgurapp.Models.Photo;
import com.example.ahmed.imgurapp.Util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoVH> {

    private ArrayList<Photo> photos;
    private Context context;

    PhotoAdapter(ArrayList<Photo> photos, Context context) {
        this.photos = photos;
        this.context = context;
    }

    @Override
    public PhotoVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.photo_item, parent, false);
        return new PhotoVH(view);
    }

    @Override
    public void onBindViewHolder(PhotoVH holder, int position) {
        Photo photo = photos.get(position);
        holder.title.setText(photos.get(position).getTitle());
        if (photo.getIs_album()) {
            holder.photo.setHeightRatio(((double) photo.getCover_height()) / photo.getCover_width());
            Picasso.with(context)
                    .load("https://i.imgur.com/" + photo.getCover() + "h.jpg")
                    .into(holder.photo);
        } else {
            holder.photo.setHeightRatio(((double) photo.getHeight()) / photo.getWidth());
            Picasso.with(context)
                    .load("https://i.imgur.com/" + photo.getId() + "h.jpg")
                    .into(holder.photo);
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setData(ArrayList<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    void addData(ArrayList<Photo> photos) {
        this.photos.addAll(photos);
        notifyDataSetChanged();
    }

    class PhotoVH extends RecyclerView.ViewHolder {
        @BindView(R.id.photo)
        DynamicHeightImageView photo;
        @BindView(R.id.title)
        TextView title;

        PhotoVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
