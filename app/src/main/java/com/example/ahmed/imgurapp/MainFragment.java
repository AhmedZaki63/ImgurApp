package com.example.ahmed.imgurapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.imgurapp.Adapters.PhotoAdapter;
import com.example.ahmed.imgurapp.Models.Photo;
import com.example.ahmed.imgurapp.Models.PhotoResponse;
import com.example.ahmed.imgurapp.Network.PhotoApi;
import com.example.ahmed.imgurapp.Network.PhotoClient;
import com.example.ahmed.imgurapp.Util.EndlessRecyclerViewScrollListener;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    ArrayList<Photo> photos;
    PhotoAdapter photoAdapter;
    EndlessRecyclerViewScrollListener viewScrollListener;
    PhotoApi photoApi;
    String sort;
    int page = 0;

    @BindView(R.id.rv_of_photos)
    RecyclerView photoView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private boolean preferenceChanged;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        photos = new ArrayList<>();
        photoAdapter = new PhotoAdapter(photos, getContext());
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        viewScrollListener =
                new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
                    @Override
                    public void onLoadMore(int p, int totalItemsCount, RecyclerView view) {
                        page++;
                        fetchPhotosData();
                    }
                };

        photoView.setAdapter(photoAdapter);
        photoView.setLayoutManager(staggeredGridLayoutManager);
        photoView.addOnScrollListener(viewScrollListener);

        photoApi = PhotoClient.createApi(PhotoClient.buildRetrofit());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                viewScrollListener.resetState();
                fetchPhotosData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey("photos")) {
            photos = Parcels.unwrap(savedInstanceState.getParcelable("photos"));
            photoAdapter.setData(photos);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sort = prefs
                .getString("prefs_sort_list_key", getString(R.string.pref_default_sort));
        if (preferenceChanged || photos.isEmpty()) {
            page = 0;
            viewScrollListener.resetState();
            fetchPhotosData();
            preferenceChanged = false;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!photos.isEmpty())
            outState.putParcelable("photos", Parcels.wrap(photos));
    }

    public void fetchPhotosData() {
        photoApi.getPhotosData("hot", sort, page
                , BuildConfig.PHOTO_CLIENT_ID).enqueue(new retrofit2.Callback<PhotoResponse>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<PhotoResponse> call
                    , @NonNull retrofit2.Response<PhotoResponse> response) {
                Log.v("url", response.raw().request().url().toString());
                if (response.isSuccessful()) {
                    PhotoResponse photoResponse = response.body();
                    if (photoResponse != null) {
                        photos = photoResponse.getData();
                        if (page == 0)
                            photoAdapter.setData(photos);
                        else
                            photoAdapter.addData(photos);
                    }
                    if (getView() != null)
                        Snackbar.make(getView(), "Data Updated!"
                                , Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<PhotoResponse> call, @NonNull Throwable t) {
                if (getView() != null)
                    Snackbar.make(getView(), "Fail to Update Data!"
                            , Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("prefs_sort_list_key"))
            preferenceChanged = true;
    }
}
