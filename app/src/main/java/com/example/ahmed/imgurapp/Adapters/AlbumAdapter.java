package com.example.ahmed.imgurapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.imgurapp.Models.Photo;
import com.example.ahmed.imgurapp.R;
import com.example.ahmed.imgurapp.Util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.albumVH> {

    private ArrayList<Photo> photos;
    private Context context;

    public AlbumAdapter(ArrayList<Photo> photos, Context context) {
        this.photos = photos;
        this.context = context;
    }

    @Override
    public albumVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_album, parent, false);
        return new albumVH(view);
    }

    @Override
    public void onBindViewHolder(albumVH holder, int position) {
        Photo photo = photos.get(position);
        holder.photo.setHeightRatio(((double) photo.getHeight()) / photo.getWidth());
        Picasso.with(context)
                .load("https://i.imgur.com/" + photo.getId() + ".jpg")
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

    class albumVH extends RecyclerView.ViewHolder {
        @BindView(R.id.photo)
        DynamicHeightImageView photo;

        albumVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
