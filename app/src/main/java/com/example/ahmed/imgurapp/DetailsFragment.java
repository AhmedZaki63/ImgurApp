package com.example.ahmed.imgurapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.imgurapp.Adapters.AlbumAdapter;
import com.example.ahmed.imgurapp.Database.PhotoDbHelper;
import com.example.ahmed.imgurapp.Models.AlbumResponse;
import com.example.ahmed.imgurapp.Models.Photo;
import com.example.ahmed.imgurapp.Network.OnLoadFinished;
import com.example.ahmed.imgurapp.Network.PhotoAsyncTask;
import com.example.ahmed.imgurapp.Util.DynamicHeightImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.crash.FirebaseCrash;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsFragment extends Fragment {

    Photo photo;
    ArrayList<Photo> photos;
    AlbumAdapter albumAdapter;
    PhotoDbHelper photoDbHelper;

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
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.adView)
    AdView adView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        //setup toolbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);

        photo = Parcels.unwrap(getArguments().getParcelable("photo"));
        if (photos == null)
            photos = new ArrayList<>();

        photoDbHelper = new PhotoDbHelper(getContext());

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

            if (photo.getImages_count() <= photo.getImages().size())
                photos = photo.getImages();

            albumAdapter.setData(photos);

            if (photos.isEmpty())
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.details, menu);
        if (photoDbHelper.isSaved(photo))
            menu.getItem(0).setIcon(R.drawable.ic_favorite);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_favourite:
                favourite(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fetchAlbumData() {
        new PhotoAsyncTask(new OnLoadFinished() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(Object response) {
                AlbumResponse albumResponse = (AlbumResponse) response;
                if (albumResponse != null) {
                    photos = albumResponse.getData().getImages();
                    albumAdapter.setData(photos);
                    if (getView() != null)
                        Snackbar.make(getView(), R.string.response_text
                                , Snackbar.LENGTH_SHORT).show();
                } else {
                    if (getView() != null)
                        Snackbar.make(getView(), R.string.failure_text
                                , Snackbar.LENGTH_LONG).show();
                    FirebaseCrash.log(getResources().getString(R.string.failure_text));
                }
            }
        }).execute(photo.getId());
    }

    public void favourite(MenuItem item) {
        if (photoDbHelper.isSaved(photo)) {
            item.setIcon(R.drawable.ic_favorite_border);
            photoDbHelper.removeFromDatabase(photo);
        } else {
            item.setIcon(R.drawable.ic_favorite);
            photoDbHelper.addToDatabase(photo, photos);
        }
    }
}
