package com.example.ahmed.imgurapp.Adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.imgurapp.DetailsActivity;
import com.example.ahmed.imgurapp.Models.Photo;
import com.example.ahmed.imgurapp.R;
import com.example.ahmed.imgurapp.Util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoVH> {

    private ArrayList<Photo> photos;
    private Context context;

    public PhotoAdapter(ArrayList<Photo> photos, Context context) {
        this.photos = photos;
        this.context = context;
    }

    @Override
    public PhotoVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_photo, parent, false);
        return new PhotoVH(view);
    }

    @Override
    public void onBindViewHolder(final PhotoVH holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putParcelable("photo", Parcels.wrap(photos.get(holder.getAdapterPosition())));
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
        Photo photo = photos.get(position);
        holder.title.setText(photos.get(position).getTitle());
        holder.photo.setHeightRatio(((double) photo.getHeight()) / photo.getWidth());
        Picasso.with(context)
                .load("https://i.imgur.com/" + photo.getCover() + "h.jpg")
                .into(holder.photo);
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
