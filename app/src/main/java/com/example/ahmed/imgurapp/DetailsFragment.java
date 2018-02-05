package com.example.ahmed.imgurapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.imgurapp.Adapters.AlbumAdapter;
import com.example.ahmed.imgurapp.Models.AlbumResponse;
import com.example.ahmed.imgurapp.Models.Photo;
import com.example.ahmed.imgurapp.Network.PhotoApi;
import com.example.ahmed.imgurapp.Network.PhotoClient;
import com.example.ahmed.imgurapp.Util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsFragment extends Fragment {

    Photo photo;
    ArrayList<Photo> photos;
    AlbumAdapter albumAdapter;
    PhotoApi photoApi;

    @BindView(R.id.details_title)
    TextView detailsTitle;
    @BindView(R.id.photo_card_view)
    CardView cardView;
    @BindView(R.id.photo)
    DynamicHeightImageView imageView;
    @BindView(R.id.rv_of_album_photos)
    RecyclerView albumView;
    @BindView(R.id.details_description)
    TextView descriptionText;
    @BindView(R.id.details_tag)
    TextView detailsTag;
    @BindView(R.id.share_fab)
    FloatingActionButton shareFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        photo = Parcels.unwrap(getArguments().getParcelable("photo"));
        photos = new ArrayList<>();

        shareFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(photo.getTitle() + " "
                                + "https://imgur.com/gallery/" + photo.getId())
                        .getIntent(), getString(R.string.action_share)));
            }
        });

        //set title text
        detailsTitle.setText(photo.getTitle());

        //set album or photo source
        if (photo.getIs_album()) {
            cardView.setVisibility(View.GONE);
            albumAdapter = new AlbumAdapter(new ArrayList<Photo>(), getContext());
            albumView.setAdapter(albumAdapter);
            albumView.setLayoutManager(new LinearLayoutManager(getContext()));
            photoApi = PhotoClient.createApi(PhotoClient.buildRetrofit());
            if (savedInstanceState != null && savedInstanceState.containsKey("photos")) {
                photos = Parcels.unwrap(savedInstanceState.getParcelable("photos"));
                albumAdapter.setData(photos);
            } else
                fetchAlbumData();
        } else {
            imageView.setHeightRatio(((double) photo.getHeight()) / photo.getWidth());
            Picasso.with(getContext())
                    .load("https://i.imgur.com/" + photo.getId() + ".jpg")
                    .into(imageView);
        }

        ///set description text
        if (photo.getDescription() != null)
            descriptionText.setText(photo.getDescription());
        else
            descriptionText.setText(R.string.no_description_text);

        //set tags text
        if (photo.getTags().size() > 0) {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < photo.getTags().size() - 1; i++) {
                s.append(photo.getTags().get(i).getName()).append(" - ");
            }
            s.append(photo.getTags().get(photo.getTags().size() - 1).getName());
            s = new StringBuilder(s.toString().replace("_", " "));
            detailsTag.setText(s.toString());
        } else
            detailsTag.setText(R.string.no_tags_text);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!photos.isEmpty())
            outState.putParcelable("photos", Parcels.wrap(photos));
    }

    public void fetchAlbumData() {
        photoApi.getAlbumData(photo.getId()
                , BuildConfig.PHOTO_CLIENT_ID).enqueue(new Callback<AlbumResponse>() {
            @Override
            public void onResponse(@NonNull Call<AlbumResponse> call, @NonNull Response<AlbumResponse> response) {
                Log.v("url", response.raw().request().url().toString());
                if (response.isSuccessful()) {
                    AlbumResponse albumResponse = response.body();
                    if (albumResponse != null) {
                        photos = albumResponse.getData().getImages();
                        albumAdapter.setData(photos);
                    }
                    if (getView() != null)
                        Snackbar.make(getView(), "Data Updated!"
                                , Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AlbumResponse> call, @NonNull Throwable t) {
                if (getView() != null)
                    Snackbar.make(getView(), "Fail to Update Data!"
                            , Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
