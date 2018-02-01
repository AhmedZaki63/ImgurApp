package com.example.ahmed.imgurapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.example.ahmed.imgurapp.Models.Photo;
import com.example.ahmed.imgurapp.Models.PhotoResponse;
import com.example.ahmed.imgurapp.Network.PhotoApi;
import com.example.ahmed.imgurapp.Network.PhotoClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    ArrayList<Photo> photos;
    PhotoAdapter photoAdapter;
    PhotoApi photoApi;

    @BindView(R.id.rv_of_photos)
    RecyclerView photoView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        photos = new ArrayList<>();
        photoAdapter = new PhotoAdapter(photos, this);
        photoView.setAdapter(photoAdapter);
        photoView.setLayoutManager(new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL));

        photoApi = PhotoClient.createApi(PhotoClient.buildRetrofit());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPhotosData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        fetchPhotosData();
    }

    public void fetchPhotosData() {
        photoApi.getPhotosData("hot", "viral", "0"
                , BuildConfig.PHOTO_CLIENT_ID).enqueue(new retrofit2.Callback<PhotoResponse>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<PhotoResponse> call
                    , @NonNull retrofit2.Response<PhotoResponse> response) {
                Log.v("url", response.raw().request().url().toString());
                if (response.isSuccessful()) {
                    PhotoResponse photoResponse = response.body();
                    if (photoResponse != null) {
                        photos = photoResponse.getData();
                        photoAdapter.setData(photos);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<PhotoResponse> call, @NonNull Throwable t) {
                Snackbar.make(findViewById(R.id.swipe_refresh_layout)
                        , "fail to update data!"
                        , Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
