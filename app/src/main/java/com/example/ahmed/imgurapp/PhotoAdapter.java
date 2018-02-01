package com.example.ahmed.imgurapp;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.imgurapp.Models.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
            Picasso.with(context)
                    .load("https://i.imgur.com/" + photo.getCover() + "h.jpg")
                    .into(holder.photo);
        } else {
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

    class PhotoVH extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;

        PhotoVH(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            title = itemView.findViewById(R.id.title);
        }
    }
}
